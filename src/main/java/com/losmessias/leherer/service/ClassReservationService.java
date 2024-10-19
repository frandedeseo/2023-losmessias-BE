package com.losmessias.leherer.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.losmessias.leherer.domain.*;
import com.losmessias.leherer.domain.enumeration.ReservationStatus;
import com.losmessias.leherer.dto.ClassReservationCancelDto;
import com.losmessias.leherer.dto.ProfessorStaticsDto;
import com.losmessias.leherer.repository.ClassReservationRepository;
import com.losmessias.leherer.repository.interfaces.ProfessorDailySummary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ClassReservationService {

    private final ClassReservationRepository classReservationRepository;
    private final ProfessorService professorService;
    private final SubjectService subjectService;
    private final NotificationService notificationService;

    @Autowired
    private CalendarService calendarService;

    public ClassReservation getReservationById(Long id) {
        return classReservationRepository.findById(id).orElse(null);
    }

    public ClassReservation cancelReservation(ClassReservationCancelDto classReservationCancelDto) {
        ClassReservation classReservation = getReservationById(classReservationCancelDto.getId());
        classReservation.setStatus(ReservationStatus.CANCELLED);
        if (checkIfIsBetween48hsBefore(classReservation)) {
            classReservation.setPrice(classReservation.getPrice() / 2);
        } else {
            classReservation.setPrice(0.0);
        }
        AppUser professor = classReservation.getProfessor();
        AppUser student = classReservation.getStudent();
        if (Objects.equals(classReservationCancelDto.getIdCancelsUser(), student.getId())){
            notificationService.cancelClassReservedNotification(classReservation, professor);
        }else if(Objects.equals(classReservationCancelDto.getIdCancelsUser(), professor.getId())){
            notificationService.cancelClassReservedNotification(classReservation, student);
        }else{
            //TODO throw exception
        }
        classReservationRepository.save(classReservation);
        return classReservation;

    }

    private boolean checkIfIsBetween48hsBefore(ClassReservation classReservation) {
        return classReservation.getDate().minusDays(2).isBefore(LocalDate.now()) || classReservation.getDate().minusDays(2).isEqual(LocalDate.now()) && classReservation.getStartingHour().isBefore(LocalTime.now());
    }

    private Event addAttendeesToGoogleCalendarEvent(Event event, Professor professor, Student student) {
        EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail(professor.getEmail()),
                new EventAttendee().setEmail(student.getEmail())
        };
        event.setAttendees(Arrays.asList(attendees));

        return event;
    }

    public ClassReservation createReservation(Professor professor,
                                              Subject subject,
                                              Student student,
                                              LocalDate day,
                                              LocalTime startingTime,
                                              LocalTime endingTime,
                                              Double price,
                                              String accessToken) { // Pass OAuth Credential here
        ClassReservation classReservation = new ClassReservation(
                professor,
                subject,
                student,
                day,
                startingTime,
                endingTime,
                price
        );

        try {
            // Pass the credential to create a Google Calendar event
            Event event = createGoogleCalendarEvent(classReservation, accessToken);
            // Add attendees (professor and student)
            event = addAttendeesToGoogleCalendarEvent(event, professor, student);

            // Store the Google Calendar event ID and Google Meet link
            classReservation.setGoogleCalendarEventId(event.getId());
            classReservation.setGoogleMeetLink(event.getHangoutLink());
        } catch (Exception e) {
            e.printStackTrace();
            // Handle error as necessary
        }

        notificationService.generateClassReservedNotification(classReservation);

        return classReservationRepository.save(classReservation);
    }

    // Método para crear el evento en Google Calendar
    private Event createGoogleCalendarEvent(ClassReservation reservation, String accessToken) throws Exception {
        // Use the access token to authenticate the Calendar service
        Calendar service = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(), null)
                .setApplicationName("Lehrer")
                .setHttpRequestInitializer(request -> request.getHeaders().setAuthorization("Bearer " + accessToken))
                .build();

        // Configure the event
        Event event = new Event()
                .setSummary("Class Reservation - " + reservation.getSubject().getName())
                .setLocation("Google Meet")
                .setDescription("Class with " + reservation.getProfessor().getFirstName());

        // Set start and end time in local time zone
        DateTime startDateTime = new DateTime(reservation.getDate() + "T" + reservation.getStartingHour());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Argentina/Buenos_Aires"); // ART timezone
        event.setStart(start);

        DateTime endDateTime = new DateTime(reservation.getDate() + "T" + reservation.getEndingHour());
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Argentina/Buenos_Aires"); // ART timezone
        event.setEnd(end);

        // Add Google Meet conference link
        ConferenceData conferenceData = new ConferenceData();
        CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest();
        createConferenceRequest.setRequestId("some-random-string-" + System.currentTimeMillis());
        conferenceData.setCreateRequest(createConferenceRequest);
        event.setConferenceData(conferenceData);

        // Insert the event into the user's calendar
        Calendar.Events.Insert request = service.events().insert("primary", event);
        request.setConferenceDataVersion(1); // Required to include conference data
        Event createdEvent = request.execute();

        return createdEvent;
    }

    private DateTime getDateTime(LocalDate date, LocalTime time) {
        // Combine the date and time into a LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.of(date, time);

        // Convert to ZonedDateTime with system default time zone or UTC if preferred
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());  // You can change to ZoneOffset.UTC if needed

        // Return the DateTime in the proper ISO 8601 format
        return new DateTime(zonedDateTime.toInstant().toString());
    }

    public boolean existsReservationForProfessorOrStudentOnDayAndTime(Long professor,
                                                             Long student,
                                                             LocalDate day,
                                                             LocalTime startingTime,
                                                             LocalTime endingTime) {
        int overlappingProfessor = classReservationRepository.countOverlappingReservations(
                professor,
                day,
                startingTime,
                endingTime);
        int overlappingStudent = classReservationRepository.countOverlappingReservationsForStudent(
                student,
                day,
                startingTime,
                endingTime);
        return overlappingProfessor > 0 || overlappingStudent > 0;
    }

    public List<ClassReservation> getReservationsByAppUserId(Long id) {
        return getUnCancelledReservation(classReservationRepository.findByStudentIdOrProfessorId(id, id));
    }

    private List<ClassReservation> getUnCancelledReservation(List<ClassReservation> classes) {
        List<ClassReservation> classesUnCancelled = new ArrayList<>();
        classes
                .stream()
                .filter(clase -> clase.getStatus() != ReservationStatus.CANCELLED)
                .forEach(classesUnCancelled::add);
        return classesUnCancelled;
    }

    public ClassReservation createUnavailableReservation(Professor professor, LocalDate day, LocalTime startingTime, LocalTime endingTime) {
        if (startingTime.isAfter(endingTime))
            throw new IllegalArgumentException("Starting time must be before ending time");
        ClassReservation classReservation = new ClassReservation(professor, day, startingTime, endingTime);
        return classReservationRepository.save(classReservation);
    }

/*
    public List<ClassReservation> getAllReservations() {
        return classReservationRepository.findAll();
    }
    public List<ClassReservation> createMultipleUnavailableReservationsFor(Professor professor, LocalDate day, LocalTime startingTime, LocalTime endingTime) {
        if (startingTime.isAfter(endingTime))
            throw new IllegalArgumentException("Starting time must be before ending time");
        List<LocalTime> intervals = generateTimeIntervals(startingTime, endingTime);
        List<ClassReservation> unavailableReservations = new ArrayList<>();
        for (LocalTime interval : intervals) {
            ClassReservation classReservation = new ClassReservation(professor, day, interval, interval.plusMinutes(30));
            unavailableReservations.add(classReservation);
        }
        return classReservationRepository.saveAll(unavailableReservations);
    }
   private List<LocalTime> generateTimeIntervals(LocalTime startTime, LocalTime endTime) {
        List<LocalTime> intervals = new ArrayList<>();
        while (startTime.isBefore(endTime)) {
            intervals.add(startTime);
            startTime = startTime.plusMinutes(30);
        }
        return intervals;
    }

    public List<ClassReservation> getByProfessorAndSubject(Long professorId, Long subjectId) {
        Professor professor = professorService.getProfessorById(professorId);
        Subject subject = subjectService.getSubjectById(subjectId);
        return classReservationRepository.findByProfessorAndSubject(professor, subject);
    }

    */

    public List<ProfessorDailySummary> getDailySummary(LocalDate day) {
        return classReservationRepository.getProfessorDailySummaryByDay(day);
    }



    public List<ProfessorStaticsDto> getStatics(Long id) {

        List<ClassReservation> classes = classReservationRepository.getClassReservationByProfessorAndOrderByDate(id);

        Integer amountOfMonths = Period.between(classes.get(0).getDate(), LocalDate.now()).getMonths() + 1;

        List<ClassReservation> currentMonth = new ArrayList<>();
        List<ClassReservation> prevMonth = new ArrayList<>();
        for (ClassReservation res : classes) {
            if (res.getDate().getYear() == LocalDate.now().getYear()) {
                if (res.getDate().getMonthValue() == LocalDate.now().getMonthValue()) {
                    currentMonth.add(res);
                } else if (res.getDate().getMonthValue() == LocalDate.now().getMonthValue() - 1) {
                    prevMonth.add(res);
                }
            }
        }

        ProfessorStaticsDto average = getProfessorStatic(classes);
        ProfessorStaticsDto currMonthStatics = getProfessorStatic(currentMonth);
        ProfessorStaticsDto prevMonthStatics = getProfessorStatic(prevMonth);

        average.getClassesPerSubject().replaceAll((k, v) -> v / amountOfMonths);
        ProfessorStaticsDto average_statics = new ProfessorStaticsDto(
                (double) average.getTotalClasses() / amountOfMonths,
                average.getClassesPerSubject(),
                average.getIncomes() / amountOfMonths,
                average.getCancelledClasses() / amountOfMonths
        );
        List<ProfessorStaticsDto> returnedList = new ArrayList<>();
        returnedList.add(currMonthStatics);
        returnedList.add(prevMonthStatics);
        returnedList.add(average_statics);
        return returnedList;
    }

    private ProfessorStaticsDto getProfessorStatic(List<ClassReservation> classes) {
        HashMap<String, Double> classesPerSubject = new HashMap<>();
        Double amountOfClasses = (double) classes.size();
        Double incomes = 0.0;
        Double amountOfCancelledClasses = 0.0;
        for (ClassReservation res : classes) {
            incomes += res.getPrice();

            if (res.getStatus() == ReservationStatus.CANCELLED) {
                amountOfCancelledClasses += 1;
            }

            if (classesPerSubject.get(res.getSubject().getName()) == null) {
                classesPerSubject.put(res.getSubject().getName(), 1.0);
            } else {
                classesPerSubject.put(res.getSubject().getName(), (Double) classesPerSubject.get(res.getSubject().getName()) + 1);
            }
        }
        return new ProfessorStaticsDto(
                amountOfClasses,
                classesPerSubject,
                incomes,
                amountOfCancelledClasses
        );
    }

    public List<ClassReservation> getReservationsByDateAndEndingTime(LocalDate date, LocalTime endingHour) {
        return classReservationRepository.findByDateAndEndingHour(date,endingHour);
    }

    public void removeFeedbackFromConcludedClass(Long professorId, Long studentId) {
        List<ClassReservation> classes = getReservationsByAppUserId(studentId);
        professorService.removeFeedbackFromConcludedClass(professorId, classes);
    }
}

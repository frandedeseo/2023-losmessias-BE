package com.losmessias.leherer.tasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;

@Component
public class ClassReservationsOrchestrator {

    private static final Logger log = Logger.getLogger(ClassReservationsOrchestrator.class.getName());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron = "0 0/30 * * * *")
//    @Scheduled(cron = "0 * * * * *")
    public void reserveClasses() {

        log.info("Reserving classes at " + dateFormat.format(System.currentTimeMillis()));
    }

}

//2023-11-08T17:58:59.962-03:00  INFO 54326 --- [  restartedMain] c.losmessias.leherer.LehererApplication  : Started LehererApplication in 12.593 seconds (process running for 12.873)
//2023-11-08T18:00:00.028-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:00
//2023-11-08T18:00:01.002-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:01
//2023-11-08T18:00:02.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:02
//2023-11-08T18:00:03.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:03
//2023-11-08T18:00:04.004-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:04
//2023-11-08T18:00:05.001-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:05
//2023-11-08T18:00:06.003-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:06
//2023-11-08T18:00:07.001-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:07
//2023-11-08T18:00:08.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:08
//2023-11-08T18:00:09.003-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:09
//2023-11-08T18:00:10.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:10
//2023-11-08T18:00:11.001-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:11
//2023-11-08T18:00:12.004-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:12
//2023-11-08T18:00:13.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:13
//2023-11-08T18:00:14.004-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:14
//2023-11-08T18:00:15.003-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:15
//2023-11-08T18:00:16.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:16
//2023-11-08T18:00:17.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:17
//2023-11-08T18:00:18.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:18
//2023-11-08T18:00:19.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:19
//2023-11-08T18:00:20.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:20
//2023-11-08T18:00:21.003-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:21
//2023-11-08T18:00:22.002-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:22
//2023-11-08T18:00:23.002-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:23
//2023-11-08T18:00:24.004-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:24
//2023-11-08T18:00:25.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:25
//2023-11-08T18:00:26.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:26
//2023-11-08T18:00:27.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:27
//2023-11-08T18:00:28.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:28
//2023-11-08T18:00:29.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:29
//2023-11-08T18:00:30.000-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:30
//2023-11-08T18:00:31.000-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:31
//2023-11-08T18:00:32.001-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:32
//2023-11-08T18:00:33.001-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:33
//2023-11-08T18:00:34.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:34
//2023-11-08T18:00:35.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:35
//2023-11-08T18:00:36.002-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:36
//2023-11-08T18:00:37.002-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:37
//2023-11-08T18:00:38.002-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:38
//2023-11-08T18:00:39.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:39
//2023-11-08T18:00:40.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:40
//2023-11-08T18:00:41.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:41
//2023-11-08T18:00:42.002-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:42
//2023-11-08T18:00:43.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:43
//2023-11-08T18:00:44.001-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:44
//2023-11-08T18:00:45.000-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:45
//2023-11-08T18:00:46.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:46
//2023-11-08T18:00:47.001-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:47
//2023-11-08T18:00:48.001-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:48
//2023-11-08T18:00:49.000-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:49
//2023-11-08T18:00:50.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:50
//2023-11-08T18:00:51.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:51
//2023-11-08T18:00:52.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:52
//2023-11-08T18:00:53.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:53
//2023-11-08T18:00:54.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:54
//2023-11-08T18:00:55.001-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:55
//2023-11-08T18:00:56.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:56
//2023-11-08T18:00:57.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:57
//2023-11-08T18:00:58.005-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:58
//2023-11-08T18:00:59.004-03:00  INFO 54326 --- [   scheduling-1] c.l.l.t.ClassReservationsOrchestrator    : Reserving classes at 18:00:59

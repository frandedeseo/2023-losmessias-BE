package com.losmessias.leherer.domain;

import com.losmessias.leherer.domain.enumeration.SubjectStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "professor_subject")
public class ProfessorSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "status")
    private SubjectStatus status;

    @Column(name="price")
    private Double price;

    public ProfessorSubject(Professor professor, Subject subject) {
        this.professor = professor;
        this.subject = subject;
        this.status = SubjectStatus.PENDING;
        this.price = null;
    }
    public ProfessorSubject(Professor professor, Subject subject, Double price) {
        this.professor = professor;
        this.subject = subject;
        this.status = SubjectStatus.PENDING;

        System.out.println("Price: " + price);
        System.out.println("Subject: " + subject);
        System.out.println("Subject Price: " + subject.getPrice());

        if (price != null && subject != null && subject.getPrice() != null) {
            double maxAllowedPrice = subject.getPrice() * 2;
            if (price > maxAllowedPrice) {
                throw new IllegalArgumentException("Price cannot be more than double the price per half hour.");
            }
        }
        this.price = price;
    }

    public void editPrice(Double new_price){
        if (new_price != null && subject != null && subject.getPrice() != null) {
            double maxAllowedPrice = subject.getPrice() * 2;
            if (new_price > maxAllowedPrice) {
                throw new IllegalArgumentException("Price cannot be more than the double of the suggested price");
            }else{
                setPrice(new_price);
            }
        }
    }
}

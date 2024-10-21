package com.losmessias.leherer.domain;

import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.domain.enumeration.FeedbackOptions;
import com.losmessias.leherer.dto.FeedbackDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "feedback_received")
public class FeedbackReceived {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @EqualsAndHashCode.Include
        @Column
        private Long id;
        @Column
        private Double avgRating = 0.0;
        @Column
        private Integer sumMaterial = 0;
        @Column
        private Integer sumPunctuality = 0;
        @Column
        private Integer sumPolite = 0;

        public void update(FeedbackDto feedbackDto, Double avg){
                if (feedbackDto.getPunctuality()){setSumPunctuality(sumPunctuality+1);}
                if (feedbackDto.getMaterial()){setSumMaterial(sumMaterial+1);}
                if (feedbackDto.getPolite()){setSumPolite(sumPolite+1);}
                setAvgRating(avg);
        }
}

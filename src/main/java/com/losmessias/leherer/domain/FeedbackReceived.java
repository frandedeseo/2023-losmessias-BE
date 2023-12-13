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
        private Double avgRating;
        @Column
        private Integer sumMaterial;
        @Column
        private Integer sumPunctuality;
        @Column
        private Integer sumPolite;

        public void update(FeedbackDto feedbackDto, Double avg){
                if (feedbackDto.getPunctuality()){setSumPunctuality(sumPunctuality+1);}
                if (feedbackDto.getMaterial()){setSumPunctuality(sumMaterial+1);}
                if (feedbackDto.getPolite()){setSumPunctuality(sumPolite+1);}
                setAvgRating(avg);
        }
}

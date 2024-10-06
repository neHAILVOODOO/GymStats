package com.voodoo.gymstats.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cardio")
public class Cardio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardioName;
    private double distance;
    private double time;
    private double weight;
    private double calories;
    private double pulse;
    private int position;
    private Long userId;

    @ManyToOne()
    @JoinColumn(name = "training_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Training training;

    public void changeCardio(Cardio change) {
        this.setCardioName(change.getCardioName());
        this.setDistance(change.getDistance());
        this.setTime(change.getTime());
        this.setWeight(change.getWeight());
        this.setCalories(change.getCalories());
        this.setPulse(change.getPulse());

    }


}

package com.voodoo.gymstats.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cardioProgram")
public class CardioProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardioName;
    private double weight;
    private int position;

    @ManyToOne()
    @JoinColumn(name = "trainingProgram_id")
    @JsonIgnore
    private TrainingProgram trainingProgram;


    public void changeCardioProgram(CardioProgram change) {
        this.setCardioName(change.getCardioName());
        this.setWeight(change.getWeight());
    }

}

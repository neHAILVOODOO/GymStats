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
@Table(name = "exercisesProgram")
public class ExerciseProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String exerciseName;
    private double weight;
    private int position;

    @ManyToOne
    @JoinColumn(name = "trainingProgram_id")
    @JsonIgnore
    private TrainingProgram trainingProgram;


    public void changeExerciseProgram(ExerciseProgram change) {
        setExerciseName(change.getExerciseName());
        setWeight(change.getWeight());

    }


}

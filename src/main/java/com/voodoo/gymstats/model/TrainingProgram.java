package com.voodoo.gymstats.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "trainingsProgram")
public class TrainingProgram   {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private DayOfWeek dayOfWeek;

    @OneToMany(mappedBy = "trainingProgram", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private List<ExerciseProgram> exercisePrograms;

    @OneToMany(mappedBy = "trainingProgram", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private List<CardioProgram> cardioPrograms;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    public void addExerciseProgramToTrainingProgram(ExerciseProgram exerciseProgram) {
        if (exercisePrograms == null) {
            exercisePrograms = new ArrayList<>();
        }
        exerciseProgram.setTrainingProgram(this);
        exercisePrograms.add(exerciseProgram);

    }

    public void addCardioProgramToTrainingProgram(CardioProgram cardioProgram) {
        if (cardioPrograms == null) {
            cardioPrograms = new ArrayList<>();
        }
        cardioProgram.setTrainingProgram(this);
        cardioPrograms.add(cardioProgram);
    }

    public void refreshExercisePositions() {

        if (exercisePrograms == null) {
            exercisePrograms = new ArrayList<>();
        }

        for (int i = 0; i < exercisePrograms.size(); i++) {
            exercisePrograms.get(i).setPosition(i);
        }
    }

    public void refreshCardioPositions() {

        if (cardioPrograms == null) {
            cardioPrograms = new ArrayList<>();
        }

        for (int i = 0; i < cardioPrograms.size(); i++) {
            cardioPrograms.get(i).setPosition(i);
        }
    }

}

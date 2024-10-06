package com.voodoo.gymstats.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "exercises")

public class Exercise {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String exerciseName;
        private double weight;
        private int set1;
        private int set2;
        private int set3;
        private int set4;
        private int position;
        private Long userId;

        @ManyToOne()
        @JoinColumn(name = "training_id")
        @ToString.Exclude
        @EqualsAndHashCode.Exclude
        private Training training;

        public void changeExercise(Exercise change) {
                this.setExerciseName(change.getExerciseName());
                this.setWeight(change.getWeight());
                this.setSet1(change.getSet1());
                this.setSet2(change.getSet2());
                this.setSet3(change.getSet3());
                this.setSet4(change.getSet4());
        }

        public void setExerciseFromProgram(ExerciseProgram exerciseProgram) {
                this.setExerciseName(exerciseProgram.getExerciseName());
                this.setWeight(exerciseProgram.getWeight());
        }

}

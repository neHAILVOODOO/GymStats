package com.voodoo.gymstats.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

    @Data
    @Entity
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Table(name = "trainings")
    public class Training {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private int todayWeight;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate trainingDate;

        @OneToMany( mappedBy = "training",fetch=FetchType.EAGER, cascade = CascadeType.ALL)
        @EqualsAndHashCode.Exclude
        @ToString.Exclude
        @JsonIgnore
        private List<Exercise> exercises;

        @OneToMany(mappedBy = "training", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
        @ToString.Exclude
        @EqualsAndHashCode.Exclude
        @JsonIgnore
        private List<Cardio> cardios;

        @ManyToOne(cascade = CascadeType.ALL)
        @ToString.Exclude
        @EqualsAndHashCode.Exclude
        @JoinColumn(name = "user_id")
        @JsonIgnore
        private User user;



        public void addExerciseToTraining(Exercise exercise) {
            if (exercises == null) {
                exercises = new ArrayList<>();
            }
            exercises.add(exercise);
            exercise.setTraining(this);
        }

        public void addCardioToTraining(Cardio cardio) {
            if (cardios == null) {
                cardios = new ArrayList<>();
            }
            cardios.add(cardio);
            cardio.setTraining(this);
        }


    }



package com.voodoo.gymstats.repository;

import com.voodoo.gymstats.model.Training;
import com.voodoo.gymstats.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.voodoo.gymstats.model.Exercise;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    Exercise findByExerciseNameAndTraining(String name, Training training);

    List<Exercise> findByTraining_UserAndExerciseName(User user, String name);

    Exercise findByTrainingAndPosition(Training training, int position);

    void deleteByTrainingAndPosition(Training training, int position);

}

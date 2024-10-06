package com.voodoo.gymstats.service;

import com.voodoo.gymstats.model.Exercise;
import com.voodoo.gymstats.model.Training;
import com.voodoo.gymstats.model.User;

import java.util.List;

public interface ExerciseService {

    Exercise findByTrainingAndPosition(Training training, int position);

    void deleteByTrainingAndPosition(Training training, int position);

    List<Exercise> findByTraining_UserAndExerciseName(User user, String name);

}

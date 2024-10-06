package com.voodoo.gymstats.service.impl;

import com.voodoo.gymstats.model.Exercise;
import com.voodoo.gymstats.model.Training;
import com.voodoo.gymstats.model.User;
import com.voodoo.gymstats.repository.ExerciseRepository;
import com.voodoo.gymstats.service.ExerciseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    private ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseServiceImpl(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public Exercise findByTrainingAndPosition(Training training, int position) {
        return exerciseRepository.findByTrainingAndPosition(training, position);
    }

    @Override
    @Transactional
    public void deleteByTrainingAndPosition(Training training, int position) {
        exerciseRepository.deleteByTrainingAndPosition(training, position);
    }

    @Override
    public List<Exercise> findByTraining_UserAndExerciseName(User user, String name) {
        return exerciseRepository.findByTraining_UserAndExerciseName(user,name);
    }

}

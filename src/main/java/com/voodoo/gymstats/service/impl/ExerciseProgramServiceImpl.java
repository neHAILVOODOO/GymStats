package com.voodoo.gymstats.service.impl;

import com.voodoo.gymstats.model.ExerciseProgram;
import com.voodoo.gymstats.model.TrainingProgram;
import com.voodoo.gymstats.repository.ExerciseProgramRepository;
import com.voodoo.gymstats.service.ExerciseProgramService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExerciseProgramServiceImpl implements ExerciseProgramService {

    private ExerciseProgramRepository exerciseProgramRepository;

    @Autowired
    public ExerciseProgramServiceImpl(ExerciseProgramRepository exerciseProgramRepository) {
        this.exerciseProgramRepository = exerciseProgramRepository;
    }

    @Override
    public ExerciseProgram findByTrainingProgramAndPosition(TrainingProgram trainingProgram, int position) {
        return exerciseProgramRepository.findByTrainingProgramAndPosition(trainingProgram, position);
    }

    @Override
    @Transactional
    public void deleteByTrainingProgramAndPosition(TrainingProgram trainingProgram, int position) {
      exerciseProgramRepository.deleteByTrainingProgramAndPosition(trainingProgram,position);
    }




}

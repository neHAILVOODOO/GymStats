package com.voodoo.gymstats.service;

import com.voodoo.gymstats.model.ExerciseProgram;
import com.voodoo.gymstats.model.TrainingProgram;

public interface ExerciseProgramService {

    ExerciseProgram findByTrainingProgramAndPosition(TrainingProgram trainingProgram, int position);

    void deleteByTrainingProgramAndPosition(TrainingProgram trainingProgram,int position);


}

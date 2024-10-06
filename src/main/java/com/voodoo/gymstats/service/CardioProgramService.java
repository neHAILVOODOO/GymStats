package com.voodoo.gymstats.service;

import com.voodoo.gymstats.model.CardioProgram;
import com.voodoo.gymstats.model.TrainingProgram;

public interface CardioProgramService {

    void deleteByTrainingProgramAndPosition(TrainingProgram trainingProgram,int position);

    CardioProgram findByTrainingProgramAndPosition(TrainingProgram trainingProgram, int position);


}

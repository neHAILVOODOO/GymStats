package com.voodoo.gymstats.service;

import com.voodoo.gymstats.model.TrainingProgram;
import com.voodoo.gymstats.model.User;

import java.time.DayOfWeek;

public interface TrainingProgramService {

    TrainingProgram saveTrainingProgram(TrainingProgram trainingProgram);

    TrainingProgram findTrainingProgramByDayOfWeekAndUser(DayOfWeek dayOfWeek, User user);



}

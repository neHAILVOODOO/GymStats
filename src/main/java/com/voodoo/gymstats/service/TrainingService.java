package com.voodoo.gymstats.service;

import com.voodoo.gymstats.model.Training;
import com.voodoo.gymstats.model.User;

import java.time.LocalDate;

public interface TrainingService {

    Training saveTraining(Training training);

    Training findTrainingByTrainingDateAndUser(LocalDate trainingDate, User user);


}

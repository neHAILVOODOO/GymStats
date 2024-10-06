package com.voodoo.gymstats.service;

import com.voodoo.gymstats.model.Cardio;
import com.voodoo.gymstats.model.Training;
import com.voodoo.gymstats.model.User;

import java.util.List;

public interface CardioService {

    Cardio findByTrainingAndPosition(Training training, int position);

    void deleteByTrainingAndPosition(Training training, int position);

    List<Cardio> findByTraining_UserAndCardioName(User user, String name);

}

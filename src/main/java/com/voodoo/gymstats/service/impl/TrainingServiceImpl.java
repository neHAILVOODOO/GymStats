package com.voodoo.gymstats.service.impl;

import com.voodoo.gymstats.model.Training;
import com.voodoo.gymstats.model.User;
import com.voodoo.gymstats.repository.TrainingRepository;
import com.voodoo.gymstats.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class TrainingServiceImpl implements TrainingService {

    private TrainingRepository trainingRepository;

    @Autowired
    public TrainingServiceImpl(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public Training saveTraining(Training training) {

        return trainingRepository.save(training);
    }

    @Override
    public Training findTrainingByTrainingDateAndUser(LocalDate trainingDate, User user) {
        return trainingRepository.findTrainingByTrainingDateAndUser(trainingDate, user);
    }

}

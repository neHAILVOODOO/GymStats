package com.voodoo.gymstats.service.impl;

import com.voodoo.gymstats.model.TrainingProgram;
import com.voodoo.gymstats.model.User;
import com.voodoo.gymstats.repository.TrainingProgramRepository;
import com.voodoo.gymstats.service.TrainingProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;

@Service
public class TrainingProgramServiceImpl implements TrainingProgramService {

    private TrainingProgramRepository trainingProgramRepository;


    @Autowired
    public TrainingProgramServiceImpl(TrainingProgramRepository trainingProgramRepository) {
        this.trainingProgramRepository = trainingProgramRepository;
    }

    @Override
    public TrainingProgram saveTrainingProgram(TrainingProgram trainingProgram) {
        return trainingProgramRepository.save(trainingProgram);
    }

    @Override
    public TrainingProgram findTrainingProgramByDayOfWeekAndUser(DayOfWeek dayOfWeek, User user) {
        return trainingProgramRepository.findTrainingProgramByDayOfWeekAndUser(dayOfWeek, user);
    }
}

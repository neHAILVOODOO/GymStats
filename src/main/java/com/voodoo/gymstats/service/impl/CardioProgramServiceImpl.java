package com.voodoo.gymstats.service.impl;

import com.voodoo.gymstats.model.CardioProgram;
import com.voodoo.gymstats.model.TrainingProgram;
import com.voodoo.gymstats.repository.CardioProgramRepository;
import com.voodoo.gymstats.service.CardioProgramService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CardioProgramServiceImpl implements CardioProgramService {

    private CardioProgramRepository cardioProgramRepository;

    @Autowired
    public CardioProgramServiceImpl(CardioProgramRepository cardioProgramRepository) {
        this.cardioProgramRepository = cardioProgramRepository;
    }


    @Override
    @Transactional
    public void deleteByTrainingProgramAndPosition(TrainingProgram trainingProgram, int position) {
        cardioProgramRepository.deleteByTrainingProgramAndPosition(trainingProgram,position);
    }

    @Override
    public CardioProgram findByTrainingProgramAndPosition(TrainingProgram trainingProgram, int position) {
        return cardioProgramRepository.findByTrainingProgramAndPosition(trainingProgram,position);
    }

}

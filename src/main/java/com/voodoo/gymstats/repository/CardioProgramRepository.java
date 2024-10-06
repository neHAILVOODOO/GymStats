package com.voodoo.gymstats.repository;

import com.voodoo.gymstats.model.CardioProgram;
import com.voodoo.gymstats.model.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardioProgramRepository extends JpaRepository<CardioProgram, Long> {

    CardioProgram findByCardioNameAndTrainingProgram(String cardioName, TrainingProgram trainingProgram);

    void deleteByTrainingProgramAndPosition(TrainingProgram trainingProgram,int position);

    CardioProgram findByTrainingProgramAndPosition(TrainingProgram trainingProgram, int position);



}

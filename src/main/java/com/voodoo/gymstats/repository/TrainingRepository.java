package com.voodoo.gymstats.repository;

import com.voodoo.gymstats.model.Training;
import com.voodoo.gymstats.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TrainingRepository extends JpaRepository<Training, Long> {

    Training findTrainingByTrainingDateAndUser(LocalDate trainingDate, User user);

}

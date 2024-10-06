package com.voodoo.gymstats.repository;

import com.voodoo.gymstats.model.Cardio;
import com.voodoo.gymstats.model.Training;
import com.voodoo.gymstats.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardioRepository extends JpaRepository<Cardio, Long> {


    Cardio findByCardioNameAndTraining(String cardioName, Training training);

    Cardio findByTrainingAndPosition(Training training, int position);

    void deleteByTrainingAndPosition(Training training, int position);

    List<Cardio> findByTraining_UserAndCardioName(User user, String name);
}

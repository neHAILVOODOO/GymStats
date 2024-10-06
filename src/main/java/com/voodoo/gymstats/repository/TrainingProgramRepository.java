package com.voodoo.gymstats.repository;

import com.voodoo.gymstats.model.TrainingProgram;
import com.voodoo.gymstats.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;

public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Long> {

    TrainingProgram findTrainingProgramByDayOfWeekAndUser(DayOfWeek dayOfWeek, User user);

}

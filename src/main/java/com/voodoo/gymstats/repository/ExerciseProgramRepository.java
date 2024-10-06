package com.voodoo.gymstats.repository;

import com.voodoo.gymstats.model.ExerciseProgram;
import com.voodoo.gymstats.model.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseProgramRepository extends JpaRepository<ExerciseProgram, Long> {

    ExerciseProgram findByTrainingProgramAndPosition(TrainingProgram trainingProgram, int position);

    void deleteByTrainingProgramAndPosition(TrainingProgram trainingProgram,int position);

    ExerciseProgram deleteExerciseProgramById(Long id);


}

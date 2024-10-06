package com.voodoo.gymstats.service.wrapper;

import com.voodoo.gymstats.model.Cardio;
import com.voodoo.gymstats.model.CardioProgram;
import com.voodoo.gymstats.model.Exercise;
import com.voodoo.gymstats.model.ExerciseProgram;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.List;

@Setter
@Getter
public class TrainingProgramWrapper {

    private DayOfWeek dayOfWeek;
    private List<ExerciseProgram> exercisePrograms;
    private List<CardioProgram> cardioPrograms;

}

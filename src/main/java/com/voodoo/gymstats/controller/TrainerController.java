package com.voodoo.gymstats.controller;


import com.voodoo.gymstats.model.*;
import com.voodoo.gymstats.service.*;
import com.voodoo.gymstats.service.wrapper.ExercisesWrapper;
import com.voodoo.gymstats.service.wrapper.TrainingProgramWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TrainerController {


    private UserService userService;
    private ExerciseService exerciseService;
    private CardioService cardioService;
    private TrainingService trainingService;
    private ExerciseProgramService exerciseProgramService;
    private CardioProgramService cardioProgramService;
    private TrainingProgramService trainingProgramService;

    @Autowired
    public TrainerController(UserService userService,
                             ExerciseService exerciseService, TrainingService trainingService, CardioService cardioService,
                             ExerciseProgramService exerciseProgramService, CardioProgramService cardioProgramService, TrainingProgramService trainingProgramService
    ) {

        this.userService = userService;
        this.exerciseService = exerciseService;
        this.cardioService = cardioService;
        this.trainingService = trainingService;
        this.exerciseProgramService = exerciseProgramService;
        this.cardioProgramService = cardioProgramService;
        this.trainingProgramService = trainingProgramService;

    }


    @PreAuthorize("hasAnyRole('ROLE_TRAINER', 'ROLE_FOUNDER')")
    @GetMapping("/users/profile/trainer/students")
    public String listStudents(@AuthenticationPrincipal User user,
                               Model model) {

        LocalDate localDate = LocalDate.now();

        List<User> users = userService.findStudentsByTrainerId(user.getId());


        model.addAttribute("user", user);
        model.addAttribute("users", users);
        model.addAttribute("localDate", localDate);

        return "trainer-students-list";
    }


    @PreAuthorize("hasAnyRole('ROLE_TRAINER', 'ROLE_FOUNDER')")
    @GetMapping("/users/profile/trainer/students/{userId}/profile")
    public String studentProfile(@PathVariable("userId") long userId, @AuthenticationPrincipal User authUser, Model model) {

        List<User> students = userService.findStudentsByTrainerId(authUser.getId());

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == userId) {

                User student = userService.findById(userId);
                LocalDate localDate = LocalDate.now();

                model.addAttribute("user", student);
                model.addAttribute("authUser", authUser);
                model.addAttribute("localDate", localDate);

                return "trainer-students-profile";

            }
        }

        return null;
    }



    @PreAuthorize("hasAnyRole('ROLE_TRAINER', 'ROLE_FOUNDER')")
    @GetMapping("/users/profile/trainer/students/{userId}/diary/{date}")
    public String loadStudentDiary(Model model,
                                   @AuthenticationPrincipal User authUser,
                                   @PathVariable("date") String dateString,
                                   @PathVariable("userId") long userId) {

        List<User> students = userService.findStudentsByTrainerId(authUser.getId());

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == userId) {

                User student = userService.findById(userId);

                LocalDate currentDate;
                TrainingProgram trainingProgram;

                try {
                    currentDate = dateString.isEmpty() ? LocalDate.now() : LocalDate.parse(dateString);
                } catch (DateTimeParseException e) {
                    currentDate = LocalDate.now();
                }

                trainingProgram = trainingProgramService.findTrainingProgramByDayOfWeekAndUser(currentDate.getDayOfWeek(), student);
                LocalDate nextDate = currentDate.plusDays(1);
                LocalDate prevDate = currentDate.minusDays(1);

                ExercisesWrapper wrapper = new ExercisesWrapper();
                Training training = trainingService.findTrainingByTrainingDateAndUser(currentDate,student);

                wrapper.setExercises(new ArrayList<>());
                wrapper.getExercises().add(new Exercise());
                wrapper.getExercises().add(new Exercise());
                wrapper.getExercises().add(new Exercise());
                wrapper.getExercises().add(new Exercise());
                wrapper.getExercises().add(new Exercise());
                wrapper.getExercises().add(new Exercise());
                wrapper.getExercises().add(new Exercise());
                wrapper.getExercises().add(new Exercise());

                wrapper.setCardios(new ArrayList<>());
                wrapper.getCardios().add(new Cardio());
                wrapper.getCardios().add(new Cardio());

                if (training != null) {

                    wrapper.setTodayWeight(training.getTodayWeight());

                    for (int j = 0; j < wrapper.getExercises().size(); j++) {

                        Exercise existingExercise = exerciseService.findByTrainingAndPosition(training, j);
                        if (existingExercise != null) {
                            wrapper.getExercises().get(j).changeExercise(existingExercise);
                        }
                    }

                    for (int j = 0; j < wrapper.getExercises().size(); j++) {

                        Cardio existingCardio = cardioService.findByTrainingAndPosition(training, j);
                        if (existingCardio != null) {
                            wrapper.getCardios().get(j).changeCardio(existingCardio);
                        }
                    }


                } else {

                    if (trainingProgram != null) {
                        if (trainingProgram.getExercisePrograms() != null) {

                            for (int j = 0; j < trainingProgram.getExercisePrograms().size(); j++) {

                                wrapper.getExercises().get(j).setExerciseFromProgram(trainingProgram.getExercisePrograms().get(j));
                            }
                        }
                    }
                }

                model.addAttribute("authUser", authUser);
                model.addAttribute("user", student);
                model.addAttribute("wrapper", wrapper);
                model.addAttribute("currentDate", currentDate);
                model.addAttribute("nextDate", nextDate);
                model.addAttribute("prevDate", prevDate);

                return "trainer-students-diary";
            }
        }

        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_TRAINER', 'ROLE_FOUNDER')")
    @PostMapping("/users/profile/trainer/students/{userId}/diary/{date}")
    public String saveStudentDiary(@ModelAttribute ExercisesWrapper wrapper,
                            @AuthenticationPrincipal User authUser,
                            @PathVariable("date") String dateString,
                                   @PathVariable("userId") long userId

    ) {



        User user = userService.findById(userId);
        Training training = new Training();
        LocalDate currentDate = LocalDate.now();

        if (!dateString.isEmpty()) {
            currentDate = LocalDate.parse(dateString);
        }

        if (trainingService.findTrainingByTrainingDateAndUser(currentDate, user) != null) {
            training = trainingService.findTrainingByTrainingDateAndUser(currentDate, user);
            training.setTodayWeight(wrapper.getTodayWeight());
            training.getExercises().clear();
            training.getCardios().clear();
        } else {
            training.setUser(user);
            training.setTrainingDate(currentDate);
            training.setTodayWeight(wrapper.getTodayWeight());
            trainingService.saveTraining(training);

        }


        for (int i = 0; i < wrapper.getExercises().size(); i++) {

            Exercise exercise = wrapper.getExercises().get(i);

            if (!exercise.getExerciseName().isEmpty()) {

                if (exerciseService.findByTrainingAndPosition(training, i) != null) {
                    Exercise existingExercise = exerciseService.findByTrainingAndPosition(training, i);
                    existingExercise.changeExercise(exercise);
                    training.addExerciseToTraining(existingExercise);
                } else {
                    exercise.setPosition(i);
                    training.addExerciseToTraining(exercise);
                }
            } else {
                exerciseService.deleteByTrainingAndPosition(training, i);
            }
        }

        for (int i = 0; i < wrapper.getCardios().size(); i++) {

            Cardio cardio = wrapper.getCardios().get(i);

            if (!cardio.getCardioName().isEmpty()) {

                if (cardioService.findByTrainingAndPosition(training, i) != null) {
                    Cardio existingCardio = cardioService.findByTrainingAndPosition(training, i);
                    existingCardio.changeCardio(cardio);
                    training.addCardioToTraining(existingCardio);
                } else {
                    cardio.setPosition(i);
                    training.addCardioToTraining(cardio);
                }
            } else {
                cardioService.deleteByTrainingAndPosition(training, i);
            }
        }


        trainingService.saveTraining(training);


        return "redirect:/users/profile/trainer/students/" + userId + "/diary/" + currentDate;
    }

    @PreAuthorize("hasAnyRole('ROLE_TRAINER', 'ROLE_FOUNDER')")
    @GetMapping("/users/profile/trainer/students/{userId}/training-program")
    public String loadStudentTrainingProgram(Model model,
                                             @AuthenticationPrincipal User authUser,
                                      @RequestParam(name = "dayOfWeek", required = false) DayOfWeek dayOfWeek,
                                             @PathVariable("userId") long userId)  {

        List<User> students = userService.findStudentsByTrainerId(authUser.getId());

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == userId) {

                User user = userService.findById(userId);

                TrainingProgram trainingProgram;

                if (dayOfWeek != null) {
                    trainingProgram = trainingProgramService.findTrainingProgramByDayOfWeekAndUser(dayOfWeek, user);
                } else {
                    trainingProgram = trainingProgramService.findTrainingProgramByDayOfWeekAndUser(LocalDate.now().getDayOfWeek(), user);
                }


                TrainingProgramWrapper wrapper = new TrainingProgramWrapper();

                wrapper.setExercisePrograms(new ArrayList<>());
                wrapper.getExercisePrograms().add(new ExerciseProgram());
                wrapper.getExercisePrograms().add(new ExerciseProgram());
                wrapper.getExercisePrograms().add(new ExerciseProgram());
                wrapper.getExercisePrograms().add(new ExerciseProgram());
                wrapper.getExercisePrograms().add(new ExerciseProgram());
                wrapper.getExercisePrograms().add(new ExerciseProgram());
                wrapper.getExercisePrograms().add(new ExerciseProgram());
                wrapper.getExercisePrograms().add(new ExerciseProgram());

                wrapper.setCardioPrograms(new ArrayList<>());
                wrapper.getCardioPrograms().add(new CardioProgram());
                wrapper.getCardioPrograms().add(new CardioProgram());


                if (trainingProgram != null) {

                    for (int j = 0; j < wrapper.getExercisePrograms().size(); j++) {

                        ExerciseProgram existingExerciseProgram = exerciseProgramService.findByTrainingProgramAndPosition(trainingProgram, j);
                        if (existingExerciseProgram != null) {
                            wrapper.getExercisePrograms().get(j).changeExerciseProgram(existingExerciseProgram);
                        }
                    }

                    for (int j = 0; j < wrapper.getCardioPrograms().size(); j++) {

                        CardioProgram existingCardioProgram = cardioProgramService.findByTrainingProgramAndPosition(trainingProgram, j);
                        if (existingCardioProgram != null) {
                            wrapper.getCardioPrograms().get(j).changeCardioProgram(existingCardioProgram);
                        }
                    }

                }


                LocalDate localDate = LocalDate.now();

                model.addAttribute("user", user);
                model.addAttribute("wrapper", wrapper);
                model.addAttribute("localDate", localDate);
                model.addAttribute("authUser", authUser);
                model.addAttribute("userId", userId);

                return "trainer-students-training-program";
            }
        }
        return null;
    }


    @PreAuthorize("hasAnyRole('ROLE_TRAINER', 'ROLE_FOUNDER')")
    @PostMapping("/users/profile/trainer/students/{userId}/training-program")
    public String saveStudentTrainingProgram(@ModelAttribute TrainingProgramWrapper wrapper,
                                             @PathVariable("userId") long userId

    ) {

        User user = userService.findById(userId);

        TrainingProgram trainingProgram;


        if (trainingProgramService.findTrainingProgramByDayOfWeekAndUser(wrapper.getDayOfWeek(), user) != null) {
            trainingProgram = trainingProgramService.findTrainingProgramByDayOfWeekAndUser(wrapper.getDayOfWeek(), user);
            trainingProgram.getExercisePrograms().clear();
            trainingProgram.getCardioPrograms().clear();
        } else {
            trainingProgram = new TrainingProgram();
            trainingProgram.setUser(user);
            trainingProgram.setDayOfWeek(wrapper.getDayOfWeek());
            trainingProgramService.saveTrainingProgram(trainingProgram);
        }


        for (int i = 0; i < wrapper.getExercisePrograms().size(); i++) {

            ExerciseProgram exerciseProgram = wrapper.getExercisePrograms().get(i);

            if (!exerciseProgram.getExerciseName().isEmpty()) {

                if (exerciseProgramService.findByTrainingProgramAndPosition(trainingProgram, i) != null) {
                    ExerciseProgram existingExerciseProgram = exerciseProgramService.findByTrainingProgramAndPosition(trainingProgram, i);
                    existingExerciseProgram.changeExerciseProgram(exerciseProgram);
                    trainingProgram.addExerciseProgramToTrainingProgram(existingExerciseProgram);
                } else {
                    exerciseProgram.setPosition(i);
                    trainingProgram.addExerciseProgramToTrainingProgram(exerciseProgram);
                }
            } else {
                exerciseProgramService.deleteByTrainingProgramAndPosition(trainingProgram, i);
            }
        }


        for (int i = 0; i < wrapper.getCardioPrograms().size(); i++) {

            CardioProgram cardioProgram = wrapper.getCardioPrograms().get(i);

            if (!cardioProgram.getCardioName().isEmpty()) {

                if (cardioProgramService.findByTrainingProgramAndPosition(trainingProgram, i) != null) {
                    CardioProgram existingCardioProgram = cardioProgramService.findByTrainingProgramAndPosition(trainingProgram, i);
                    existingCardioProgram.changeCardioProgram(cardioProgram);
                    trainingProgram.addCardioProgramToTrainingProgram(existingCardioProgram);
                }
                else {
                    cardioProgram.setPosition(i);
                    trainingProgram.addCardioProgramToTrainingProgram(cardioProgram);
                }
            } else {
                cardioProgramService.deleteByTrainingProgramAndPosition(trainingProgram, i);
            }
        }

        trainingProgram.refreshExercisePositions();
        trainingProgram.refreshCardioPositions();

        trainingProgramService.saveTrainingProgram(trainingProgram);


        return "redirect:/users/profile/trainer/students/" + userId + "/training-program";

    }


    @GetMapping("/users/profile/trainer/students/{userId}/training-stats")

    public String getStudentTrainingStats(@AuthenticationPrincipal User authUser,

                                   @RequestParam(name = "inputTrainingType", required = false) String inputTrainingType,

                                   @RequestParam(name = "inputExerciseName", required = false) String inputExerciseName,
                                   @RequestParam(name = "inputExerciseParameter", required = false) String inputExerciseParameter,

                                   @RequestParam(name = "inputCardioName", required = false) String inputCardioName,
                                   @RequestParam(name = "inputCardioParameter", required = false) String inputCardioParameter,
                                   @PathVariable("userId") long userId,

                                   Model model) {


        List<User> students = userService.findStudentsByTrainerId(authUser.getId());

        for (int p = 0; p < students.size(); p++) {
            if (students.get(p).getId() == userId) {

                User user = userService.findById(userId);

                LocalDate localDate = LocalDate.now();
                List<Exercise> exercises = new ArrayList<>();
                List<Cardio> cardios = new ArrayList<>();
                String parameter = null;


                for (int i = 0; i < user.getTrainings().size(); i++) {

                    for (int j = 0; j < user.getTrainings().get(i).getExercises().size(); j++) {

                        boolean isEquals = false;
                        if (!exercises.isEmpty()) {
                            for (int z = 0; z < exercises.size(); z++) {
                                if (exercises.get(z).getExerciseName().equals(user.getTrainings().get(i).getExercises().get(j).getExerciseName())) {
                                    isEquals = true;
                                    break;
                                }
                            }
                            if (!isEquals) {
                                exercises.add(user.getTrainings().get(i).getExercises().get(j));
                            }
                        } else {
                            exercises.add(user.getTrainings().get(i).getExercises().get(j));
                        }

                    }

                    for (int j = 0; j < user.getTrainings().get(i).getCardios().size(); j++) {

                        boolean isEquals = false;
                        if (!cardios.isEmpty()) {
                            for (int z = 0; z < cardios.size(); z++) {
                                if (cardios.get(z).getCardioName().equals(user.getTrainings().get(i).getCardios().get(j).getCardioName())) {
                                    isEquals = true;
                                    break;
                                }
                            }
                            if (!isEquals) {
                                cardios.add(user.getTrainings().get(i).getCardios().get(j));
                            }
                        } else {
                            cardios.add(user.getTrainings().get(i).getCardios().get(j));
                        }
                    }
                }


                List<Cardio> cardiosByName;
                List<Exercise> exercisesByName;

                if ((inputExerciseName != null && inputExerciseParameter != null) || (inputCardioName != null && inputCardioParameter != null)) {

                    if (inputTrainingType.equals("EXERCISE")) {

                        exercisesByName = exerciseService.findByTraining_UserAndExerciseName(user, inputExerciseName);
                        parameter = inputExerciseParameter.toLowerCase();
                        model.addAttribute("exercisesByName", exercisesByName);

                    } else if (inputTrainingType.equals("CARDIO")) {

                        cardiosByName = cardioService.findByTraining_UserAndCardioName(user, inputCardioName);
                        parameter = inputCardioParameter.toLowerCase();
                        model.addAttribute("cardiosByName", cardiosByName);

                    }

                    model.addAttribute("parameter", parameter);

                }

                model.addAttribute("user", user);
                model.addAttribute("exercises", exercises);
                model.addAttribute("cardios", cardios);
                model.addAttribute("authUser", authUser);
                model.addAttribute("localDate", localDate);
                model.addAttribute("userId", userId);

                return "trainer-students-training-stats";
            }
        }
    return null;
    }



}

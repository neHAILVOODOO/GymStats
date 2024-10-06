package com.voodoo.gymstats.service.impl;

import com.voodoo.gymstats.dto.UserDto;
import com.voodoo.gymstats.model.Details;
import com.voodoo.gymstats.model.User;
import com.voodoo.gymstats.model.UserRole;
import com.voodoo.gymstats.repository.UserRepository;
import com.voodoo.gymstats.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();


        return users.stream().map((user) -> mapToUserDto(user)).collect(Collectors.toList());
    }


    @Override
    public User saveUser(UserDto userDto) {

        Details details = new Details();
        User user = mapToUser(userDto);
        details.setUser(user);
        user.setUserDetails(details);

        return userRepository.save(user);
    }

    @Override
    public Boolean existsUserByLogin(String login) {
        return userRepository.existsUserByLogin(login);
    }

    @Override
    public List<User> findStudentsByTrainerId(Long trainerId) {
        return userRepository.findAllByTrainerId(trainerId);
    }

    @Override
    public List<User> findUsersByRole(UserRole userRole) {
        return userRepository.findAllByRole(userRole);
    }

    @Override
    public UserDto findUserById(Long userId) {
        User user = userRepository.findById(userId).get();
        return mapToUserDto(user);
    }

    @Override
    public User findById(Long userId) {
        User user = userRepository.findById(userId).get();
        return user;
    }

    @Override
    public void updateUser(UserDto userDto) {
        User user = mapToUser(userDto);
        userRepository.save(user);
    }

    @Override
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> searchUsers(String query) {
        List<User> users = userRepository.searchUsers(query);

        return users.stream().map(user -> mapToUserDto(user)).collect(Collectors.toList());
    }

    public User mapToUser(UserDto userDto) {


        User user = User.builder()
                .id(userDto.getId())
                .login(userDto.getLogin())
                .password(userDto.getPassword())
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .role(userDto.getRole())
                .filename(userDto.getFilename())
                .active(userDto.getActive())
                .trainerId(userDto.getTrainerId())
                .userDetails(userDto.getUserDetails())
                .createdOnDate(userDto.getCreatedOnDate())
                .lastVisitDate(userDto.getLastVisitDate())
                .trainings(userDto.getTrainings())
                .trainingPrograms(userDto.getTrainingPrograms())
                .build();

        return user;
    }

    public UserDto mapToUserDto(User user) {

        UserDto userDto = UserDto.builder()

                .id(user.getId())
                .login(user.getLogin())
                .password(user.getPassword())
                .name(user.getName())
                .surname(user.getSurname())
                .role(user.getRole())
                .filename(user.getFilename())
                .active(user.getActive())
                .trainerId(user.getTrainerId())
                .userDetails(user.getUserDetails())
                .createdOnDate(user.getCreatedOnDate())
                .lastVisitDate(user.getLastVisitDate())
                .trainings(user.getTrainings())
                .trainingPrograms(user.getTrainingPrograms())
                .build();

        return userDto;
    }

}

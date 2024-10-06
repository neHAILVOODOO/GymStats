package com.voodoo.gymstats.service;

import com.voodoo.gymstats.model.User;
import com.voodoo.gymstats.dto.UserDto;
import com.voodoo.gymstats.model.UserRole;

import java.util.List;

public interface UserService {

    List<UserDto> findAllUsers();

    User saveUser(UserDto userDto);

    Boolean existsUserByLogin(String login);

    List<User> findStudentsByTrainerId(Long trainerId);

    List<User> findUsersByRole(UserRole userRole);

    UserDto findUserById(Long userId);

    User findById(Long userId);

    void updateUser(UserDto user);

    void delete(Long userId);

    List<UserDto> searchUsers(String query);

     User mapToUser(UserDto userDto);

    UserDto mapToUserDto(User user);


}

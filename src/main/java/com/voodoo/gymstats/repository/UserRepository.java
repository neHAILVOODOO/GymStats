package com.voodoo.gymstats.repository;

import com.voodoo.gymstats.model.User;
import com.voodoo.gymstats.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);

    User findByLogin(String login);

    Boolean existsUserByLogin(String login);

    List<User> findAll();

    @Query("SELECT u from User u WHERE u.name LIKE CONCAT('%', :query, '%') OR u.surname LIKE CONCAT('%', :query, '%')")
    List<User> searchUsers(String query);

    List<User> findAllByRole(UserRole role);
    List<User> findAllByTrainerId(Long trainerId);
}

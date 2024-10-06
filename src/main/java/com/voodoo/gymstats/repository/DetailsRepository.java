package com.voodoo.gymstats.repository;

import com.voodoo.gymstats.model.Details;
import com.voodoo.gymstats.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailsRepository extends JpaRepository<Details, Long> {

    Details findByUser(User user);


}

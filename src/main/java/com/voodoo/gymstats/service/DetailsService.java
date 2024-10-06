package com.voodoo.gymstats.service;

import com.voodoo.gymstats.model.Details;
import com.voodoo.gymstats.model.User;

public interface DetailsService {

    Details findByUser(User user);

}

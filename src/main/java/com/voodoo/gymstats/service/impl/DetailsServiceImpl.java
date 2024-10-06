package com.voodoo.gymstats.service.impl;

import com.voodoo.gymstats.model.Details;
import com.voodoo.gymstats.model.User;
import com.voodoo.gymstats.repository.DetailsRepository;
import com.voodoo.gymstats.service.DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DetailsServiceImpl implements DetailsService {

    private DetailsRepository detailsRepository;

    @Autowired
    public DetailsServiceImpl(DetailsRepository detailsRepository) {
        this.detailsRepository = detailsRepository;
    }

    @Override
    public Details findByUser(User user) {
        return detailsRepository.findByUser(user);
    }

}

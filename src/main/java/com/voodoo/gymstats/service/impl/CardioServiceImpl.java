package com.voodoo.gymstats.service.impl;

import com.voodoo.gymstats.model.Cardio;
import com.voodoo.gymstats.model.Training;
import com.voodoo.gymstats.model.User;
import com.voodoo.gymstats.repository.CardioRepository;
import com.voodoo.gymstats.service.CardioService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardioServiceImpl implements CardioService {

    private CardioRepository cardioRepository;

    @Autowired
    public CardioServiceImpl(CardioRepository cardioRepository) {
        this.cardioRepository = cardioRepository;
    }


    @Override
    public Cardio findByTrainingAndPosition(Training training, int position) {
        return cardioRepository.findByTrainingAndPosition(training, position);
    }

    @Override
    @Transactional
    public void deleteByTrainingAndPosition(Training training, int position) {
        cardioRepository.deleteByTrainingAndPosition(training,position);
    }

    @Override
    public List<Cardio> findByTraining_UserAndCardioName(User user, String name) {
        return cardioRepository.findByTraining_UserAndCardioName(user, name);
    }

}

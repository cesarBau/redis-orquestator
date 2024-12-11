package com.redis.redis.service;

import java.util.List;

import com.redis.redis.model.PetitionModel;

public interface IMasiveService {

    void createResources(String id, Integer value);
    List<PetitionModel> generateResources(String id);
    void executeRequest(PetitionModel data);

}

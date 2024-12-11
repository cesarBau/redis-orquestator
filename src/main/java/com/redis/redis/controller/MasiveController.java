package com.redis.redis.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redis.redis.model.PetitionModel;
import com.redis.redis.model.RedisModel;
import com.redis.redis.model.dto.LineDto;
import com.redis.redis.service.IMasiveService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;



@RestController
@RequestMapping("/redis")
public class MasiveController {

    private static Logger logger = LoggerFactory.getLogger(MasiveController.class);

    @Autowired
    private final IMasiveService iMasiveService;

    public MasiveController(IMasiveService iMasiveService) {
        this.iMasiveService = iMasiveService;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public RedisModel createMasive(@RequestBody RedisModel entity) {
        logger.info("Consume controller createMasive");
        iMasiveService.createResources(entity.getId(), entity.getValue());
        return entity;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LineDto createResources(@PathVariable String id) {
        logger.info("Consume controller createResources");
        List<PetitionModel> consume = iMasiveService.generateResources(id);
        LineDto response = new LineDto(id, "sucess", consume);
        return response;
    }
    
}

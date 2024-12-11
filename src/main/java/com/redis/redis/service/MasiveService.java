package com.redis.redis.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.redis.model.PetitionModel;

import redis.clients.jedis.Jedis;

@Service
@PropertySource("classpath:other.properties")
public class MasiveService implements IMasiveService {

    private static Logger logger = LoggerFactory.getLogger(MasiveService.class);

    @Value("${url.orchestrator}")
    private String resourceUrl;

    @Value("${url.username}")
    private String username;

    @Value("${url.password}")
    private String password;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private Jedis redisConfiguration;

    @Override
    public void createResources(String id, Integer value) {
        logger.info("Consume service createResources");
        redisConfiguration.set(id, Integer.toString(value));
    }

    @Override
    public List<PetitionModel> generateResources(String id) {
        logger.info("Consume service generateResources");
        logger.info(String.format("The key is: %s", id));
        String cachedResponse = redisConfiguration.get(id);
        if (cachedResponse == null){
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Key not found", null);
        }
        logger.info(String.format("Value %s", cachedResponse));
        List<PetitionModel> creations =  new ArrayList<>();
        for (int i = 0; i < Integer.valueOf(cachedResponse); i++) {
            PetitionModel data = new PetitionModel(String.valueOf(i));
            creations.add(data);
            executeRequest(data);
        }
        return creations;
    }

    @Override
    public void executeRequest(PetitionModel data) {
        logger.info("Consume service executeRequest");
        String json = "";
        try {
            logger.info("Create JSON");
            json = objectMapper.writeValueAsString(data);
            logger.info(json);
        } catch (JsonProcessingException e) {
            logger.info("Error to create JSON");
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(username, password);
        HttpEntity<String> request = new HttpEntity<>(json,headers);
        ResponseEntity<String> response = restTemplate.postForEntity(resourceUrl, request, String.class);
        logger.info(response.getBody());
    }

}

package com.redis.redis.model.dto;

import java.util.List;

import com.redis.redis.model.PetitionModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LineDto {

    private String id;
    private String message;
    private List<PetitionModel> values;

}

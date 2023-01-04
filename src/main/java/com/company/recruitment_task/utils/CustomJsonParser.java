package com.company.recruitment_task.utils;

import com.company.recruitment_task.model.person.dto.PersonDto;
import com.company.recruitment_task.service.PersonRestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CustomJsonParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonRestService.class);

    private final ObjectMapper mapper;
    private final JsonSchema jsonSchema;

    public CustomJsonParser(ObjectMapper mapper, JsonSchema jsonSchema) {
        this.mapper = mapper;
        this.jsonSchema = jsonSchema;
    }

    public PersonDto parsePersonJsonToDto(String json) {
        if (json == null) {
            return null;
        }
        try {
            JsonNode jsonNode = mapper.readTree(json);

            Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
            if (!errors.isEmpty()) {
                LOGGER.info("ERRORS: {} ", errors);
            }
            return mapper.readValue(json, PersonDto.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while parsing json to object: {}", e);
            return null;
        }
    }

}
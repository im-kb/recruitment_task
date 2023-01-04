package com.company.recruitment_task.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.company.recruitment_task.model.person.dto.PersonDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PersonDeserializer extends StdDeserializer<PersonDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonDeserializer.class);

    public PersonDeserializer() {
        this(null);
    }

    public PersonDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public PersonDto deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        Long externalId = null;
        Long id = null;
        String name = null;
        Integer height = 0;
        Integer mass = 0;

        try {
            externalId = node.get("externalId") != null ? node.get("externalId").asLong() : null;
        } catch (NumberFormatException nfe) {
            LOGGER.warn("Cannot deserialize field externalId with error: {}", nfe);
        }

        try {
            id = node.get("id") != null ? node.get("id").asLong() : null;
        } catch (NumberFormatException nfe) {
            LOGGER.warn("Cannot deserialize field id with error: {}", nfe);
        }

        try {
            height = node.get("height") != null ? node.get("height").asInt() : null;
        } catch (NumberFormatException nfe) {
            LOGGER.warn("Cannot deserialize field height with error: {}", nfe);
        }

        try {
            mass = node.get("mass") != null ? Integer.valueOf(node.get("mass").asText()) : 0;
        } catch (NumberFormatException nfe) {
            LOGGER.warn("Cannot deserialize field mass with error: {}", nfe.getMessage());
        }

        try {
            name = node.get("name") != null ? node.get("name").asText() : null;
        } catch (NumberFormatException nfe) {
            LOGGER.warn("Cannot deserialize field name with error: {}", nfe);
        }

        return PersonDto.builder()
                .id(id)
                .externalId(externalId)
                .name(name)
                .mass(mass)
                .height(height)
                .build();
    }

}
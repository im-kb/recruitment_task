package com.company.recruitment_task.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.company.recruitment_task.configuration.JsonSchemaConfiguration;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JsonSchemaValidatorTest {

    private static JsonSchema jsonSchema;
    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public static void init() {
        var jsonSchemaFactory = JsonSchemaFactory
                .builder(JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4))
                .objectMapper(mapper)
                .build();

        jsonSchema = jsonSchemaFactory.getSchema(JsonSchemaConfiguration.class.getResourceAsStream("/person_schema.json"));
    }

    @Test
    public void givenInvalidInput_whenValidating_thenInvalid() throws IOException {
        JsonNode jsonNode = mapper.readTree(
                JsonSchemaValidatorTest.class.getResourceAsStream("/invalid_person.json"));
        Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
        assertThat(errors).isNotEmpty();
        assertThat(errors).asString().contains("name: integer found, string expected");
        assertThat(errors).asString().contains("height: boolean found, string expected");
        assertThat(errors).asString().contains("mass: null found, string expected");
    }

    @Test
    public void givenValidInput_whenValidating_thenValid() throws IOException {
        JsonNode jsonNode = mapper.readTree(
                JsonSchemaValidatorTest.class.getResourceAsStream("/valid_person.json"));
        Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
        assertThat(errors).isEmpty();
    }

}
package com.company.recruitment_task.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonSchemaConfiguration {

    private final ObjectMapper objectMapper;

    public JsonSchemaConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public JsonSchema jsonSchemaValidator() {
        var jsonSchemaFactory = JsonSchemaFactory
                .builder(JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4))
                .objectMapper(objectMapper)
                .build();

        return jsonSchemaFactory.getSchema(JsonSchemaConfiguration.class.getResourceAsStream("/person_schema.json"));
    }

}
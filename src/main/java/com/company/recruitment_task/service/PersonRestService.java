package com.company.recruitment_task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.company.recruitment_task.service.PersonRestService.ApiPath.PEOPLE;

@Service
public class PersonRestService {
    private final String SWAPI_API_URL = "https://swapi.dev/api/";
    private final Logger LOGGER = LoggerFactory.getLogger(PersonRestService.class);

    private final RestTemplate restTemplate;

    public PersonRestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public enum ApiPath {
        PEOPLE("people");

        ApiPath(String path) {
            this.path = path;
        }

        final String path;
    }

    public String getPersonFromExternalApi(Long personId) {
        var responseJson = restTemplate.getForEntity(prepareGetPersonURI(SWAPI_API_URL, personId), String.class);

        if (responseJson.getStatusCode() == HttpStatus.OK) {
            LOGGER.info("Successfully fetched person from external api, external status code : {}", responseJson.getStatusCode());
            return responseJson.getBody();
        } else {
            LOGGER.warn("Error while fetching person from main external api, external status code : {}", responseJson.getStatusCode());
            return null;
        }
    }

    private URI prepareGetPersonURI(String apiUrl, Long personId) {
        String url = apiUrl + PEOPLE.path + "/{id}";

        Map<String, Object> params = new HashMap<>();
        params.put("id", personId);

        return UriComponentsBuilder
                .fromUriString(url)
                .uriVariables(params)
                .build()
                .toUri();
    }

}
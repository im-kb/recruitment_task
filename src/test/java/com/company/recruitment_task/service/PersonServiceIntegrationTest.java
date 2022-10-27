package com.company.recruitment_task.service;

import com.company.recruitment_task.model.person.dto.PersonDto;
import com.company.recruitment_task.repository.IPersonRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.company.recruitment_task.common.CommonTestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonServiceIntegrationTest {

    @LocalServerPort
    private int port;

    private static RestTemplate restTemplate;
    private final String URL = "http://localhost";

    @Autowired
    private IPersonRepository personRepository;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @Test
    @Transactional
    public void shouldReturn404WhenPersonNotFound() {
        //given
        ResponseEntity<PersonDto> response;

        try {
            response = restTemplate.getForEntity(baseApplicationUrl() + "/10", PersonDto.class);
        } catch (HttpClientErrorException.NotFound ex) {
            return;
        }

        //then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldImportSinglePerson() {
        //when
        var result = restTemplate.getForEntity(externalImportUrl(1L), PersonDto.class).getBody();

        //then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Luke Skywalker");
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getExternalId()).isEqualTo(1L);
        assertThat(result.getMass()).isEqualTo(77);
        assertThat(result.getHeight()).isEqualTo(172);
    }

    @Test
    public void shouldNotImportSamePersonTwice() {
        //given
        restTemplate.getForEntity(externalImportUrl(1L), PersonDto.class).getBody();
        restTemplate.getForEntity(externalImportUrl(1L), PersonDto.class).getBody();

        //then
        assertThat(personRepository.findAll().size() == 1);
    }

    @Test
    public void shouldGetExistingPerson() {
        //given
        personRepository.save(PERSON_1);

        //when
        var result = restTemplate.getForEntity(baseApplicationUrl() + "/1", PersonDto.class).getBody();

        //then
        assertThat(result).isEqualTo(PERSON_1_DTO);
    }

    @Test
    public void shouldGetCorrectPersonsFromKeywordSearch() {
        //given
        personRepository.save(PERSON_1);
        personRepository.save(PERSON_2);
        personRepository.save(PERSON_3);

        //when
        var result = restTemplate.getForEntity(prepareGetPersonByNameKeywordUri("lu"), PersonDto[].class).getBody();

        //then
        assertThat(result).hasSize(2);
        assertThat(result).contains(PERSON_1_DTO);
        assertThat(result).contains(PERSON_2_DTO);
    }

    private String baseApplicationUrl() {
        return URL + ":" + port + "/company-task/people/";
    }

    private String externalImportUrl(Long id) {
        return baseApplicationUrl() + "/external-import/" + id;
    }

    private URI prepareGetPersonByNameKeywordUri(String nameKeyword) {
        Map<String, String> params = new HashMap<>();
        params.put("nameKeyword", nameKeyword);

        return UriComponentsBuilder
                .fromUriString(baseApplicationUrl() + "/search/")
                .queryParam("nameKeyword", nameKeyword)
                .build()
                .toUri();
    }

}
package com.company.recruitment_task.mapper;

import com.company.recruitment_task.model.person.Person;
import com.company.recruitment_task.model.person.dto.PersonDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.List;

import static com.company.recruitment_task.common.CommonTestHelper.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class PersonMapperTest {

    private PersonMapper personMapper = new PersonMapper();

    @Test
    public void shouldMapToDto() {
        //given
        var expected = PERSON_1_DTO;

        //when
        var result = personMapper.mapToDto(PERSON_1);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void shouldMapFromDto() {
        //given
        var expected = PERSON_1;

        //when
        var result = personMapper.mapFromDto(PERSON_1_DTO);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @NullSource
    public void shouldReturnNullGivenNullInputWhenMapToDto(Person person) {
        //when
        var result = personMapper.mapToDto(person);

        //then
        assertThat(result).isEqualTo(null);
    }

    @ParameterizedTest
    @NullSource
    public void shouldReturnNullGivenNullInputWhenMapFromDto(PersonDto personDto) {
        //when
        var result = personMapper.mapFromDto(personDto);

        //then
        assertThat(result).isEqualTo(null);
    }

    @Test
    public void shouldMapToDtoList() {
        //given
        var expected = asList(PERSON_1_DTO, PERSON_2_DTO, PERSON_3_DTO);

        //when
        var result = personMapper.mapToDtoList(asList(null, PERSON_1, PERSON_2, PERSON_3, null));

        //then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @NullSource
    public void shouldReturnEmptyListWhenMapToDtoListGivenEmptyAndNullInput(List<Person> personList) {
        //when
        var result = personMapper.mapToDtoList(personList);

        //then
        assertThat(result).isEqualTo(emptyList());
    }

}
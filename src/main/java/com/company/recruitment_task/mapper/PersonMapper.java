package com.company.recruitment_task.mapper;

import com.company.recruitment_task.model.person.Person;
import com.company.recruitment_task.model.person.dto.PersonDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Component
public class PersonMapper {
    public Person mapFromDto(PersonDto personDto){
        if (personDto == null) {
            return null;
        }

        return Person.builder()
                .id(personDto.getId())
                .externalId(personDto.getExternalId())
                .name(personDto.getName())
                .height(personDto.getHeight())
                .mass(personDto.getMass())
                .build();
    }

    public PersonDto mapToDto(Person person){
        if (person == null) {
            return null;
        }

        return PersonDto.builder()
                .id(person.getId())
                .externalId(person.getExternalId())
                .name(person.getName())
                .height(person.getHeight())
                .mass(person.getMass())
                .build();
    }

    public List<PersonDto> mapToDtoList(List<Person> person){
        if (isEmpty(person)) {
            return emptyList();
        }

        return person.stream()
                .map(this::mapToDto)
                .filter(Objects::nonNull)
                .collect(toList());
    }

}
package com.company.recruitment_task.common;

import com.company.recruitment_task.model.person.Person;
import com.company.recruitment_task.model.person.dto.PersonDto;

public class CommonTestHelper {

    public static final PersonDto PERSON_1_DTO = preparePersonDto(1L, 10L, "Lucy", 180, 80);
    public static final PersonDto PERSON_2_DTO = preparePersonDto(2L, 20L, "Lucian", 185, 70);
    public static final PersonDto PERSON_3_DTO = preparePersonDto(3L, 30L, "Ted", 190, 60);

    public static final Person PERSON_1 = preparePerson(1L, 10L, "Lucy", 180, 80);
    public static final Person PERSON_2 = preparePerson(2L, 20L, "Lucian", 185, 70);
    public static final Person PERSON_3 = preparePerson(3L, 30L, "Ted", 190, 60);

    private static PersonDto preparePersonDto(Long id, Long externalId, String name, Integer height, Integer mass){
        return PersonDto.builder()
                .id(id)
                .height(height)
                .externalId(externalId)
                .mass(mass)
                .name(name)
                .build();
    }

    private static Person preparePerson(Long id, Long externalId, String name, Integer height, Integer mass){
        return Person.builder()
                .id(id)
                .height(height)
                .externalId(externalId)
                .mass(mass)
                .name(name)
                .build();
    }

}
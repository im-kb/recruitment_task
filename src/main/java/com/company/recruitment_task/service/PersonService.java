package com.company.recruitment_task.service;

import com.company.recruitment_task.mapper.PersonMapper;
import com.company.recruitment_task.model.person.Person;
import com.company.recruitment_task.model.person.dto.PersonDto;
import com.company.recruitment_task.repository.IPersonRepository;
import com.company.recruitment_task.utils.CustomJsonParser;
import com.company.recruitment_task.utils.PersonHeightValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Service
public class PersonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    private final PersonRestService personRestService;
    private final PersonMapper personMapper;
    private final IPersonRepository personRepository;
    private final PersonHeightValidator personHeightValidator;
    private final CustomJsonParser customJsonParser;

    public PersonService(PersonRestService personRestService, PersonMapper personMapper, IPersonRepository personRepository, PersonHeightValidator personHeightValidator, CustomJsonParser customJsonParser) {
        this.personRestService = personRestService;
        this.personMapper = personMapper;
        this.personRepository = personRepository;
        this.personHeightValidator = personHeightValidator;
        this.customJsonParser = customJsonParser;
    }

    public PersonDto importPerson(Long externalPersonId) {
        if (externalPersonId == null) {
            return null;
        }
        var personDto = getPersonFromExternalApi(externalPersonId);
        if (personDto == null) {
            LOGGER.warn("Error while fetching person from external api");
            return null;
        }

        if (!personHeightValidator.isPersonHeightInThreshold(personDto.getHeight())) {
            LOGGER.warn("Person with externalId: {} and height: {} not stored, because it doesnt meet the height threshold", personDto.getExternalId(), personDto.getHeight());
            return null;
        }

        var person = personRepository.getPersonByExternalId(externalPersonId);
        if (person != null) {
            LOGGER.info("Person with externalID: {} already exists in the database", personDto.getExternalId());
            return personMapper.mapToDto(mergeExistingPerson(person, personDto));
        } else {
            return personMapper.mapToDto(createPerson(personDto));
        }
    }

    private Person createPerson(PersonDto personDto) {
        var personEntity = personMapper.mapFromDto(personDto);

        LOGGER.info("Adding person with externalID: {} to the database", personDto.getExternalId());
        return personRepository.save(personEntity);
    }

    private Person mergeExistingPerson(Person slavePerson, PersonDto masterPerson) {
        if (slavePerson == null || masterPerson == null) {
            return null;
        }
        slavePerson.setName(masterPerson.getName());
        slavePerson.setMass(masterPerson.getMass());
        slavePerson.setHeight(masterPerson.getHeight());

        LOGGER.info("Updating person with id: {} and externalID: {} to the database", slavePerson.getId(), slavePerson.getExternalId());
        return personRepository.save(slavePerson);
    }

    public PersonDto getPersonFromDatabase(Long personId) {
        if (personId == null) {
            return null;
        }

        try {
            return personMapper.mapToDto(personRepository.getReferenceById(personId));
        } catch (EntityNotFoundException enfe) {
            LOGGER.info("Person with id: {} not found", personId);
        }
        return null;
    }

    public List<PersonDto> searchPersonsByNameKeyword(String nameKeyword) {
        if (nameKeyword == null) {
            return null;
        }
        var persons = personRepository.getPersonsByNameIgnoreCaseContaining(nameKeyword);
        if (isEmpty(persons)) {
            return null;
        }

        return personMapper.mapToDtoList(persons);
    }

    private PersonDto getPersonFromExternalApi(Long externalPersonId) {
        String jsonResponse = personRestService.getPersonFromExternalApi(externalPersonId);
        var personDto = customJsonParser.parsePersonJsonToDto(jsonResponse);
        if (personDto != null) {
            personDto.setExternalId(externalPersonId);
        }

        return personDto;
    }

}
package com.company.recruitment_task.controller;

import com.company.recruitment_task.model.person.dto.PersonDto;
import com.company.recruitment_task.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@RestController
@RequestMapping("people")
@Tag(name = "Person controller")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @Operation(summary = "Fetch a person from external service and store it in database")
    @GetMapping(value="/external-import/{personId}")
    public ResponseEntity<PersonDto> importPersonById(@NonNull @PathVariable("personId") Long personId) {
        var result = personService.importPerson(personId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.internalServerError().build();
    }

    @Operation(summary = "Get a person by id from internal database")
    @GetMapping(value="/{personId}")
    public ResponseEntity<PersonDto> getPersonById(@NonNull @PathVariable("personId") Long personId) {
        var result = personService.getPersonFromDatabase(personId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get a list of persons by name keyword")
    @GetMapping(value = "/search")
    public ResponseEntity<List<PersonDto>> getPersonsByNameKeyword(@NonNull @RequestParam("nameKeyword") String nameKeyword) {
        var result = personService.searchPersonsByNameKeyword(nameKeyword);
        return !isEmpty(result) ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

}
package com.company.recruitment_task.repository;

import com.company.recruitment_task.model.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPersonRepository extends JpaRepository<Person, Long> {

    Person getPersonByExternalId(Long externalId);

    List<Person> getPersonsByNameIgnoreCaseContaining(String nameKeyword);

}
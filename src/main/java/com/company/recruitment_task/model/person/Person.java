package com.company.recruitment_task.model.person;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(name = "external_id", unique = true)
    private Long externalId;

    @Column
    private String name;

    @Column
    private Integer height;

    @Column
    private Integer mass;

}
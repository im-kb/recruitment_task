package com.company.recruitment_task.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PersonHeightValidator {

    private Integer minimumHeight;

    public PersonHeightValidator(@Value("${utils.personHeightValidator.minHeight:180}") Integer minimumHeight) {
        this.minimumHeight = minimumHeight;
    }

    public boolean isPersonHeightInThreshold(Integer height) {
        return height != null && height >= minimumHeight;
    }

}
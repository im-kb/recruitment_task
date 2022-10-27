package com.company.recruitment_task.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonHeightValidatorTest {

    private final Integer TEST_PERSON_HEIGHT = 180;
    private final PersonHeightValidator personHeightValidator = new PersonHeightValidator(TEST_PERSON_HEIGHT);

    @ParameterizedTest
    @MethodSource("shouldCheckHeightProperly")
    public void shouldCheckHeightProperly(Integer height, boolean expectedResult) {
        var result = personHeightValidator.isPersonHeightInThreshold(height);
        assertEquals(result, expectedResult);
    }

    private static Stream<Arguments> shouldCheckHeightProperly() {
        return Stream.of(
                Arguments.of(0, false),
                Arguments.of(null, false),
                Arguments.of(100, false),
                Arguments.of(179, false),
                Arguments.of(180, true),
                Arguments.of(181, true)
        );
    }

}
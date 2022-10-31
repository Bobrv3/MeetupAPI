package com.bobrov.meetup.service.validator;

import com.bobrov.meetup.controller.MeetupController;
import com.bobrov.meetup.dao.util.FilterOperation;
import com.bobrov.meetup.service.exception.FilterValidationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class FilterValidator {
    private static final String OPERATION_VALUE_SEPARATOR = ";";
    private static final List<String> filterOperations
            = Collections.unmodifiableList(Arrays.stream(FilterOperation.values())
            .map(filterCondition -> filterCondition.name())
            .collect(Collectors.toList()));


    public static void validate(Map<String, String> paramsForFilter, Class<?> validatedClass) {
        if (paramsForFilter.containsKey(MeetupController.SORT_ORDER)) {
            paramsForFilter.remove(MeetupController.SORT_ORDER);
        }
        if (paramsForFilter.containsKey(MeetupController.SORT_BY)) {
            paramsForFilter.remove(MeetupController.SORT_BY);
        }

        validateParams(paramsForFilter, validatedClass);
        validateOperations(paramsForFilter);
    }

    private static void validateOperations(Map<String, String> paramsForFilter) {
        for (Map.Entry<String, String> entry : paramsForFilter.entrySet()) {
            String operation = entry.getValue().split(OPERATION_VALUE_SEPARATOR)[0].toUpperCase();
            if (!filterOperations.contains(operation)) {
                throw new FilterValidationException(
                        String.format(String.format("Unknown operation '%s' for filtering", operation)));
            }
        }
    }

    private static void validateParams(Map<String, String> paramsForFilter, Class<?> validatedClass) {
        List<String> fieldsName = Arrays.stream(validatedClass.getDeclaredFields())
                .map(field -> field.getName())
                .collect(Collectors.toList());

        paramsForFilter.keySet().stream()
                .forEach(paramName -> {
                    if (!fieldsName.contains(paramName)) {
                        throw new FilterValidationException(String.format("Unknown field '%s'", paramName));
                    }
                });
    }
}

package com.bobrov.meetup.service.validator;

import com.bobrov.meetup.service.exception.SortValidationException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class SortValidator {
    private static final String ASC_ORDER = "asc";
    private static final String DESC_ORDER = "asc";

    public static void validate(List<String> paramsForSort, String sortOrder, Class<?> validatedClass) {
        validateOrder(sortOrder);
        validateParams(paramsForSort, validatedClass);
    }

    private static void validateParams(List<String> paramsForSort, Class<?> validatedClass) {
        List<String> fieldsName = Arrays.stream(validatedClass.getDeclaredFields())
                .map(field -> field.getName())
                .collect(Collectors.toList());

        paramsForSort.stream()
                .forEach(paramName -> {
                    if (!fieldsName.contains(paramName)) {
                        throw new SortValidationException((String.format("Unknown field '%s'", paramName)));
                    }
                });
    }

    private static void validateOrder(String sortOrder) {
        if (!sortOrder.equalsIgnoreCase(ASC_ORDER)
                && !sortOrder.equalsIgnoreCase(DESC_ORDER)
        ) {
          throw new SortValidationException(
                  String.format("Unknown order direction '%s'", sortOrder));
        }
    }
}

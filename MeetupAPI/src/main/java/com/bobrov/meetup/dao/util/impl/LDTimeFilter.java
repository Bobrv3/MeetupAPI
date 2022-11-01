package com.bobrov.meetup.dao.util.impl;

import com.bobrov.meetup.dao.util.Filter;
import com.bobrov.meetup.dao.util.FilterOperation;
import com.bobrov.meetup.model.Meetup;
import com.bobrov.meetup.service.exception.FilterValidationException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class LDTimeFilter implements Filter {
    @Override
    public Predicate getPredicate(Map.Entry<String, String> entry, Root<Meetup> root, CriteriaBuilder cb) {
        String field = entry.getKey();
        String value = entry.getValue().split(";")[1];
        FilterOperation operation = FilterOperation.valueOf(
                entry.getValue().split(";")[0].toUpperCase());

        try {
            LocalDateTime eventDate = LocalDateTime.parse(value);

            switch (operation) {
                case EQ:
                    return cb.equal(root.get(field), eventDate);
                case NE:
                    return cb.notEqual(root.get(field), eventDate);
                case LT:
                    return cb.lessThan(root.get(field), eventDate);
                case LE:
                    return cb.lessThanOrEqualTo(root.get(field), eventDate);
                case GE:
                    return cb.greaterThanOrEqualTo(root.get(field), eventDate);
                case GT:
                    return cb.greaterThan(root.get(field), eventDate);
                default:
                    throw new IllegalArgumentException(
                            String.format("unsupported operation '%s' with the value '%s'", operation.name(), value));
            }
        } catch (DateTimeParseException ex) {
            throw new FilterValidationException(
                    String.format("not correct value for filtering '%s'", value)
            );
        }
    }
}

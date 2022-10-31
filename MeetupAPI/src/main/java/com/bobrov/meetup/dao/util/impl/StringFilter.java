package com.bobrov.meetup.dao.util.impl;

import com.bobrov.meetup.dao.util.Filter;
import com.bobrov.meetup.dao.util.FilterOperation;
import com.bobrov.meetup.model.Meetup;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Map;

public class StringFilter implements Filter {
    @Override
    public Predicate getPredicate(Map.Entry<String, String> entry, Root<Meetup> root, CriteriaBuilder cb) {
        String field = entry.getKey();
        String value = entry.getValue().split(";")[1];
        FilterOperation operation = FilterOperation.valueOf(
                entry.getValue().split(";")[0].toUpperCase()
        );

        switch (operation) {
            case EQ:
                return cb.equal(root.get(field), value);
            case NE:
                return cb.notEqual(root.get(field),value);
            case LIKE:
                return cb.like(root.get(field), value);
            default:
                throw new IllegalArgumentException(
                        String.format("unsupported operation '%s' with the value '%s'", operation.name(), value));
        }
    }
}

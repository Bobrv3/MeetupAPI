package com.bobrov.meetup.dao.util;

import com.bobrov.meetup.model.Meetup;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Map;

public interface Filter {
    Predicate getPredicate(Map.Entry<String, String> entry, Root<Meetup> root, CriteriaBuilder cb);
}

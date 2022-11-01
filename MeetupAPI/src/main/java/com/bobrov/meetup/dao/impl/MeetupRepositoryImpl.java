package com.bobrov.meetup.dao.impl;

import com.bobrov.meetup.dao.MeetupRepository;
import com.bobrov.meetup.dao.util.FilterProvider;
import com.bobrov.meetup.model.Meetup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MeetupRepositoryImpl implements MeetupRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private final FilterProvider filterProvider;
    private static final String ASC_ORDER = "ASC";

    @Override
    public Optional<Meetup> findById(Long id) {
        return Optional.ofNullable(
                entityManager.find(Meetup.class, id)
        );
    }

    @Override
    public List<Meetup> findAll(Map<String, String> paramsForFilter, List<String> paramsForSort, String sortOrder) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Meetup> query = criteriaBuilder.createQuery(Meetup.class);
        Root<Meetup> root = query.from(Meetup.class);

        List<Order> ordersList = getOrderList(paramsForSort, sortOrder, criteriaBuilder, root);
        List<Predicate> predicates = getPredicateList(paramsForFilter, criteriaBuilder, root);

        query.select(root)
                .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])))
                .orderBy(ordersList);

        return entityManager.createQuery(query).getResultList();
    }

    private List<Predicate> getPredicateList(Map<String, String> paramsForFilter, CriteriaBuilder criteriaBuilder, Root<Meetup> root) {
        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, String> entry : paramsForFilter.entrySet()) {
            predicates.add(filterProvider
                    .getFilter(entry.getKey())
                    .getPredicate(entry, root, criteriaBuilder));
        }
        return predicates;
    }

    private List<Order> getOrderList(List<String> paramsForSort, String sortOrder, CriteriaBuilder criteriaBuilder, Root<Meetup> root) {
        List<Order> ordersList = new ArrayList<>();
        if (sortOrder.equalsIgnoreCase(ASC_ORDER)) {
            for (String param : paramsForSort) {
                ordersList.add(criteriaBuilder.asc(root.get(param)));
            }
        } else {
            for (String param : paramsForSort) {
                ordersList.add(criteriaBuilder.desc(root.get(param)));
            }
        }
        return ordersList;
    }

    @Override
    public Meetup saveOrUpdate(Meetup meetup) {
        return entityManager.merge(meetup);
    }

    @Override
    public void delete(Meetup meetup) {
        entityManager.remove(meetup);
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}

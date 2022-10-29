package com.bobrov.meetup.dao.impl;

import com.bobrov.meetup.dao.MeetupRepository;
import com.bobrov.meetup.model.Meetup;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MeetupRepositoryImpl implements MeetupRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Meetup> findById(Long id) {
        final Meetup meetup = entityManager.find(Meetup.class, id);

        return Optional.ofNullable(
                entityManager.find(Meetup.class, id)
        );
    }

    @Override
    public List<Meetup> findAll() {
        Session session = entityManager.unwrap(Session.class);
        Query<Meetup> query = session.createQuery("FROM Meetup", Meetup.class);

        return query.getResultList();
    }

    @Override
    public Meetup save(Meetup meetup) {
        entityManager.persist(meetup);
        return meetup;
    }

    @Override
    public Meetup update(Meetup meetup) {
        return entityManager.merge(meetup);
    }

    @Override
    public void delete(Meetup meetup) {
        entityManager.remove(meetup);
    }
}

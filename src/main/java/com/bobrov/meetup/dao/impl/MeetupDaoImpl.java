package com.bobrov.meetup.dao.impl;

import com.bobrov.meetup.dao.MeetupDAO;
import com.bobrov.meetup.exception.NoSuchMeetingException;
import com.bobrov.meetup.model.Meetup;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MeetupDaoImpl implements MeetupDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Meetup> findById(Long id) {
        return Optional.of(
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
    public void deleteById(Long id) {
        Optional<Meetup> meetup = findById(id);

        entityManager.remove(meetup
                .orElseThrow(NoSuchMeetingException::new)
        );
    }
}

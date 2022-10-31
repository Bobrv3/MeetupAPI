package com.bobrov.meetup.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "meetups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meetup {
    private static final String SEQ_NAME = "meetup_seq";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "organizer", nullable = false)
    private String organizer;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "place", nullable = false)
    private String place;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Meetup meetup = (Meetup) o;
        return id != null && Objects.equals(id, meetup.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

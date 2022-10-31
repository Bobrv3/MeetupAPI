INSERT INTO meetups (id, topic, description, organizer, event_date, place)
VALUES (1, 'Joker', 'International conference for experienced Java developers', 'JUGru group',
        '2022-11-19 09:30', 'Moscow'),
       (2, 'Mobius 2022 Autumn', 'Conference for mobile developers', 'JUGru group',
        '2022-11-21 10:00', 'Moscow'),
       (3, 'DUMP Kazan 2022', 'Tatarstan''s central IT conference', 'IT People Connection',
        '2022-11-11 11:00', 'Kazan'),
       (4, 'Joker', 'International conference for experienced Java developers', 'Skolkovo Innovation Center',
        '2023-11-25 10:00', 'Moscow');

ALTER SEQUENCE meetup_seq RESTART WITH 5;
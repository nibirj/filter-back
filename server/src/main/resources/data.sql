INSERT INTO filter (name) VALUES ('Test1'), ('Test2');

INSERT INTO amount_criteria (comparing_condition, comparable_value, filter_id) VALUES
       ('More', 4, 1),
       ('Less', 3, 1),
       ('Less', 32, 2),
       ('Equal', 12, 2);

INSERT INTO date_criteria (comparing_condition, comparable_date, filter_id) VALUES
       ('From', '2024-12-03 00:00:00', 1),
       ('Exactly', '2020-09-24 00:00:00', 2);


INSERT INTO title_criteria (comparing_condition, comparable_name, filter_id) VALUES
        ('Equal', 'Hello world!', 1),
        ('Contains', 'Hello world 2', 2);


-- liquibase formatted sql

-- changeSet kostusonline:e49d6346-9ce6-56c6-b699-7a833edfa7e8 runOnChange:true
INSERT INTO
    'user' (
        'name',
        'date_of_birth',
        'password'
    )
VALUES (
        'Anton',
        '1980-12-30',
        '12345678'
    ),
    (
        'Ivan',
        '1990-03-15',
        '12345678'
    ),
    (
        'Olga',
        '1985-06-01',
        '12345678'
    ),
    (
        'Sergey',
        '1970-01-01',
        '12345678'
    );
create table account
(
    user_id uuid unique,
    balance int
);

create table points
(
    user_id uuid unique,
    points  int
);
create table if not exists users
(
    id      bigint primary key auto_increment,
    email   text not null,
    api_key text not null
);

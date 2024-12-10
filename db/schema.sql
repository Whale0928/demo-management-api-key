CREATE SEQUENCE clients_id_seq;

create table clients
(
    id          integer      not null default nextval('clients_id_seq'),
    name        varchar(255) not null,        -- 키 이름
    email       varchar(255) not null unique, -- 이메일
    api_key     varchar(512) not null unique, -- api key
    issuer_info varchar(255) not null,        --  발급자 정보
    permissions text array,
    allowed_ips text array,
    issued_at   timestamp,
    expires_at  timestamp
);

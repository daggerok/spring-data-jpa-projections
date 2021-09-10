drop table if exists person CASCADE
;

drop sequence if exists person_sequence
;

drop table if exists address CASCADE
;

drop sequence if exists address_sequence
;

--

create sequence address_sequence start with 111 increment by 1
;

create table address
(
    id         bigint       not null,
    country    varchar(255) not null,
    city       varchar(255) not null,
    street     varchar(255) not null,
    created_at timestamp    not null,
    updated_at timestamp    not null,
    primary key (id)
)
;

--

create sequence person_sequence start with 222 increment by 1
;

create table person
(
    id         bigint       not null,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    created_at timestamp    not null,
    updated_at timestamp    not null,
    address_id bigint,
    primary key (id)
)
;

alter table person
    add constraint person_address_id_fk foreign key (address_id) references address

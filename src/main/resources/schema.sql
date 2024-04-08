create sequence comments_id_seq;
create sequence bookings_id_seq;
create sequence items_id_seq;
create sequence users_id_seq;
create sequence requests_id_seq;

create table if not exists USERS
(
    id    bigint       not null default nextval('users_id_seq') primary key,
    name  varchar(255) not null,
    email varchar(255) not null,
    unique(email)
);

create table if not exists REQUESTS
(
    id           bigint                      not null default nextval('requests_id_seq') primary key,
    description  varchar(255)                not null,
    requestor_id int references USERS(id),
    created      timestamp without time zone not null
);

create table if not exists ITEMS
(
    id           bigint       not null default nextval('items_id_seq') primary key,
    name         varchar(255) not null,
    description  varchar(255) not null,
    is_available bool                  default false,
    request_id int references REQUESTS(id),
    owner_id     int references USERS(id)
);

create table if not exists BOOKINGS
(
    id          bigint       not null default nextval('bookings_id_seq') primary key,
    start_date  timestamp without time zone,
    end_time    timestamp without time zone,
    item_id     int references ITEMS(id),
    booker_id   int references USERS(id),
    status      varchar(10)  not null
);

create table if not exists COMMENTS
(
    id    bigint       not null default nextval('comments_id_seq') primary key,
    text varchar(255)  not null,
    item_id int references ITEMS(id),
    author_id int references USERS(id),
    created timestamp without time zone not null
);


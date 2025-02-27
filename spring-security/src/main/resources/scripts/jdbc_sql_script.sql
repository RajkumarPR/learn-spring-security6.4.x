create table users
(username varchar(50) not null primary key,
password varchar(500) not null,
enabled boolean not null);

create table authorities
(username varchar(50) not null,
authority varchar(50) not null,
constraint fk_authorities_users foreign key(username) references users(username));

create unique index ix_auth_username on authorities (username,authority);

-- Insert user details
insert into users values('user', '{noop}mypass@01', '1');
insert into authorities values('user','USER');

-- insert admin details
insert into users values('admin', '{bcrypt}$2y$12$YnPVVUzj9ZE97xxHANVHRO3KPudi7RVjXXQgh3ttSGUa7woqquSsO', '1');
insert into authorities values('admin','USER');
insert into authorities values('admin','ADMIN');

-- insert manager details
insert into users values('manager', '{SHA-256}0f439c8f8b72f3b19479597bf175b6219a3c2181011cd02e250e0f3fba09d5fc', '1');
insert into authorities values('manager','USER');
insert into authorities values('manager','MANAGER');

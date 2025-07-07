create database if not exists web_shop;
use web_shop;

drop table if exists users, roles, products, orders, order_items;

create table users (
    username varchar(255) primary key,
    password varchar(255)
);

create table roles (
    username varchar(255) not null,
    role varchar(250) not null,
    primary key (username, role),
    foreign key (username) references users(username) on delete cascade
);

create table products (
    id int primary key auto_increment,
    name varchar(255),
    price decimal(10, 2)
);

create table orders (
    id int primary key auto_increment,
    username varchar(255),
    foreign key (username) references users(username) on delete cascade
);

create table order_items (
    id int primary key auto_increment,
    order_id int,
    product_id int,
    quantity int,
    foreign key (order_id) references orders(id) on delete cascade,
    foreign key (product_id) references products(id) on delete cascade
);

insert into users (username, password) values ('admin', '$2a$10$tBTfzHzjmQVKza3VSa5lsOX6/iL93xPVLlLXYg2FhT6a.jb1o6VDq');
insert into roles (username, role) values ('admin', 'ADMIN');

insert into products (name, price) values ('Apple', 0.99);
insert into products (name, price) values ('Banana', 0.59);
insert into products (name, price) values ('Cherry', 1.99);
insert into products (name, price) values ('Date', 2.99);
insert into products (name, price) values ('Elderberry', 3.99);

insert into orders (username) values ('admin');
insert into orders (username) values ('admin');
insert into orders (username) values ('admin');
insert into orders (username) values ('admin');
insert into orders (username) values ('admin');

create table user (
	id serial not null constraint user_pkey primary key,
	name varchar(4000),
	role_id integer not null ,
	active boolean default true not null
);

create table role (
    id serial not null constraint role_pkey primary key,
    name varchar(4000),
    active boolean default true not null
);
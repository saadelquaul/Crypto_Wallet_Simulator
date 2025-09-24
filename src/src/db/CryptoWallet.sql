create table if not exists wallets (
id SERIAL primary key,
adress varchar(100) not null unique,
balanace double precision not null,
type varchar(50) not null
);

create table if not exists transactions (
id SERIAL primary key,
source_address varchar(100) not null,
destination_address varchar(100) not null,
amount double precision not null,
creation_date TIMESTAMP not null,
fees double precision not null,
fee_level varchar(50) not null,
status varchar(50) not null,
crypto_currency_type varchar(50) not null
);


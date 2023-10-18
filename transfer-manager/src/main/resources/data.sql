------------------------------------------------------------------------------------------------
--Рабочие таблицы

--Схема с рабочими таблицами
create schema if not exists TEST;

--Таблица с аналитикой
create table if not exists TEST.DUMP(
    id serial,
    archive_name varchar(100),
    archive_size bigint,
    file_name varchar(100),
    file_size bigint,
    entity_name varchar(100),
    row_count integer,
    started timestamp,
    finished timestamp,
    error_time timestamp,
    error_description varchar(4096),
    delivery_guid uuid,
    file_guid uuid,
    primary key (id)
);

--Таблица со свойствами для взаимодействия с файлами (маска, форматы дат, наборы столбцов и тп)
create table if not exists TEST.ENTITY(
    id serial,
    entity_name varchar(100),
    mask varchar(100),
    status char,
    valid_date_from date,
    valid_date_before date,
    inner_date_format varchar(100),
    file_date_format varchar(100),
    columns varchar(1000),
    separator varchar(255),
    description varchar(255),
    db_connection_key varchar(1000),
    primary key (id)
);

--Таблица с общими настройками системы
create table if not exists TEST.COMMON_SETTINGS (
    id serial,
    key varchar(100),
    value varchar(1000),
    primary key (id)
);

------------------------------------------------------------------------------------------------
--Таблицы для авторизации

--Схема с таблицами авторизации
create schema if not exists AUTH_INFO;

--Таблица с данными о пользователях системы
create table if not exists AUTH_INFO.USERS (
    user_pk serial,
    user_username varchar(255),
    user_password_hash varchar(255),
    primary key (user_pk)
);

------------------------------------------------------------------------------------------------
--Наполнение БД рабочими значениями

--Создание администратора системы
--Логин = admin
--Пароль = pwd
insert into AUTH_INFO.USERS (user_username, user_password_hash)
values ('admin', '$2a$16$YB9vanYp1bAbE2nPr5Gg2ecTsLqaLbxuuLIre17rgNEzmWRfKlJ1O');

--Наполнение таблицы с общими настройками системы рабочими значениями
--TODO не забыть ввести корректные рабочие значения при запуске на пром
insert into test.common_settings (key, value) values ('SUCCESS_DIRECTORY', '../Folder/successDirectory');
insert into test.common_settings (key, value) values ('ERROR_DIRECTORY', '../Folder/errorDirectory');
insert into test.common_settings (key, value) values ('PROCESS_DIRECTORY', '../Folder/processDirectory');
insert into test.common_settings (key, value) values ('LOADING_HERE', '../Folder/LoadingHere');
insert into test.common_settings (key, value) values ('CHECK_PERIOD', '300');
insert into test.common_settings (key, value) values ('CHECK_DURATION', '100');
insert into test.common_settings (key, value) values ('STORAGE_LIFE', '10');
insert into test.common_settings (key, value) values ('PAUSE', '500');
insert into test.common_settings (key, value) values ('SESSION_TIME', '0');
insert into test.common_settings (key, value) values ('TIME_TO_WAIT_CONNECTION', '5');
insert into test.common_settings (key, value) values ('NUMBER_OF_SESSION', '1');
insert into test.common_settings (key, value) values ('TIME_FOR_DELIVERY', '20');
insert into test.common_settings (key, value) values ('NUMBER_OF_ROW_FOR_SINGLE_READ_WRITE', '1000');
insert into test.common_settings (key, value) values ('PERIOD_FOR_CLEANING', '12');

------------------------------------------------------------------------------------------------
--Таблицы для тестов TODO: не забыть убрать в будущем всё, что ниже

--Скрипт для создания тествой таблицы маски sbszh_ois
create table if not exists SBSZH_OIS (
    cust_id numeric,
    epk_id varchar(100),
    step varchar(100),
    crm_segment varchar(100),
    load_dt date not null,
    object_guid varchar(100),
    iter_guid varchar(100),
	process_dt date
);

--Скрипт для создания тествой таблицы маски sbszh (для тестов создаётся в postgres)
--create table if not exists SBSZH(
--    cust_id numeric,
--    step varchar(100),
--    load_dt date not null,
--    dk_final varchar(100),
--    object_guid varchar(100),
--    iter_guid varchar(100),
--	process_dt date
--);

--Скрипт для создания тествой таблицы маски spr_tb
create table if not exists SPR_TB(
    nTBOld numeric,
    nOSBOld numeric,
    nVSPOld numeric,
    nTB numeric,
    nOSB numeric,
    nVSP numeric,
    sTBName varchar(10000),
    sOSBName varchar(10000),
    nGOSB numeric,
    sGOSBName varchar(10000),
    sVSPName varchar(10000),
    nVSPType numeric,
    nVSPBad numeric,
    nRegionCode numeric,
    sRegionName varchar(10000),
    sCityType varchar(10000),
    sCity varchar(10000),
    sVSPAddress varchar(10000),
    sChief varchar(10000),
    sPhone varchar(10000),
    nOfficeBType numeric,
    sOfficeType varchar(10000),
    sVip varchar(10000),
    dtReformat date,
    nCluster numeric,
    dtOpen date,
    rVSPSquare numeric,
    rVSPOperationSquare numeric,
    rEmployee numeric,
    rBusinessWindows numeric,
    rLatitude numeric,
    rLongitude numeric,
    bIsCIK varchar(10000),
    sDTAKey varchar(10000),
    sVSPAddressShort varchar(10000),
    sVSPAddressLev1 varchar(10000),
    sVSPAddressLev2 varchar(10000),
    sVSPAddressLev3 varchar(10000),
    nOrderTBid numeric,
    nOrderGOSBid numeric,
    nOrderOSBid numeric,
    nOrderVSPid numeric,
    nOrderVSPAddrL1id numeric,
    nOrderVSPAddrL2id numeric,
    nOrderVSPAddrL3id numeric,
    sTBKey varchar(10000),
    sOSBKey varchar(10000),
    sVSPKey varchar(10000),
    nTechTBid numeric,
    nTechGOSBid numeric,
    nTechOSBId numeric,
    nTechVSPId numeric,
    nTBOrig numeric,
    sTBNameOrig varchar(10000),
    object_guid varchar(100),
    iter_guid varchar(100),
	process_dt date
);

------------------------------------------------------------------------------------------------
--Наполнение БД тестовыми значениями

--Наполнение таблицы с общими настройками url-ами для связи с БД
insert into test.common_settings (key, value) values ('core_url', 'jdbc:postgresql://localhost:5432/sbszh?user=postgres&password=123');
insert into test.common_settings (key, value) values ('ois_url', 'jdbc:h2:mem:testdb;user=sa');

--Наполнение тестовых сущностей
insert into test.entity (entity_name, mask, status, valid_date_from, valid_date_before, inner_date_format, file_date_format, columns, separator, description, db_connection_key) values
    ('sbszh', 'sbszh_results_202012310930.csv', 'y', '1990-12-12', '2022-12-12', 'yyyy-MM-dd', 'yyyyMMddHHmm', '#cust_id/dk_final/step/load_dt', 'TAB', 'id клиентов сбербанка для системы CORE', 'core_url');
insert into test.entity (entity_name, mask, status, valid_date_from, valid_date_before, inner_date_format, file_date_format, columns, separator, description, db_connection_key) values
    ('sbszh_ois', 'sbszh_ois_results_202012310930.csv', 'y', '1990-12-12', '2022-12-12', 'yyyy-MM-dd', 'yyyyMMddHHmm', '#cust_id/epk_id/step/crm_segment/load_dt', 'COMMA', 'id клиентов сбербанка для внутренней системы OIS', 'ois_url');
insert into test.entity (entity_name, mask, status, valid_date_from, valid_date_before, inner_date_format, file_date_format, columns, separator, description, db_connection_key) values
    ('spr_tb', 'spr_tb_results_202012310930.csv', 'y', '1990-12-12', '2022-12-12', 'yyyy/MM/dd HH:mm:ss', 'yyyyMMddHHmm',
    '#nTBOld/nOSBOld/nVSPOld/nTB/nOSB/nVSP/sTBName/sOSBName/sVSPName/nGOSB/sGOSBName/nVSPType/nVSPBad/nRegionCode/sRegionName/sCityType/sCity/sVSPAddress/sChief/sPhone/nOfficeBType/sOfficeType/sVIP/dtReformat/nCluster/dtOpen/rVSPSquare/rVSPOperationSquare/rEmployee/rBusinessWindows/rLatitude/rLongitude/bIsCIK/sDTAKey/sVSPAddressShort/sVSPAddressLev1/sVSPAddressLev2/sVSPAddressLev3/nOrderTBId/nOrderGOSBId/nOrderOSBId/nOrderVSPId/nOrderVSPAddrL1Id/nOrderVSPAddrL2Id/nOrderVSPAddrL3Id/sTBKey/sOSBKey/sVSPKey/nTechTBId/nTechGOSBId/nTechOSBId/nTechVSPId/nTBOrig/sTBNameOrig',
    'TAB', 'Полный список территориальных банков', 'ois_url');
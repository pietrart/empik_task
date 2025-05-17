-- V1__init.sql

insert into t_coupon (c_coupon_id, c_code, c_created_at, c_max_usage, c_actual_usage, c_country, c_version)
values ('4fd93b5f-0b6f-48d6-ae42-58c9dad06f7e', 'WIOSNA-20%', '2025-05-17', 5, 0, 'PL', 0),
       ('65d77032-b1d9-4ba5-a434-8947e82bb12d', 'WIOSNA-10%', '2025-05-16', 1, 0, 'PL', 0),
       ('646c2fdb-e513-453e-9c07-49412c20cb25', 'WIOSNA-15%', '2025-05-16', 1, 0, 'PL', 0),
       ('5d634662-e548-48bc-9ac5-8ae67f168398', 'ZIMA-50%', '2025-05-17', 5, 5, 'CA', 0),
       ('417a4eb4-60ca-4458-9aa1-0bdb32b1c53b', 'LATO-10%', '2025-05-16', 1, 4, 'CA', 0)
;
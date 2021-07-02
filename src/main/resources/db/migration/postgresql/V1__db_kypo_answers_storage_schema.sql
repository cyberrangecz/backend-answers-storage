create table sandbox_answer (
   sandbox_answer_id  bigserial not null,
    answer_content varchar(255) not null,
    answer_identifier varchar(255) not null,
    sandbox_info_id int8,
    primary key (sandbox_answer_id)
);

create table sandbox_info (
   sandbox_info_id  bigserial not null,
    sandbox_ref_id int8 not null,
    primary key (sandbox_info_id)
);

alter table sandbox_info
   add constraint UK_7vwh6n5gwia9qm0446a9n2ffs unique (sandbox_ref_id);

alter table sandbox_answer
   add constraint FKew4md7g9pq3tah3yhodmdlytw
   foreign key (sandbox_info_id)
   references sandbox_info;

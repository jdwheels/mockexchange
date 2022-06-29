create table mock_badge
(
    id         bigint    not null
        primary key,
    class_type integer   not null,
    date       timestamp not null,
    name       varchar(255),
    tag_based  boolean   not null,
    user_id    bigint
);

create table mock_comment
(
    id                bigint       not null
        primary key,
    content_license   varchar(127) not null,
    creation_date     timestamp    not null,
    post_id           bigint       not null,
    score             bigint       not null,
    text              text         not null,
    user_display_name varchar(127),
    user_id           bigint
);

create table mock_post
(
    id                       bigint       not null
        primary key,
    accepted_answer_id       bigint,
    tags                     varchar(127),
    answer_count             bigint,
    body                     text         not null,
    closed_date              timestamp,
    comment_count            bigint,
    community_owned_date     timestamp,
    content_license          varchar(127) not null,
    creation_date            timestamp    not null,
    favorite_count           bigint,
    last_activity_date       timestamp    not null,
    last_edit_date           timestamp,
    last_editor_display_name varchar(127),
    last_editor_user_id      bigint,
    owner_display_name       varchar(127),
    owner_user_id            bigint,
    parent_id                bigint,
    post_type_id             integer ,
    score                    bigint       not null,
    title                    varchar(255),
    view_count               bigint
);

create table mock_post_history
(
    id                   bigint    not null
        primary key,
    content_license      varchar(127),
    creation_date        timestamp not null,
    post_history_type_id integer   not null,
    post_id              bigint    not null,
    revision_guid        uuid      not null,
    text                 text,
    user_display_name    varchar(127),
    user_id              bigint
);

create table mock_post_link
(
    id              bigint    not null
        primary key,
    creation_date   timestamp not null,
    link_type_id    integer   not null,
    post_id         bigint    not null,
    related_post_id bigint    not null
);

create table mock_tag
(
    id              bigint       not null
        primary key,
    count           bigint       not null,
    excerpt_post_id bigint,
    tag_name        varchar(255) not null,
    wiki_post_id    bigint
);

create table mock_user
(
    id                bigint           not null
        primary key,
    about_me          varchar(8191),
    account_id        bigint,
    creation_date     timestamp        not null,
    display_name      varchar(63)      not null,
    down_votes        bigint default 0 not null,
    last_access_date  timestamp        not null,
    location          varchar(127),
    profile_image_url varchar(255),
    reputation        bigint default 0 not null,
    up_votes          bigint default 0 not null,
    views             bigint default 0 not null,
    website_url       varchar(255)
);

create table mock_vote
(
    id            bigint    not null
        primary key,
    creation_date timestamp not null,
    post_id       bigint    not null,
    user_id       bigint,
    vote_type_id  integer   not null
);

create index mock_vote_post_id_index
    on mock_vote (post_id);

create sequence mock_badge_id_seq;

create sequence mock_comment_id_seq;

create sequence mock_post_id_seq;

create sequence mock_post_history_id_seq;

create sequence mock_post_link_id_seq;

create sequence mock_tag_id_seq;

create sequence mock_user_id_seq;

create sequence mock_vote_id_seq;

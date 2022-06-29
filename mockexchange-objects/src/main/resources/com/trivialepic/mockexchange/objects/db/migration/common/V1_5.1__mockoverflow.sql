select setval('mock_badge_id_seq', 1000000000);

select setval('mock_comment_id_seq', 1000000000);

select setval('mock_post_id_seq', 1000000000);

select setval('mock_post_history_id_seq', 1000000000);

select setval('mock_post_link_id_seq', 1000000000);

select setval('mock_tag_id_seq', 1000000000);

select setval('mock_user_id_seq', 1000000000);

select setval('mock_vote_id_seq', 1000000000);

alter table mock_post
    add column data_source varchar(50);
alter table mock_user
    add column data_source varchar(50);
alter table mock_tag
    add column data_source varchar(50);
alter table mock_comment
    add column data_source varchar(50);
alter table mock_post_history
    add column data_source varchar(50);
alter table mock_post_link
    add column data_source varchar(50);
alter table mock_vote
    add column data_source varchar(50);
alter table mock_badge
    add column data_source varchar(50);

create sequence mock_user_account_id_seq;

select setval('mock_user_account_id_seq', 1000000000);

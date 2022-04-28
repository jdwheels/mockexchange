
select setval('mock_badge_id_seq', (select max(id) from mock_badge));

select setval('mock_comment_id_seq', (select max(id) from mock_comment));

select setval('mock_post_id_seq', (select max(id) from mock_post));

select setval('mock_post_history_id_seq', (select max(id) from mock_post_history));

select setval('mock_post_link_id_seq', (select max(id) from mock_post_link));

select setval('mock_tag_id_seq', (select max(id) from mock_tag));

select setval('mock_user_id_seq', (select max(id) from mock_user));

select setval('mock_vote_id_seq', (select max(id) from mock_vote));

drop index if exists mock_post_question_parent_id_index;
drop index if exists  mock_post_post_type_id_index;
drop index if exists  mock_post_user_id_index;
drop index if exists  mock_post_last_id_index;
drop index if exists  mock_comment_post_id_index;
drop index if exists  mock_comment_user_id_index;

create index mock_post_question_parent_id_index on mock_post (post_type_id, parent_id);
create index mock_post_post_type_id_index       on mock_post (post_type_id);
create index mock_post_user_id_index            on mock_post (owner_user_id);
create index mock_post_last_id_index            on mock_post (last_editor_user_id);
create index mock_comment_post_id_index         on mock_comment (post_id);
create index mock_comment_user_id_index         on mock_comment (user_id);

alter table mock_user
    ALTER column account_id set default nextval('mock_user_account_id_seq');
alter table mock_user
    ALTER column id set default nextval('mock_user_id_seq');


alter table mock_post
    add constraint mock_post_owner_user_id foreign key (owner_user_id) references mock_user (id);
alter table mock_post
    add constraint mock_post_parent_id foreign key (parent_id) references mock_post (id);
alter table mock_post
    add constraint mock_post_last_editor_user_id foreign key (last_editor_user_id) references mock_user (id);
alter table mock_post
    add constraint mock_post_accepted_answer_id foreign key (accepted_answer_id) references mock_post (id);

alter table mock_comment
    add constraint mock_comment_post_id_post_id foreign key (post_id) references mock_post (id);
alter table mock_comment
    add constraint mock_comment_user_id foreign key (user_id) references mock_user (id);

alter table mock_badge
    add constraint mock_badge_user_id_user_id foreign key (user_id) references mock_user (id);

-- alter table mock_tag add constraint mock_tag_excerpt_post_id_post_id foreign key (excerpt_post_id) references mock_post(id);

alter table mock_vote
    add constraint mock_vote_user_id_user_id foreign key (user_id) references mock_user (id);
-- alter table mock_vote add constraint mock_vote_user_id_post_id_type_id unique (user_id, post_id, vote_type_id);

alter table mock_post_history
    add constraint mock_post_history_post_id foreign key (post_id) references mock_post (id);
alter table mock_post_history
    add constraint mock_post_history_user_id foreign key (user_id) references mock_user (id);

-- alter table mock_post_link add constraint mock_post_link_related_post_id foreign key (related_post_id) references mock_post(id);
-- alter table mock_post_link add constraint mock_post_link_post_id foreign key (post_id) references mock_post(id);

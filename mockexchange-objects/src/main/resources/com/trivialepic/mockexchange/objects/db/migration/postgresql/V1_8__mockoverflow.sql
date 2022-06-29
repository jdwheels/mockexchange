alter table mock_post drop column post_type cascade;
drop type post_type;
alter table mock_post add column post_type varchar(16);
update mock_post set post_type = (
    select case
               when post_type_id = 1 then 'QUESTION'
               when post_type_id = 2 then 'ANSWER'
               when post_type_id = 3 then 'TYPE_3'
               when post_type_id = 4 then 'TYPE_4'
               when post_type_id = 5 then 'TYPE_5'
               when post_type_id = 6 then 'TYPE_6'
               when post_type_id = 7 then 'TYPE_7'
               else 'NA'
               end
);
alter table mock_post alter column post_type SET NOT NULL;

drop view if exists mock_post_summary;
create view mock_post_summary as
select p.title,
       p.id,
       p.answer_count,
       p.score,
       p.last_edit_date,
       p.body,
       p.tags,
       p.post_type_id,
       p.parent_id,
       p.post_type,
       le.display_name as last_editor_display_name,
       le.id           as last_editor_user_id,
       le.reputation   as last_editor_reputation
from mock_post p
         left outer join mock_user le on p.last_editor_user_id = le.id;


drop index if exists mock_post_post_type;
create index mock_post_post_type                on mock_post (post_type);

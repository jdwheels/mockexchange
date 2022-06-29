create type post_type as enum ('NA', 'QUESTION', 'ANSWER', 'TYPE_3', 'TYPE_4', 'TYPE_5', 'TYPE_6', 'TYPE_7');
alter table mock_post add column post_type post_type;
update mock_post set post_type = (
    select case
               when post_type_id = 1 then 'QUESTION'::post_type
               when post_type_id = 2 then 'ANSWER'::post_type
               when post_type_id = 3 then 'TYPE_3'::post_type
               when post_type_id = 4 then 'TYPE_4'::post_type
               when post_type_id = 5 then 'TYPE_5'::post_type
               when post_type_id = 6 then 'TYPE_6'::post_type
               when post_type_id = 7 then 'TYPE_7'::post_type
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

create function post_vote() returns trigger as
$post_vote$
BEGIN
    if (new.vote_type_id = 2) then
        UPDATE mock_post p set score = score + 1 where p.id = new.post_id;
    elsif (new.vote_type_id = 3) then
        UPDATE mock_post p set score = score - 1 where p.id = new.post_id;
    elseif (new.vote_type_id = 5) then
        UPDATE mock_post p set favorite_count = favorite_count + 1 where p.id = new.post_id;
    end if;
    return new;
END;
$post_vote$ language plpgsql;

create trigger post_vote
    before insert or update
    on mock_vote
    for each row
execute function post_vote();

create function post_comment() returns trigger as
$post_comment$
BEGIN
    if (TG_OP = 'DELETE') then
        UPDATE mock_post p set comment_count = greatest(comment_count - 1, 0) where p.id = old.post_id;
        return old;
    elsif (TG_OP = 'INSERT') then
        UPDATE mock_post p set comment_count = comment_count + 1 where p.id = new.post_id;
        return new;
    end if;
    return null;
END;
$post_comment$ language plpgsql;

create trigger post_comment
    before insert or delete
    on mock_comment
    for each row
execute function post_comment();


alter table mock_post
    alter column id set default nextval('mock_post_id_seq');
alter table mock_post
    alter column content_license set default 'UNLICENSED';
alter table mock_post
    alter column score set default 0;


alter table mock_vote
    alter column id set default nextval('mock_vote_id_seq');
alter table mock_vote
    alter column creation_date set default now();


alter table mock_comment
    alter column id set default nextval('mock_comment_id_seq');
alter table mock_comment
    alter column content_license set default 'UNLICENSED';
alter table mock_comment
    alter column creation_date set default now();
alter table mock_comment
    alter column score set default 0;

drop view mock_post_summary;
alter table mock_post alter column post_type type varchar(31);

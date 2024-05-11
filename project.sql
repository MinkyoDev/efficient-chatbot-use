-- 테이블 생성
DROP TABLE Chat_log;

CREATE TABLE Chat_log (
	log_id	number		NOT NULL,
	chat_id	number		NOT NULL,
	history_id	number		NULL,
	request	varChar2(4000)		NULL,
	response	varChar2(4000)		NULL,
	prompt_tokens	number		NOT NULL,
	completion_tokens	number		NOT NULL,
	create_at	timestamp	DEFAULT systimestamp	NOT NULL
);


DROP TABLE History;

CREATE TABLE History (
	history_id	number		NOT NULL,
	chat_id	number		NOT NULL,
	deps	number	DEFAULT 0	NOT NULL,
	summary	varChar2(4000)		NULL,
	prompt_tokens	number		NOT NULL,
	completion_tokens	number		NOT NULL
);

DROP TABLE Chat;

CREATE TABLE Chat (
	chat_id	number		NOT NULL,
	user_email	varChar2(100)		NOT NULL,
	model	varChar(200)		NOT NULL,
	name	varChar2(500)		NULL,
	stream_enabled	char(1)	DEFAULT 0	NOT NULL,
	memory_enabled	char(1)	DEFAULT 1	NOT NULL,
	ceche_enabled	char(1)	DEFAULT 1	NOT NULL
);

DROP TABLE Users;

CREATE TABLE Users (
	email	varChar2(100)		NOT NULL,
	password	varChar2(100)		NOT NULL,
	nicname	varChar2(100)		NOT NULL,
	is_active	char(1)	DEFAULT 1	NOT NULL
);

DROP TABLE Models;

CREATE TABLE Models (
	name	varChar(200)		NOT NULL,
	company	varChar2(1000)		NULL,
	price_per_ptoken	number		NOT NULL,
	price_per_ctoken	number		NOT NULL
);

ALTER TABLE Chat ADD CONSTRAINT PK_CHAT PRIMARY KEY (
	chat_id
);

ALTER TABLE Users ADD CONSTRAINT PK_USERS PRIMARY KEY (
	email
);

ALTER TABLE Models ADD CONSTRAINT PK_MODELS PRIMARY KEY (
	name
);

ALTER TABLE Chat_log ADD CONSTRAINT PK_CHAT_LOG PRIMARY KEY (
	log_id
);

ALTER TABLE History ADD CONSTRAINT PK_HISTORY PRIMARY KEY (
	history_id
);

ALTER TABLE Chat ADD CONSTRAINT FK_Users_TO_Chat_1 FOREIGN KEY (
	user_email
)
REFERENCES Users (
	email
);

ALTER TABLE Chat ADD CONSTRAINT FK_Models_TO_Chat_1 FOREIGN KEY (
	model
)
REFERENCES Models (
	name
);

ALTER TABLE Chat_log ADD CONSTRAINT FK_Chat_TO_Chat_log_1 FOREIGN KEY (
	chat_id
)
REFERENCES Chat (
	chat_id
);

ALTER TABLE Chat_log ADD CONSTRAINT FK_History_TO_Chat_log_1 FOREIGN KEY (
	history_id
)
REFERENCES History (
	history_id
);

ALTER TABLE History ADD CONSTRAINT FK_Chat_TO_History_1 FOREIGN KEY (
	chat_id
)
REFERENCES Chat (
	chat_id
);


-- 시퀀스
drop sequence chatid_seq;
CREATE SEQUENCE chatid_seq
       INCREMENT BY 1
       START WITH 1
       MINVALUE 1
       MAXVALUE 9999
       NOCYCLE
       NOCACHE
       NOORDER;

drop sequence hisid_seq;
CREATE SEQUENCE hisid_seq
       INCREMENT BY 1
       START WITH 1
       MINVALUE 1
       MAXVALUE 9999
       NOCYCLE
       NOCACHE
       NOORDER;
       
drop sequence logid_seq;
CREATE SEQUENCE logid_seq
       INCREMENT BY 1
       START WITH 1
       MINVALUE 1
       MAXVALUE 9999
       NOCYCLE
       NOCACHE
       NOORDER;
       
-- 트리거
create or replace trigger if_insert_history
after insert on history for each row
begin
    update chat_log set history_id = :new.history_id where history_id is null and chat_id = :new.chat_id;
end;
/

CREATE OR REPLACE TRIGGER Before_Delete_Chat
BEFORE DELETE
ON Chat
FOR EACH ROW
BEGIN
    -- Chat_log 테이블에서 참조를 먼저 삭제
    DELETE FROM Chat_log WHERE chat_id = :OLD.chat_id;
    
    -- History 테이블에서 참조를 먼저 삭제
    DELETE FROM History WHERE chat_id = :OLD.chat_id;
END;
/

-- 데이터 넣기
insert into models values ('gpt-3.5-turbo-1106', 'openAI', 0.0010/1000, 0.0020/1000);
insert into models values ('gpt-4', 'openAI', 0.03/1000, 0.06/1000);
insert into models values ('gpt-4-1106-preview', 'openAI', 0.01/1000, 0.03/1000);
insert into models values ('gpt-4-turbo', 'openAI', 10/1000000, 30/1000000);
commit;

insert into users (email, nicname, password) values ('11', '1111', '1111');
commit;

select * from chat_log;

select * from chat;

SELECT * FROM Chat_log WHERE chat_id = 9 AND EXISTS (SELECT 1 FROM Chat WHERE user_email = '11' AND Chat.chat_id = Chat_log.chat_id);

SELECT COUNT(*) count FROM Chat_log WHERE chat_id = 9;

SELECT c.*, cl.create_at AS last_modified_at
FROM Chat c
LEFT JOIN (
    SELECT chat_id, MAX(create_at) AS create_at
    FROM Chat_log
    GROUP BY chat_id
) cl ON c.chat_id = cl.chat_id
WHERE c.user_email = '11' AND cl.create_at IS NOT NULL
ORDER BY last_modified_at DESC;





--insert into chat (chat_id, user_email, model, memory_enabled, ceche_enabled) values (chatid_seq.nextVal, '11', 'gpt-3.5-turbo-1106', 1, 1);
--rollback;
--commit;

--insert into chat_log (log_id, chat_id, model_name, request, response, prompt_tokens, completion_tokens) values (logid_seq.nextVal, 1, 'gpt-3.5-turbo-1106', '안녕', '안녕하세요! 무엇을 도와드릴까요?',10, 10);

--insert into history (history_id, chat_id, deps, is_full, summary, prompt_tokens, completion_tokens) values (hisid_seq.nextVal, 1, 0, 0, '', 10, 10);
--rollback;

--select *, (select count(*) from chat_log where chat_id = 1) as cnt from chat where chat_id = 1;
--SELECT *, (SELECT COUNT(*) FROM chat_log WHERE chat_id = 1) AS chat_log_count FROM chat WHERE chat_id = 1;
--
--select * from chat_log join chat using(chat_id) where chat_id = 1;
--select count(*) from chat_log where chat_id = 1;
--select * from(select * from chat_log where chat_id = 5 order by create_at desc) logs where rownum<=3 order by logs.create_at asc;
--select * from chat_log where chat_id = 2 and request = '안녕';
--select * from models;
--delete from chat where chat_id = 1;
--select * from chat where user_email = '11';
--select * from chat_log where chat_id = 1;
--select * from chat;
--update users set is_active = 1 where id = '22';
--select * from users;
--commit;
--insert into users (id, password, is_active) values ('11', '11111', 1);
--select * from history ;
--delete from users;
--select sum(prompt_tokens) + sum(completion_tokens) total_tokens from chat_log where history_id is null and chat_id = 1;
--select logs.user_email, logs.chat_id, logs.request, logs.response from(select * from chat_log join Chat using (chat_id) where chat_id = 1 order by create_at desc) logs where rownum<=5 order by logs.create_at asc;
--select * from chat_log join Chat using (chat_id) where chat_id = 1 and history_id is null order by create_at;
--select summary from history where chat_id = 1;
--select * from chat_log where chat_id = 1;
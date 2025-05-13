USE schedule;

CREATE TABLE Schedules
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '일정 id',
    name VARCHAR(50) NOT NULL COMMENT '작성자 이름',
    title VARCHAR(50) NOT NULL COMMENT '일정 제목',
    contents VARCHAR(100) NOT NULL COMMENT '일정 내용',
    password VARCHAR(50) NOT NULL COMMENT '일정 비밀번호',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '일정 작성일',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '일정 수정일'
);

DROP TABLE Schedules;

CREATE TABLE schedule (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '일정 id',
                          name VARCHAR(50) NOT NULL COMMENT '작성자 이름',
                          title VARCHAR(50) NOT NULL COMMENT '일정 제목',
                          contents VARCHAR(100) NOT NULL COMMENT '일정 내용',
                          password VARCHAR(50) NOT NULL COMMENT '일정 비밀번호',
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '일정 작성일',
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '일정 수정일'
);

ALTER TABLE schedule ADD COLUMN user_id BIGINT NOT NULL COMMENT '작성자 id';

CREATE TABLE user
(
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '작성자 id',
    name VARCHAR(50) NOT NULL COMMENT '작성자 이름',
    email VARCHAR(255) COMMENT '작성자 이메일',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '일정 작성일',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '일정 수정일'

);

ALTER TABLE schedule ADD FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE;

ALTER TABLE schedule DROP COLUMN name;

SELECT * FROM schedule LEFT JOIN user on schedule.user_id = user.user_id;
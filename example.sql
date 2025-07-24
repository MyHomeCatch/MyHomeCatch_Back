CREATE TABLE users (
                      user_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 ID',
                      email VARCHAR(32) NOT NULL UNIQUE COMMENT '이메일',
                      name VARCHAR(16) NOT NULL COMMENT '이름',
                      nickname VARCHAR(16) NOT NULL COMMENT '닉네임',
                      password VARCHAR(100) NOT NULL COMMENT '비밀번호',
                      address VARCHAR(50) COMMENT '주소',
                      additional_point INT DEFAULT NULL COMMENT '가점'
);

SELECT * FROM users;

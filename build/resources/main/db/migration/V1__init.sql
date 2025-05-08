CREATE TABLE user
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    username      VARCHAR(255) NOT NULL UNIQUE,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    role          VARCHAR(50)  NOT NULL,
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    date_of_birth DATE,
    bio           TEXT,
    joined_date   DATE,
    is_active     BOOLEAN      NOT NULL,
    profile_image LONGBLOB
);


CREATE TABLE address
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    street      VARCHAR(255) NOT NULL,
    city        VARCHAR(255) NOT NULL,
    postal_code VARCHAR(20),
    country     VARCHAR(100) NOT NULL,
    user_id     BIGINT,
    CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE course
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    title             VARCHAR(255) NOT NULL,
    description       TEXT         NOT NULL,
    image_data        LONGBLOB,
    instructor_id     BIGINT       NOT NULL,
    enrollment_limit  INT,
    published         BOOLEAN      NOT NULL,
    category          VARCHAR(100),
    rating            DOUBLE,
    number_of_reviews INT,
    CONSTRAINT fk_course_instructor FOREIGN KEY (instructor_id) REFERENCES user (id)
);

CREATE TABLE file
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    type        VARCHAR(100),
    uploader_id BIGINT,
    course_id   BIGINT,
    CONSTRAINT fk_file_uploader FOREIGN KEY (uploader_id) REFERENCES user (id),
    CONSTRAINT fk_file_course FOREIGN KEY (course_id) REFERENCES course (id)
);

CREATE TABLE progress
(
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id           BIGINT,
    user_id             BIGINT,
    progress_percentage DOUBLE,
    CONSTRAINT fk_progress_course FOREIGN KEY (course_id) REFERENCES course (id),
    CONSTRAINT fk_progress_user FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE quiz
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id     BIGINT       NOT NULL,
    title         VARCHAR(255) NOT NULL,
    description   TEXT,
    total_marks   INT,
    passing_marks INT,
    created_by_id BIGINT,
    CONSTRAINT fk_quiz_course FOREIGN KEY (course_id) REFERENCES course (id),
    CONSTRAINT fk_quiz_creator FOREIGN KEY (created_by_id) REFERENCES user (id)
);

CREATE TABLE question
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    text            TEXT NOT NULL,
    type            VARCHAR(100),
    marks           INT,
    correct_answer  TEXT,
    requires_review BOOLEAN
);

CREATE TABLE answer
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    text        TEXT    NOT NULL,
    correct     BOOLEAN NOT NULL,
    explanation TEXT,
    question_id BIGINT,
    CONSTRAINT fk_answer_question FOREIGN KEY (question_id) REFERENCES question (id)
);

CREATE TABLE review
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id   BIGINT,
    user_id     BIGINT,
    review_text TEXT,
    rating      DOUBLE,
    CONSTRAINT fk_review_course FOREIGN KEY (course_id) REFERENCES course (id),
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE student_answer
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id          BIGINT,
    quiz_id          BIGINT,
    question_id      BIGINT,
    submitted_answer TEXT,
    awarded_marks    INT,
    mentor_feedback  TEXT,
    reviewed         BOOLEAN,
    CONSTRAINT fk_student_answer_user FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_student_answer_quiz FOREIGN KEY (quiz_id) REFERENCES quiz (id),
    CONSTRAINT fk_student_answer_question FOREIGN KEY (question_id) REFERENCES question (id)
);

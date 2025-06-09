CREATE TABLE course_part (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             course_id BIGINT NOT NULL,
                             title VARCHAR(255) NOT NULL,
                             week_number INT,
                             part_number INT,
                             CONSTRAINT fk_course_part_course FOREIGN KEY (course_id) REFERENCES course(id)
);

CREATE TABLE course_part_content (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     course_part_id BIGINT NOT NULL,
                                     content_type VARCHAR(50) NOT NULL,
                                     content_id BIGINT NOT NULL,
                                     CONSTRAINT fk_content_part FOREIGN KEY (course_part_id) REFERENCES course_part(id)
);

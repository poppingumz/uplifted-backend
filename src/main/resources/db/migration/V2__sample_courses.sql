-- Insert sample users (teachers)
INSERT INTO user (
    username, email, password, role, first_name, last_name, date_of_birth, bio, joined_date, is_active, profile_image
) VALUES
      ('jdoe', 'jdoe@example.com', 'securepass123', 'TEACHER', 'John', 'Doe', '1985-06-15', 'Experienced software developer and educator.', '2024-01-01', true, NULL),
      ('asmith', 'asmith@example.com', 'anotherpass456', 'TEACHER', 'Anna', 'Smith', '1990-03-22', 'Passionate about teaching design and UX.', '2024-02-10', true, NULL);

-- Insert sample courses
INSERT INTO course (
    title, description, image_data, instructor_id, enrollment_limit, published, category, rating, number_of_reviews
) VALUES
      ('Introduction to Web Development', 'Learn the fundamentals of HTML, CSS, and JavaScript to start building websites from scratch.', NULL, 1, 100, true, 'Web Development', 4.7, 24),
      ('UX/UI Design Basics', 'Master the basics of user experience and interface design to create intuitive and beautiful products.', NULL, 2, 50, true, 'Design', 4.5, 18);

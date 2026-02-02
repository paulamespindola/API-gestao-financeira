INSERT INTO users (name, email, password, role, status)
SELECT
  'Admin',
  'admin@gestaofinanceira.com',
  '$2a$10$KIXQ1OtS9DDDeWGPAHDq6O2Z4jGk5pI1l8Kd6jvZ4p7hY9YVxWZ9y',
  'ADMIN',
  'ACTIVE'
WHERE NOT EXISTS (
  SELECT 1 FROM users WHERE email = 'admin@gestaofinanceira.com'
);

ALTER TABLE users ADD COLUMN location TEXT;

UPDATE users
SET location = 'michigan'
WHERE id = 1;
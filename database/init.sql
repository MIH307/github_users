
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS github_users (
    id SERIAL PRIMARY KEY,
    git_id integer NOT NULL,
    login VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(255),
    html_url VARCHAR(255)
);
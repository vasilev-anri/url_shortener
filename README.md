REST URL Shortener API


Generates a short, fixed-size, and unique URL identifier for each request.

Allows premium clients to create custom short URLs.

Tracks and counts the number of times each shortened URL is accessed.

Automatically deletes URLs that are older than 30 days to maintain a clean database.

#


### Configuration
**environment variables** 

| Variable           | Example Value                |
|--------------------|------------------------------|
| `DB_HOST`          | `localhost`                  |
| `DB_PORT`          | `5432`                       |
| `DB_NAME`          | `url_shortener`              |
| `DB_USER`          | `your_db_user`               |
| `DB_PASSWORD`      | `your_db_password`           |
| `JWT_PRIVATE_KEY`  | `path_to_your/private.pem`   |
| `JWT_PUBLIC_KEY`   | `path_to_your/public.pem`    |
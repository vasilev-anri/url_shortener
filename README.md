REST URL Shortener API


Generates a short, fixed-size, and unique URL identifier for each request.

Allows premium clients to create custom short URLs.

Tracks and counts the number of times each shortened URL is accessed.

Automatically deletes URLs that are older than 30 days to maintain a clean database.

##

# JWT Certificate Setup

## Required Files
- `private.pem`: Private key for signing JWTs
- `public.pem`: Public key for verifying JWTs

## Generation Instructions
Run these commands (requires OpenSSL):

```bash
# Generate RSA key pair
openssl genrsa -out keypair.pem 2048

# Extract public key
openssl rsa -in keypair.pem -pubout -out public.pem

# Convert private key to PKCS#8 format
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem

# Cleanup (optional)
rm keypair.pem
```

##

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
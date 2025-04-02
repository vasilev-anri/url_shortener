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


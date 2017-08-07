# Tokens

This implements the handling of tokens within Token-based 
authentication.

Current implementation is based on Auth0's JWT: 
https://github.com/auth0/java-jwt

## Generating a Token
The following fields are included in a "self-contained" token:

* Random unique identifier to avoid forgery; changes over time.
* Email address of the user which remains consistent across all
tokens associated with the user (may be auto-generated for guest 
users).
* Session expiration time allowing for renewal.
* List of Badges earned by this user.

The token is also:
* Issued by a specific entity (domain name is used).
* Contains a secret stored outside of the code repo.

## Persistence
Tokens are indexed by the random unique identifier. This store 
changes over time as tokens expire and are renewed. When renewing 
a token, the old token is accepted and re-issued using a new ID.

## Validation
Checks performed:
* Issuer is valid.
* ID is found in index.
* Principal/User/Email is found in index.
* Expiration has not yet occurred.

## Guest Token
A special default token is used to indicate a new user.

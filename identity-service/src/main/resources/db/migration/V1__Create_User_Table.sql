CREATE TABLE fintech_users (
   id UUID PRIMARY KEY,
   first_name VARCHAR(255) NOT NULL,
   last_name VARCHAR(255) NOT NULL,

   email VARCHAR(255) NOT NULL UNIQUE,
   email_verified BOOLEAN NOT NULL,

   password_hash VARCHAR(255) NOT NULL,

   cpf VARCHAR(11) NOT NULL UNIQUE,

   phone_number VARCHAR(20) NOT NULL,
   phone_verified BOOLEAN NOT NULL,

   user_type VARCHAR(20) NOT NULL,

   is_active BOOLEAN NOT NULL DEFAULT TRUE,

   street VARCHAR(255) NOT NULL,
   neighborhood VARCHAR(255) NOT NULL,
   city VARCHAR(255) NOT NULL,
   state VARCHAR(2) NOT NULL,
   zip VARCHAR(20) NOT NULL,
   country VARCHAR(100) NOT NULL,

   created_at TIMESTAMP NOT NULL,
   updated_at TIMESTAMP NOT NULL
);

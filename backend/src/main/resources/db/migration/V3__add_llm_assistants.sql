CREATE TABLE llm_assistants
(
    id              UUID PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    model           VARCHAR(50) NOT NULL,
    prompt          TEXT,
    temperature     DECIMAL(3,2) DEFAULT 0.7,
    top_p           DECIMAL(3,2) DEFAULT 1.0,
    max_tokens      INT DEFAULT 1000
);
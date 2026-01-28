-- Sessions table: polling sessions created by hosts
CREATE TABLE sessions (
    id BIGSERIAL PRIMARY KEY,
    host_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    join_code VARCHAR(10) NOT NULL UNIQUE,
    join_mode VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    started_at TIMESTAMP,
    ended_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_sessions_host_id ON sessions(host_id);
CREATE INDEX idx_sessions_join_code ON sessions(join_code);
CREATE INDEX idx_sessions_status ON sessions(status);

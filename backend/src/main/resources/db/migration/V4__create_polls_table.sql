-- Polls table: questions within a session
CREATE TABLE polls (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES sessions(id) ON DELETE CASCADE,
    question TEXT NOT NULL,
    poll_type VARCHAR(20) NOT NULL DEFAULT 'MULTIPLE_CHOICE',
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    show_results BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    opened_at TIMESTAMP,
    closed_at TIMESTAMP
);

CREATE INDEX idx_polls_session_id ON polls(session_id);
CREATE INDEX idx_polls_status ON polls(status);

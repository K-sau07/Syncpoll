-- Answers table: who answered what
CREATE TABLE answers (
    id BIGSERIAL PRIMARY KEY,
    poll_id BIGINT NOT NULL REFERENCES polls(id) ON DELETE CASCADE,
    participant_id BIGINT NOT NULL REFERENCES participants(id) ON DELETE CASCADE,
    poll_option_id BIGINT NOT NULL REFERENCES poll_options(id) ON DELETE CASCADE,
    answered_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(poll_id, participant_id)
);

CREATE INDEX idx_answers_poll_id ON answers(poll_id);
CREATE INDEX idx_answers_participant_id ON answers(participant_id);

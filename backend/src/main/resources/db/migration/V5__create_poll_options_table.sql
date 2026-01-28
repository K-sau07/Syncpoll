-- Poll options table: answer choices for each poll
CREATE TABLE poll_options (
    id BIGSERIAL PRIMARY KEY,
    poll_id BIGINT NOT NULL REFERENCES polls(id) ON DELETE CASCADE,
    option_text VARCHAR(500) NOT NULL,
    option_order INT DEFAULT 0
);

CREATE INDEX idx_poll_options_poll_id ON poll_options(poll_id);

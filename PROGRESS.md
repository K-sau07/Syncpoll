# SyncPoll Progress

## Current Status: Setting up

---

## Done

- Project definition
- Feature scope for MVP
- Tech stack finalized
- Architecture design
- Project structure created
- Initial dependencies configured

## In Progress

- Database schema design
- Entity classes

## Up Next

- Repository layer
- Service layer
- REST Controllers
- Google OAuth integration
- WebSocket setup
- Kafka integration

---

## Key Decisions

| Decision | Choice | Why |
|----------|--------|-----|
| Build tool | Maven | Standard in enterprise, straightforward to use |
| Repo structure | Monorepo | Easier to manage when working solo |
| Package structure | By layer | Traditional approach, easier to navigate |
| Database | PostgreSQL | Need relational data and complex queries for analytics |
| Cache | Redis | Fast lookups for live session state |
| Messaging | Kafka | Durable event streaming, can't afford to lose answers |
| Auth | Google OAuth | No password management headaches, verified identities |

---

## MVP Scope

1. Host logs in with Google
2. Host creates a session (open or verified join)
3. Host gets a join code
4. Participants join with their name (or Google if verified mode)
5. Host runs live polls
6. Results update in real-time via WebSockets
7. Host can see exactly who answered what
8. Attendance tracked automatically
9. Session history saved for the host

---

## Patterns to Implement

- Logging with SLF4J
- Input validation with Bean Validation
- Global exception handling with @ControllerAdvice
- DTOs for all request/response objects
- Pagination for list endpoints
- Async processing where it makes sense
- Scheduled tasks for cleanup
- Health checks via Actuator

---

## Notes

- Domain: syncpoll.live (need to purchase)
- This is a portfolio project targeting backend roles at larger tech companies
- The goal is to show depth in Java, not just "I can wire up Spring Boot"

# SyncPoll

Real-time audience engagement for classrooms and webinars — live polls where
**attendance is a by-product of answering**, so hosts see who participated, not just
an anonymous bar chart.

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/Spring_Boot_3-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![React](https://img.shields.io/badge/React-61DAFB?style=flat-square&logo=react&logoColor=black)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white)

## The idea

Most polling tools give you aggregates: *73% picked B*. Useful for a show of hands,
useless if you're a professor who needs to know **who** was in the room and whether they
were following along.

SyncPoll records a participant the moment they answer. Attendance, engagement and poll
results come from the same event — there's no separate roll call to run.

## What works today

- **Live polls** — host creates, starts and closes polls; participants answer from a join code
- **Real-time updates** over WebSocket, no refresh
- **Attendance report** — per participant: join time, session duration, and answers given
  against polls run, with search, sorting and CSV export
- **Participant list** live during a session
- **Google OAuth** for hosts
- **Join by code or QR**

## Not built yet

Being explicit, because the dependencies are in `pom.xml` and it would be reasonable to
assume otherwise:

| | Status |
|:--|:--|
| **Kafka event streaming** | dependency present, **not implemented** — polls write straight to Postgres |
| **Redis caching** | dependency present, **not implemented** |
| **Cross-session analytics** | not started — attendance is per-session only |

## Stack

**Backend** — Java 21 · Spring Boot 3 · Spring Security + Google OAuth · WebSocket · Spring Data JPA · PostgreSQL
**Frontend** — React · Vite · Tailwind · React Router · TanStack Query
**CI** — GitHub Actions: backend tests against a Postgres service container, frontend build + vitest

## Running locally

```bash
# Postgres (the backend expects it on 5432)
docker compose up -d postgres

# backend  → http://localhost:8080
cd backend && ./mvnw spring-boot:run

# frontend → http://localhost:5173
cd frontend && npm install && npm run dev
```

## API

| Method | Path | |
|:--|:--|:--|
| `POST` | `/api/sessions` | create a session |
| `GET` | `/api/sessions/{id}` | session detail |
| `POST` | `/api/sessions/{id}/end` | end a session |
| `GET` | `/api/sessions/{id}/participants` | who joined |
| `GET` | `/api/sessions/{id}/attendance` | attendance report |
| `POST` | `/api/join` | join with a code |
| `POST` | `/api/sessions/{id}/polls` | create a poll |
| `POST` | `/api/sessions/{id}/polls/{pollId}/start` | open for answers |
| `POST` | `/api/sessions/{id}/polls/{pollId}/close` | stop accepting answers |
| `POST` | `/api/sessions/{id}/polls/{pollId}/answer` | submit an answer |
| `GET` | `/api/sessions/{id}/polls/{pollId}/results` | tallied results |

## Tests

```bash
cd backend  && ./mvnw test      # needs Postgres on 5432
cd frontend && npm run test:run
```

Backend coverage is currently thin — one context-load test. Frontend covers the join flow.

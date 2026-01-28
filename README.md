# SyncPoll

Real-time audience engagement platform for classrooms and webinars.

## What is this

SyncPoll lets hosts run live polls during presentations, track attendance automatically, and see analytics on how participants engage over time. Built for professors, TAs, and webinar hosts who need more than just anonymous polling.

## Features

- Live polls with real-time results
- Automatic attendance tracking when participants answer
- Host sees who answered what (not just aggregates)
- User analytics across multiple sessions
- Google OAuth for hosts
- Open or verified join modes for participants

## Tech Stack

**Backend**
- Java 21
- Spring Boot 3
- Spring Security with Google OAuth
- WebSockets for real-time updates
- PostgreSQL
- Redis for caching and live state
- Kafka for event streaming

**Frontend**
- React

**Infrastructure**
- Docker
- GitHub Actions for CI/CD

## Getting Started

### Prerequisites
- Java 21
- Maven
- Node.js 18+
- Docker

### Run the backend
```bash
cd backend
mvn spring-boot:run
```

### Run the frontend
```bash
cd frontend
npm install
npm run dev
```

### Run everything with Docker
```bash
cd docker
docker-compose up
```

## Project Structure

```
syncpoll/
├── backend/          # Java Spring Boot API
├── frontend/         # React app
├── docker/           # Docker compose files
└── .github/          # CI/CD workflows
```

## Why I built this

I was using Slido as a TA and kept running into the same problems - the free tier is limited, there's no good way to track individual student participation over a semester, and the analytics are shallow. So I decided to build something better.

This project also serves as a way to demonstrate what I know about Java backend development - concurrency, event streaming, real-time systems, and building something that actually works under load.

## License

MIT

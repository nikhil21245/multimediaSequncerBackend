# Multi-Window Media Sequencer

A full-stack media sequencing application that displays independent media playlists across multiple windows and provides real-time synchronized playback using WebSockets.

## Live Demo

**Frontend:**
`<YOUR_VERCEL_FRONTEND_URL>`

**Backend:**
`https://multimediasequncerbackend-production.up.railway.app`

**Backend API:**
`https://multimediasequncerbackend-production.up.railway.app/api`

---

## Overview

The Multi-Window Media Sequencer allows multiple display windows to run their own configured media playlists independently.

Each window can display:

* Images
* Videos
* Blank screens

The application also provides a synchronization feature that allows the same media to be displayed across all windows simultaneously for a configured duration. After synchronization ends, each window resumes its own playlist.

The application uses WebSockets for real-time synchronization between the backend and connected frontend windows.

---

## Features

* Four independent media windows
* Individual playlist configuration for each window
* Image playback
* Video playback
* Blank screen/fallback media
* Continuous playlist looping
* Five-hour playback cycle
* Real-time synchronization using WebSockets
* Dynamic playlist loading from the backend
* Persistent media and playlist configuration using MySQL
* REST APIs for windows, playlists, and synchronization
* Separate frontend and backend deployments

---

## Architecture

```text
                    ┌──────────────────────┐
                    │      React Frontend  │
                    │       (Vercel)       │
                    └──────────┬───────────┘
                               │
                    REST API   │   WebSocket
                               │
                               ▼
                    ┌──────────────────────┐
                    │   Spring Boot API    │
                    │      (Railway)       │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │        MySQL         │
                    │      (Railway)       │
                    └──────────────────────┘
```

### Communication

**REST API**

Used for:

* Loading windows
* Loading playlists
* Updating playlists
* Triggering synchronization

**WebSocket**

Used for:

* Broadcasting synchronization events
* Notifying all connected windows simultaneously
* Maintaining real-time playback synchronization

---

## Technology Stack

### Frontend

* React
* Vite
* JavaScript
* HTML
* CSS
* WebSocket API

### Backend

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* WebSocket
* Maven

### Database

* MySQL 8

### Deployment

* Frontend: Vercel
* Backend: Railway
* Database: Railway MySQL

---

## Project Structure

```text
mediaSequencerJava/
│
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── nikhil/
│                   └── mediaSequencerJava/
│
├── frontend/
│   ├── src/
│   │   ├── App.jsx
│   │   ├── App.css
│   │   └── main.jsx
│   │
│   ├── package.json
│   └── vite.config.js
│
├── pom.xml
└── README.md
```

---

## REST API

### Get all windows

```http
GET /api/windows
```

Returns all configured display windows.

### Get a window playlist

```http
GET /api/windows/{windowId}/playlist
```

Returns the media playlist configured for a specific window.

### Add media to a window

```http
POST /api/windows/{windowId}/media
```

Adds/configures media for a specific window.

### Trigger synchronization

```http
POST /api/sync
```

Broadcasts the selected media to all connected windows using WebSocket.

---

## WebSocket

WebSocket endpoint:

```text
wss://multimediasequncerbackend-production.up.railway.app/ws
```

When synchronization is triggered, the backend broadcasts an event containing information such as:

```json
{
  "mediaId": "M2",
  "mediaType": "VIDEO",
  "mediaUrl": "https://example.com/video.mp4",
  "durationMs": 30000,
  "startTime": 1726671234567
}
```

All connected frontend windows receive the event and begin displaying the synchronized media.

After the synchronization duration expires, each window returns to its previously configured playlist.

---

## Playback Logic

Each window maintains its own playlist.

For example:

```text
Window 1
    ↓
Image → Video → Image → Blank → repeat

Window 2
    ↓
Video → Image → Blank → repeat

Window 3
    ↓
Image → Image → Video → repeat

Window 4
    ↓
Blank → Image → Image → repeat
```

The playlists operate independently during normal playback.

When synchronization is triggered:

```text
                Sync Event
                    │
        ┌───────────┼───────────┐
        ↓           ↓           ↓
     Window 1    Window 2    Window 3    Window 4
        │           │           │           │
        └───────────┴───────────┴───────────┘
                    │
              Same media
                    │
             Sync duration
                    │
                    ↓
        Return to individual playlists
```

---

## Database

The application uses MySQL for persistent storage.

Main entities include:

* `Media`
* `MediaWindow`
* `WindowMedia`

The database stores media information and the relationship between windows and their configured playlists.

---

## Local Setup

### Prerequisites

* Java 21
* Maven
* Node.js
* MySQL 8

### Backend

Clone the repository:

```bash
git clone https://github.com/nikhil21245/multimediaSequncerBackend.git
```

Navigate to the project:

```bash
cd multimediaSequncerBackend
```

Configure the MySQL connection in `application.properties`.

Then run:

```bash
mvn spring-boot:run
```

Backend will start on:

```text
http://localhost:8080
```

### Frontend

Navigate to the frontend directory:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

Frontend will be available at:

```text
http://localhost:5173
```

---

## Deployment

### Frontend

The React application is deployed using Vercel.

The frontend communicates with the deployed Spring Boot backend through the Railway public API.

### Backend

The Spring Boot application is deployed using Railway.

The backend uses Railway MySQL for persistent storage.

---

## Repository

GitHub:

https://github.com/nikhil21245/multimediaSequncerBackend

---

## Assignment Requirements Covered

| Requirement              | Implementation            |
| ------------------------ | ------------------------- |
| Full-stack application   | React + Spring Boot       |
| Multiple display windows | Four independent windows  |
| Configurable media       | Database-backed playlists |
| Image support            | Yes                       |
| Video support            | Yes                       |
| Blank screen             | Yes                       |
| Continuous playback      | Playlist looping          |
| Five-hour cycle          | Implemented               |
| Dynamic playlist loading | REST API                  |
| Synchronization          | WebSocket                 |
| Persistent storage       | MySQL                     |
| Frontend deployment      | Vercel                    |
| Backend deployment       | Railway                   |
| Database deployment      | Railway MySQL             |

---

## Author

**Nikhil Kusale**

B.E. Electronics & Telecommunication
IIIT Pune

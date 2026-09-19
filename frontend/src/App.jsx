import { useEffect, useState } from 'react'
import './App.css'

const CYCLE_DURATION_MS = 5 * 60 * 60 * 1000

function App() {
  const [windows, setWindows] = useState([])
  const [selectedMedia, setSelectedMedia] = useState('M2')
  const [syncEvent, setSyncEvent] = useState(null)

  const loadWindows = () => {
    fetch('http://localhost:8080/api/windows')
      .then(response => response.json())
      .then(data => setWindows(data))
      .catch(error => console.error('Failed to fetch windows:', error))
  }

  const handleSync = () => {
    fetch('http://localhost:8080/api/sync', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        mediaId: selectedMedia,
        durationMs: 30000
      })
    })
      .then(response => response.json())
      .then(data => console.log('Sync response:', data))
      .catch(error => console.error('Sync failed:', error))
  }

  useEffect(() => {
    loadWindows()
  }, [])

  useEffect(() => {
    const ws = new WebSocket('ws://localhost:8080/ws')

    ws.onopen = () => {
      console.log('WebSocket connected!')
    }

    ws.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data)

        console.log('Sync event received:', data)

        setSyncEvent(data)
      } catch (error) {
        console.error('Failed to parse WebSocket message:', error)
      }
    }

    ws.onerror = (error) => {
      console.error('WebSocket error:', error)
    }

    ws.onclose = () => {
      console.log('WebSocket disconnected')
    }

    return () => ws.close()
  }, [])

  return (
    <div className="app">

      <header className="header">
        <h1>Multi-Window Media Sequencer</h1>
      </header>

      <div className="controls">

        <select
          value={selectedMedia}
          onChange={(event) => setSelectedMedia(event.target.value)}
        >
          <option value="M1">M1</option>
          <option value="M2">M2</option>
          <option value="M3">M3</option>
          <option value="M4">M4</option>
        </select>

        <button onClick={handleSync}>
          Sync
        </button>

        <button onClick={loadWindows}>
          Refresh Playlists
        </button>

      </div>

      <main className="windows">

        {windows.map(window => (
          <WindowCard
            key={window.id}
            window={window}
            syncEvent={syncEvent}
          />
        ))}

      </main>

    </div>
  )
}

function WindowCard({ window, syncEvent }) {

  const [playlist, setPlaylist] = useState([])
  const [currentIndex, setCurrentIndex] = useState(0)
  const [isSyncing, setIsSyncing] = useState(false)
  const [cycleStartTime] = useState(() => Date.now())

  const loadPlaylist = () => {

    fetch(`http://localhost:8080/api/windows/${window.id}/playlist`)
      .then(response => response.json())
      .then(data => {

        console.log(
          `Playlist loaded for Window ${window.id}:`,
          data.playlist
        )

        setPlaylist(data.playlist)

      })
      .catch(error => {

        console.error(
          `Failed to fetch playlist for Window ${window.id}:`,
          error
        )

      })
  }

  useEffect(() => {
    loadPlaylist()
  }, [window.id])

  useEffect(() => {

    const timer = setInterval(() => {

      const elapsed = Date.now() - cycleStartTime

      if (elapsed >= CYCLE_DURATION_MS) {

        console.log(
          `5-hour cycle completed for Window ${window.id}`
        )

        setCurrentIndex(0)
      }

    }, 1000)

    return () => clearInterval(timer)

  }, [cycleStartTime, window.id])

  useEffect(() => {

    if (playlist.length === 0 || isSyncing) {
      return
    }

    const currentMedia = playlist[currentIndex]

    if (!currentMedia) {
      return
    }

    const timer = setTimeout(() => {

      setCurrentIndex(
        (currentIndex + 1) % playlist.length
      )

    }, currentMedia.durationMs)

    return () => clearTimeout(timer)

  }, [playlist, currentIndex, isSyncing])

  useEffect(() => {

    if (!syncEvent) {
      return
    }

    console.log(
      `Window ${window.id} received sync:`,
      syncEvent.mediaId
    )

    setIsSyncing(true)

    const timer = setTimeout(() => {

      console.log(
        `Window ${window.id} sync finished`
      )

      setIsSyncing(false)

    }, syncEvent.durationMs)

    return () => clearTimeout(timer)

  }, [syncEvent, window.id])

  const currentMedia = playlist[currentIndex]

  let displayMedia = currentMedia

  if (isSyncing && syncEvent) {

    displayMedia = {
      id: syncEvent.mediaId,
      type: syncEvent.mediaType,
      url: syncEvent.mediaUrl,
      durationMs: syncEvent.durationMs
    }

  }

  return (

    <div className="window">

      {displayMedia && (

        <div className="now-playing">

          {displayMedia.type === 'IMAGE' && (

            <img
              key={displayMedia.id}
              className="media"
              src={displayMedia.url}
              alt=""
            />

          )}

          {displayMedia.type === 'VIDEO' && (

            <video
              key={displayMedia.id}
              className="media"
              src={displayMedia.url}
              autoPlay
              muted
              playsInline
            />

          )}

          {displayMedia.type === 'BLANK' && (

            <div className="blank-screen"></div>

          )}

        </div>

      )}

    </div>

  )
}

export default App
import { useEffect, useState } from 'react'
import './App.css'

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ||
  'https://multimediasequncerbackend-production.up.railway.app'

const WS_URL =
  API_BASE_URL.replace(/^http/, 'ws') + '/ws'

const CYCLE_DURATION_MS = 5 * 60 * 60 * 1000

function App() {
  const [windows, setWindows] = useState([])
  const [playlists, setPlaylists] = useState({})
  const [currentMedia, setCurrentMedia] = useState({})
  const [syncMedia, setSyncMedia] = useState(null)
  const [syncDuration, setSyncDuration] = useState(30)
  const [selectedSyncMedia, setSelectedSyncMedia] = useState('M1')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchWindows()
  }, [])

  useEffect(() => {
    if (windows.length === 0) {
      return
    }

    const interval = setInterval(() => {
      refreshPlaylists()
    }, 10000)

    return () => clearInterval(interval)
  }, [windows])

  useEffect(() => {
    if (windows.length === 0) {
      return
    }

    const timers = []

    windows.forEach((window) => {
      const playlist = playlists[window.id]

      if (!playlist || playlist.length === 0) {
        return
      }

      let currentIndex = 0

      const playNext = () => {
        const media = playlist[currentIndex]

        setCurrentMedia((prev) => ({
          ...prev,
          [window.id]: {
            media,
            startedAt: Date.now()
          }
        }))

        currentIndex = (currentIndex + 1) % playlist.length

        const duration = media.durationMs || 10000

        timers.push(
          setTimeout(playNext, duration)
        )
      }

      playNext()
    })

    return () => {
      timers.forEach(clearTimeout)
    }
  }, [windows, playlists])

  useEffect(() => {
    const ws = new WebSocket(WS_URL)

    ws.onopen = () => {
      console.log('WebSocket connected!')
    }

    ws.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data)

        setSyncMedia({
          ...data,
          receivedAt: Date.now()
        })
      } catch (error) {
        console.error('Invalid WebSocket message:', error)
      }
    }

    ws.onerror = (error) => {
      console.error('WebSocket error:', error)
    }

    ws.onclose = () => {
      console.log('WebSocket disconnected')
    }

    return () => {
      ws.close()
    }
  }, [])

  useEffect(() => {
    if (!syncMedia) {
      return
    }

    const duration =
      syncMedia.durationMs || syncDuration * 1000

    const timer = setTimeout(() => {
      setSyncMedia(null)
    }, duration)

    return () => clearTimeout(timer)
  }, [syncMedia])

  async function fetchWindows() {
    try {
      setLoading(true)

      const response = await fetch(
        `${API_BASE_URL}/api/windows`
      )

      if (!response.ok) {
        throw new Error(
          `HTTP ${response.status}`
        )
      }

      const data = await response.json()

      setWindows(data)

      await loadPlaylists(data)
    } catch (error) {
      console.error(
        'Failed to fetch windows:',
        error
      )
    } finally {
      setLoading(false)
    }
  }

  async function loadPlaylists(windowList) {
    const playlistData = {}

    for (const window of windowList) {
      try {
        const response = await fetch(
          `${API_BASE_URL}/api/windows/${window.id}/playlist`
        )

        if (!response.ok) {
          throw new Error(
            `HTTP ${response.status}`
          )
        }

        const data = await response.json()

        playlistData[window.id] = data
      } catch (error) {
        console.error(
          `Failed to fetch playlist for window ${window.id}:`,
          error
        )

        playlistData[window.id] = []
      }
    }

    setPlaylists(playlistData)
  }

  async function refreshPlaylists() {
    if (windows.length === 0) {
      return
    }

    await loadPlaylists(windows)
  }

  async function handleSync() {
    try {
      const response = await fetch(
        `${API_BASE_URL}/api/sync`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            mediaId: selectedSyncMedia,
            durationMs: syncDuration * 1000
          })
        }
      )

      if (!response.ok) {
        throw new Error(
          `HTTP ${response.status}`
        )
      }

      console.log('Sync request sent successfully')
    } catch (error) {
      console.error(
        'Failed to sync media:',
        error
      )
    }
  }

  function getMediaForWindow(windowId) {
    if (syncMedia) {
      return {
        id: syncMedia.mediaId,
        type: syncMedia.mediaType,
        url: syncMedia.mediaUrl,
        durationMs: syncMedia.durationMs
      }
    }

    return currentMedia[windowId]?.media
  }

  function renderMedia(media) {
    if (!media) {
      return (
        <div className="blank-screen" />
      )
    }

    const mediaType =
      media.mediaType || media.type

    const mediaUrl =
      media.mediaUrl || media.url

    if (mediaType === 'IMAGE') {
      return (
        <img
          className="media"
          src={mediaUrl}
          alt=""
        />
      )
    }

    if (mediaType === 'VIDEO') {
      return (
        <video
          className="media"
          src={mediaUrl}
          autoPlay
          muted
          playsInline
        />
      )
    }

    if (mediaType === 'BLANK') {
      return (
        <div className="blank-screen" />
      )
    }

    return (
      <div className="blank-screen" />
    )
  }

  if (loading) {
    return (
      <div className="app">
        <div className="header">
          <h1>Multi-Window Media Sequencer</h1>
        </div>

        <div className="windows">
          <div className="window">
            <div className="blank-screen" />
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className="app">
      <div className="header">
        <h1>
          Multi-Window Media Sequencer
        </h1>
      </div>

      <div className="controls">
        <select
          value={selectedSyncMedia}
          onChange={(event) =>
            setSelectedSyncMedia(
              event.target.value
            )
          }
        >
          <option value="M1">
            M1 - Nature Image
          </option>

          <option value="M2">
            M2 - Demo Video
          </option>

          <option value="M3">
            M3 - City Image
          </option>

          <option value="M4">
            M4 - Blank Screen
          </option>
        </select>

        <input
          type="number"
          min="1"
          value={syncDuration}
          onChange={(event) =>
            setSyncDuration(
              Number(event.target.value)
            )
          }
          style={{
            width: '70px',
            padding: '8px'
          }}
        />

        <button onClick={handleSync}>
          Sync
        </button>
      </div>

      <div className="windows">
        {windows.map((window) => {
          const media =
            getMediaForWindow(window.id)

          return (
            <div
              className="window"
              key={window.id}
            >
              <div className="now-playing">
                {renderMedia(media)}
              </div>
            </div>
          )
        })}
      </div>
    </div>
  )
}

export default App
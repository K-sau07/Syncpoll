// main app with routing

import { BrowserRouter, Routes, Route } from 'react-router-dom'
import JoinPage from './pages/JoinPage'
import ParticipantSessionPage from './pages/ParticipantSessionPage'
import HostDashboard from './pages/HostDashboard'
import HostSessionPage from './pages/HostSessionPage'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<JoinPage />} />
        <Route path="/session/:sessionId" element={<ParticipantSessionPage />} />
        <Route path="/host" element={<HostDashboard />} />
        <Route path="/host/session/:sessionId" element={<HostSessionPage />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App

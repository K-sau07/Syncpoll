// main app with routing

import { BrowserRouter, Routes, Route } from 'react-router-dom'
import JoinPage from './pages/JoinPage'
import ParticipantSessionPage from './pages/ParticipantSessionPage'
import HostDashboard from './pages/HostDashboard'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<JoinPage />} />
        <Route path="/session/:sessionId" element={<ParticipantSessionPage />} />
        <Route path="/host" element={<HostDashboard />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App

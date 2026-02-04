// main app with routing

import { BrowserRouter, Routes, Route } from 'react-router-dom'
import JoinPage from './pages/JoinPage'
import ParticipantSessionPage from './pages/ParticipantSessionPage'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<JoinPage />} />
        <Route path="/session/:sessionId" element={<ParticipantSessionPage />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App

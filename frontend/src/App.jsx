// main app with routing and toast provider

import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { ToastProvider } from './components/Toast'
import JoinPage from './pages/JoinPage'
import ParticipantSessionPage from './pages/ParticipantSessionPage'
import HostDashboard from './pages/HostDashboard'
import HostSessionPage from './pages/HostSessionPage'
import NotFoundPage from './pages/NotFoundPage'

function App() {
  return (
    <ToastProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<JoinPage />} />
          <Route path="/session/:sessionId" element={<ParticipantSessionPage />} />
          <Route path="/host" element={<HostDashboard />} />
          <Route path="/host/session/:sessionId" element={<HostSessionPage />} />
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </BrowserRouter>
    </ToastProvider>
  )
}

export default App

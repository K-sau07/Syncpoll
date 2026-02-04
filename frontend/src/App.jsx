// main app with routing

import { BrowserRouter, Routes, Route } from 'react-router-dom'
import JoinPage from './pages/JoinPage'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<JoinPage />} />
        {/* more routes coming */}
      </Routes>
    </BrowserRouter>
  )
}

export default App

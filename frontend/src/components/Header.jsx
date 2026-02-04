// main header component

import { useNavigate, useLocation } from 'react-router-dom'
import { LogOut, User } from 'lucide-react'
import { Button } from './ui'

function Header({ showAuth = true }) {
  const navigate = useNavigate()
  const location = useLocation()
  
  const isHost = location.pathname.startsWith('/host')
  
  const handleLogout = () => {
    localStorage.removeItem('token')
    navigate('/')
  }
  
  return (
    <header className="bg-surface border-b border-border">
      <div className="max-w-6xl mx-auto px-4 py-4 flex items-center justify-between">
        <button 
          onClick={() => navigate(isHost ? '/host' : '/')}
          className="text-xl font-bold text-primary hover:opacity-80 transition-opacity"
        >
          SyncPoll
        </button>
        
        {showAuth && isHost && (
          <div className="flex items-center gap-3">
            <span className="text-sm text-text-secondary flex items-center gap-2">
              <User className="w-4 h-4" />
              Host
            </span>
            <Button variant="ghost" size="sm" onClick={handleLogout}>
              <LogOut className="w-4 h-4 mr-1" />
              Sign Out
            </Button>
          </div>
        )}
      </div>
    </header>
  )
}

export default Header

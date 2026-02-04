// host dashboard showing all sessions

import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { Plus, Users, BarChart2, Calendar } from 'lucide-react'
import { Button, Card, Input } from '../components/ui'
import { sessionService } from '../services/session'

function HostDashboard() {
  const navigate = useNavigate()
  const [sessions, setSessions] = useState([])
  const [loading, setLoading] = useState(true)
  const [showCreateModal, setShowCreateModal] = useState(false)
  const [newSessionTitle, setNewSessionTitle] = useState('')
  const [joinMode, setJoinMode] = useState('OPEN')
  const [creating, setCreating] = useState(false)
  
  useEffect(() => {
    fetchSessions()
  }, [])
  
  const fetchSessions = async () => {
    try {
      const response = await sessionService.getAll()
      setSessions(response.data)
    } catch (err) {
      console.error('Failed to fetch sessions:', err)
    } finally {
      setLoading(false)
    }
  }
  
  const handleCreateSession = async (e) => {
    e.preventDefault()
    if (!newSessionTitle.trim()) return
    
    setCreating(true)
    try {
      const response = await sessionService.create({
        title: newSessionTitle,
        joinMode: joinMode
      })
      
      // navigate to the new session
      navigate(`/host/session/${response.data.id}`)
    } catch (err) {
      console.error('Failed to create session:', err)
    } finally {
      setCreating(false)
    }
  }
  
  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric'
    })
  }
  
  return (
    <div className="min-h-screen bg-background">
      {/* header */}
      <header className="bg-surface border-b border-border">
        <div className="max-w-6xl mx-auto px-4 py-4 flex items-center justify-between">
          <h1 className="text-xl font-bold text-primary">SyncPoll</h1>
          <Button onClick={() => setShowCreateModal(true)}>
            <Plus className="w-4 h-4 mr-2" />
            New Session
          </Button>
        </div>
      </header>
      
      {/* main content */}
      <main className="max-w-6xl mx-auto px-4 py-8">
        <h2 className="text-2xl font-semibold text-text-primary mb-6">Your Sessions</h2>
        
        {loading ? (
          <p className="text-text-secondary">Loading sessions...</p>
        ) : sessions.length === 0 ? (
          <Card className="text-center py-12">
            <div className="w-16 h-16 bg-primary/10 rounded-full flex items-center justify-center mx-auto mb-4">
              <BarChart2 className="w-8 h-8 text-primary" />
            </div>
            <h3 className="text-lg font-semibold text-text-primary mb-2">No sessions yet</h3>
            <p className="text-text-secondary mb-6">Create your first polling session to get started</p>
            <Button onClick={() => setShowCreateModal(true)}>
              <Plus className="w-4 h-4 mr-2" />
              Create Session
            </Button>
          </Card>
        ) : (
          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
            {sessions.map((session) => (
              <Card 
                key={session.id} 
                className="cursor-pointer hover:border-primary/50 transition-colors"
                onClick={() => navigate(`/host/session/${session.id}`)}
              >
                <div className="flex items-start justify-between mb-4">
                  <h3 className="font-semibold text-text-primary">{session.title}</h3>
                  <span className={`
                    px-2 py-1 rounded text-xs font-medium
                    ${session.status === 'ACTIVE' 
                      ? 'bg-success/10 text-success' 
                      : 'bg-gray-100 text-text-secondary'}
                  `}>
                    {session.status}
                  </span>
                </div>
                
                <div className="flex items-center gap-4 text-sm text-text-secondary">
                  <span className="flex items-center gap-1">
                    <Users className="w-4 h-4" />
                    {session.participantCount || 0}
                  </span>
                  <span className="flex items-center gap-1">
                    <BarChart2 className="w-4 h-4" />
                    {session.pollCount || 0} polls
                  </span>
                </div>
                
                <div className="mt-4 pt-4 border-t border-border flex items-center justify-between">
                  <span className="text-xs text-text-secondary flex items-center gap-1">
                    <Calendar className="w-3 h-3" />
                    {formatDate(session.createdAt)}
                  </span>
                  <span className="text-xs font-mono bg-gray-100 px-2 py-1 rounded">
                    {session.joinCode}
                  </span>
                </div>
              </Card>
            ))}
          </div>
        )}
      </main>
      
      {/* create session modal */}
      {showCreateModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50">
          <Card className="w-full max-w-md">
            <h3 className="text-lg font-semibold text-text-primary mb-4">Create New Session</h3>
            
            <form onSubmit={handleCreateSession} className="flex flex-col gap-4">
              <Input
                label="Session Title"
                placeholder="e.g. CS101 Lecture 5"
                value={newSessionTitle}
                onChange={(e) => setNewSessionTitle(e.target.value)}
                required
              />
              
              <div>
                <label className="text-sm font-medium text-text-primary mb-2 block">
                  Join Mode
                </label>
                <div className="flex gap-3">
                  <button
                    type="button"
                    onClick={() => setJoinMode('OPEN')}
                    className={`
                      flex-1 p-3 rounded-lg border text-sm font-medium transition-colors
                      ${joinMode === 'OPEN' 
                        ? 'border-primary bg-primary/5 text-primary' 
                        : 'border-border text-text-secondary hover:border-primary/50'}
                    `}
                  >
                    Open
                    <p className="font-normal text-xs mt-1">Anyone with code can join</p>
                  </button>
                  <button
                    type="button"
                    onClick={() => setJoinMode('VERIFIED')}
                    className={`
                      flex-1 p-3 rounded-lg border text-sm font-medium transition-colors
                      ${joinMode === 'VERIFIED' 
                        ? 'border-primary bg-primary/5 text-primary' 
                        : 'border-border text-text-secondary hover:border-primary/50'}
                    `}
                  >
                    Verified
                    <p className="font-normal text-xs mt-1">Requires sign in</p>
                  </button>
                </div>
              </div>
              
              <div className="flex gap-3 mt-2">
                <Button 
                  type="button" 
                  variant="secondary" 
                  onClick={() => setShowCreateModal(false)}
                  className="flex-1"
                >
                  Cancel
                </Button>
                <Button 
                  type="submit" 
                  disabled={creating || !newSessionTitle.trim()}
                  className="flex-1"
                >
                  {creating ? 'Creating...' : 'Create'}
                </Button>
              </div>
            </form>
          </Card>
        </div>
      )}
    </div>
  )
}

export default HostDashboard

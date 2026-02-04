// landing page where participants enter join code

import { useState, useEffect } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { Button, Input, Card } from '../components/ui'
import { sessionService } from '../services/session'
import { useToast } from '../components/Toast'

function JoinPage() {
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const toast = useToast()
  
  const [joinCode, setJoinCode] = useState('')
  const [displayName, setDisplayName] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  
  // auto-fill code from URL parameter
  useEffect(() => {
    const codeFromUrl = searchParams.get('code')
    if (codeFromUrl) {
      setJoinCode(codeFromUrl.toUpperCase())
    }
  }, [searchParams])
  
  const handleJoin = async (e) => {
    e.preventDefault()
    setError('')
    
    if (!joinCode.trim()) {
      setError('Please enter a join code')
      return
    }
    
    if (!displayName.trim()) {
      setError('Please enter your name')
      return
    }
    
    setLoading(true)
    
    try {
      const response = await sessionService.join(joinCode.toUpperCase(), displayName)
      const { sessionId, participantId } = response.data
      
      // store participant info for this session
      localStorage.setItem('participantId', participantId)
      localStorage.setItem('sessionId', sessionId)
      
      toast.success('Joined session!')
      navigate(`/session/${sessionId}`)
    } catch (err) {
      if (err.response?.status === 404) {
        setError('Session not found. Check your code and try again.')
      } else if (err.response?.data?.message) {
        setError(err.response.data.message)
      } else {
        setError('Something went wrong. Please try again.')
      }
      toast.error('Failed to join session')
    } finally {
      setLoading(false)
    }
  }
  
  return (
    <div className="min-h-screen bg-background flex flex-col items-center justify-center p-4">
      <div className="w-full max-w-md">
        {/* logo and title */}
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-primary mb-2">SyncPoll</h1>
          <p className="text-text-secondary">Join a live polling session</p>
        </div>
        
        {/* join form */}
        <Card>
          <form onSubmit={handleJoin} className="flex flex-col gap-4">
            <Input
              label="Join Code"
              placeholder="Enter 6-digit code"
              value={joinCode}
              onChange={(e) => setJoinCode(e.target.value.toUpperCase())}
              disabled={loading}
            />
            
            <Input
              label="Your Name"
              placeholder="How should we call you?"
              value={displayName}
              onChange={(e) => setDisplayName(e.target.value)}
              disabled={loading}
            />
            
            {error && (
              <p className="text-sm text-error text-center">{error}</p>
            )}
            
            <Button 
              type="submit" 
              fullWidth 
              disabled={loading}
            >
              {loading ? 'Joining...' : 'Join Session'}
            </Button>
          </form>
        </Card>
        
        {/* host link */}
        <p className="text-center mt-6 text-text-secondary text-sm">
          Are you a host?{' '}
          <a href="/host" className="text-primary hover:underline">
            Sign in here
          </a>
        </p>
      </div>
    </div>
  )
}

export default JoinPage

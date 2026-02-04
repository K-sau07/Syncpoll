// host view for managing a live session

import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { ArrowLeft, Copy, Users, Plus, Play, Square, Check } from 'lucide-react'
import { Button, Card, Input } from '../components/ui'
import { sessionService, pollService } from '../services/session'

function HostSessionPage() {
  const { sessionId } = useParams()
  const navigate = useNavigate()
  
  const [session, setSession] = useState(null)
  const [polls, setPolls] = useState([])
  const [loading, setLoading] = useState(true)
  const [showCreatePoll, setShowCreatePoll] = useState(false)
  const [codeCopied, setCodeCopied] = useState(false)
  
  // poll creation form
  const [question, setQuestion] = useState('')
  const [options, setOptions] = useState(['', ''])
  const [creating, setCreating] = useState(false)
  
  useEffect(() => {
    fetchSessionData()
  }, [sessionId])
  
  const fetchSessionData = async () => {
    try {
      const [sessionRes, pollsRes] = await Promise.all([
        sessionService.getById(sessionId),
        pollService.getBySession(sessionId)
      ])
      setSession(sessionRes.data)
      setPolls(pollsRes.data)
    } catch (err) {
      console.error('Failed to fetch session:', err)
    } finally {
      setLoading(false)
    }
  }
  
  const copyJoinCode = async () => {
    await navigator.clipboard.writeText(session.joinCode)
    setCodeCopied(true)
    setTimeout(() => setCodeCopied(false), 2000)
  }
  
  const handleCreatePoll = async (e) => {
    e.preventDefault()
    if (!question.trim() || options.filter(o => o.trim()).length < 2) return
    
    setCreating(true)
    try {
      await pollService.create(sessionId, {
        question: question,
        options: options.filter(o => o.trim())
      })
      
      // reset form and refresh
      setQuestion('')
      setOptions(['', ''])
      setShowCreatePoll(false)
      fetchSessionData()
    } catch (err) {
      console.error('Failed to create poll:', err)
    } finally {
      setCreating(false)
    }
  }
  
  const addOption = () => {
    if (options.length < 6) {
      setOptions([...options, ''])
    }
  }
  
  const updateOption = (index, value) => {
    const newOptions = [...options]
    newOptions[index] = value
    setOptions(newOptions)
  }
  
  const removeOption = (index) => {
    if (options.length > 2) {
      setOptions(options.filter((_, i) => i !== index))
    }
  }
  
  const startPoll = async (pollId) => {
    try {
      await pollService.start(sessionId, pollId)
      fetchSessionData()
    } catch (err) {
      console.error('Failed to start poll:', err)
    }
  }
  
  const stopPoll = async (pollId) => {
    try {
      await pollService.stop(sessionId, pollId)
      fetchSessionData()
    } catch (err) {
      console.error('Failed to stop poll:', err)
    }
  }
  
  if (loading) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <p className="text-text-secondary">Loading session...</p>
      </div>
    )
  }
  
  if (!session) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <p className="text-text-secondary">Session not found</p>
      </div>
    )
  }
  
  return (
    <div className="min-h-screen bg-background">
      {/* header */}
      <header className="bg-surface border-b border-border sticky top-0 z-10">
        <div className="max-w-6xl mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-4">
              <button 
                onClick={() => navigate('/host')}
                className="text-text-secondary hover:text-text-primary"
              >
                <ArrowLeft className="w-5 h-5" />
              </button>
              <div>
                <h1 className="font-semibold text-text-primary">{session.title}</h1>
                <div className="flex items-center gap-2 mt-1">
                  <span className={`
                    px-2 py-0.5 rounded text-xs font-medium
                    ${session.status === 'ACTIVE' 
                      ? 'bg-success/10 text-success' 
                      : 'bg-gray-100 text-text-secondary'}
                  `}>
                    {session.status}
                  </span>
                  <span className="text-text-secondary text-sm flex items-center gap-1">
                    <Users className="w-4 h-4" />
                    {session.participantCount || 0} participants
                  </span>
                </div>
              </div>
            </div>
            
            {/* join code */}
            <div className="flex items-center gap-2">
              <div className="text-right">
                <p className="text-xs text-text-secondary">Join Code</p>
                <p className="font-mono font-bold text-lg text-primary">{session.joinCode}</p>
              </div>
              <Button 
                variant="secondary" 
                size="sm"
                onClick={copyJoinCode}
              >
                {codeCopied ? <Check className="w-4 h-4" /> : <Copy className="w-4 h-4" />}
              </Button>
            </div>
          </div>
        </div>
      </header>
      
      {/* main content */}
      <main className="max-w-6xl mx-auto px-4 py-6">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-lg font-semibold text-text-primary">Polls</h2>
          <Button onClick={() => setShowCreatePoll(true)}>
            <Plus className="w-4 h-4 mr-2" />
            Create Poll
          </Button>
        </div>
        
        {polls.length === 0 ? (
          <Card className="text-center py-12">
            <p className="text-text-secondary mb-4">No polls yet. Create your first poll to engage your audience.</p>
            <Button onClick={() => setShowCreatePoll(true)}>
              <Plus className="w-4 h-4 mr-2" />
              Create Poll
            </Button>
          </Card>
        ) : (
          <div className="flex flex-col gap-4">
            {polls.map((poll) => (
              <PollCard 
                key={poll.id} 
                poll={poll}
                onStart={() => startPoll(poll.id)}
                onStop={() => stopPoll(poll.id)}
              />
            ))}
          </div>
        )}
      </main>
      
      {/* create poll modal */}
      {showCreatePoll && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50">
          <Card className="w-full max-w-lg max-h-[90vh] overflow-y-auto">
            <h3 className="text-lg font-semibold text-text-primary mb-4">Create Poll</h3>
            
            <form onSubmit={handleCreatePoll} className="flex flex-col gap-4">
              <Input
                label="Question"
                placeholder="What do you want to ask?"
                value={question}
                onChange={(e) => setQuestion(e.target.value)}
                required
              />
              
              <div>
                <label className="text-sm font-medium text-text-primary mb-2 block">
                  Options
                </label>
                <div className="flex flex-col gap-2">
                  {options.map((option, index) => (
                    <div key={index} className="flex gap-2">
                      <Input
                        placeholder={`Option ${index + 1}`}
                        value={option}
                        onChange={(e) => updateOption(index, e.target.value)}
                        className="flex-1"
                      />
                      {options.length > 2 && (
                        <button
                          type="button"
                          onClick={() => removeOption(index)}
                          className="px-3 text-text-secondary hover:text-error"
                        >
                          ×
                        </button>
                      )}
                    </div>
                  ))}
                </div>
                {options.length < 6 && (
                  <button
                    type="button"
                    onClick={addOption}
                    className="mt-2 text-sm text-primary hover:underline"
                  >
                    + Add option
                  </button>
                )}
              </div>
              
              <div className="flex gap-3 mt-2">
                <Button 
                  type="button" 
                  variant="secondary" 
                  onClick={() => setShowCreatePoll(false)}
                  className="flex-1"
                >
                  Cancel
                </Button>
                <Button 
                  type="submit" 
                  disabled={creating || !question.trim() || options.filter(o => o.trim()).length < 2}
                  className="flex-1"
                >
                  {creating ? 'Creating...' : 'Create Poll'}
                </Button>
              </div>
            </form>
          </Card>
        </div>
      )}
    </div>
  )
}

// poll card component
function PollCard({ poll, onStart, onStop }) {
  const totalVotes = poll.options?.reduce((sum, o) => sum + (o.votes || 0), 0) || 0
  
  return (
    <Card>
      <div className="flex items-start justify-between mb-4">
        <div>
          <h3 className="font-semibold text-text-primary">{poll.question}</h3>
          <span className={`
            inline-block mt-1 px-2 py-0.5 rounded text-xs font-medium
            ${poll.status === 'ACTIVE' ? 'bg-success/10 text-success' : ''}
            ${poll.status === 'DRAFT' ? 'bg-gray-100 text-text-secondary' : ''}
            ${poll.status === 'CLOSED' ? 'bg-error/10 text-error' : ''}
          `}>
            {poll.status}
          </span>
        </div>
        
        <div className="flex gap-2">
          {poll.status === 'DRAFT' && (
            <Button size="sm" onClick={onStart}>
              <Play className="w-4 h-4 mr-1" />
              Start
            </Button>
          )}
          {poll.status === 'ACTIVE' && (
            <Button size="sm" variant="danger" onClick={onStop}>
              <Square className="w-4 h-4 mr-1" />
              Stop
            </Button>
          )}
        </div>
      </div>
      
      {/* results */}
      <div className="flex flex-col gap-2">
        {poll.options?.map((option) => {
          const percentage = totalVotes > 0 ? Math.round((option.votes || 0) / totalVotes * 100) : 0
          
          return (
            <div key={option.id} className="relative">
              <div className="flex justify-between items-center p-3 rounded-lg border border-border relative z-10">
                <span className="text-text-primary">{option.text}</span>
                <span className="text-text-secondary text-sm">
                  {option.votes || 0} ({percentage}%)
                </span>
              </div>
              <div 
                className="absolute inset-0 bg-primary/10 rounded-lg"
                style={{ width: `${percentage}%` }}
              />
            </div>
          )
        })}
      </div>
      
      <p className="text-xs text-text-secondary mt-3">
        {totalVotes} {totalVotes === 1 ? 'response' : 'responses'}
      </p>
    </Card>
  )
}

export default HostSessionPage

// participant view of a live session with polls

import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { Button, Card } from '../components/ui'
import { pollService } from '../services/session'

function ParticipantSessionPage() {
  const { sessionId } = useParams()
  const navigate = useNavigate()
  const [currentPoll, setCurrentPoll] = useState(null)
  const [selectedOption, setSelectedOption] = useState(null)
  const [hasAnswered, setHasAnswered] = useState(false)
  const [results, setResults] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  
  const participantId = localStorage.getItem('participantId')
  
  useEffect(() => {
    if (!participantId) {
      navigate('/')
      return
    }
    
    // for now, fetch polls on load
    // websocket will replace this later
    fetchCurrentPoll()
  }, [sessionId, participantId])
  
  const fetchCurrentPoll = async () => {
    try {
      const response = await pollService.getBySession(sessionId)
      const polls = response.data
      
      // find the active poll
      const activePoll = polls.find(p => p.status === 'ACTIVE')
      if (activePoll) {
        setCurrentPoll(activePoll)
        // check if already answered
        const answered = activePoll.answers?.some(a => a.participantId === parseInt(participantId))
        setHasAnswered(answered)
      } else {
        setCurrentPoll(null)
      }
      
      setLoading(false)
    } catch (err) {
      setError('Failed to load poll')
      setLoading(false)
    }
  }
  
  const handleAnswer = async () => {
    if (!selectedOption || hasAnswered) return
    
    try {
      await pollService.answer(sessionId, currentPoll.id, selectedOption, participantId)
      setHasAnswered(true)
      
      // fetch results after answering
      const resultsResponse = await pollService.getResults(sessionId, currentPoll.id)
      setResults(resultsResponse.data)
    } catch (err) {
      if (err.response?.data?.message) {
        setError(err.response.data.message)
      } else {
        setError('Failed to submit answer')
      }
    }
  }
  
  if (loading) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <p className="text-text-secondary">Loading...</p>
      </div>
    )
  }
  
  if (!currentPoll) {
    return (
      <div className="min-h-screen bg-background flex flex-col items-center justify-center p-4">
        <Card className="max-w-md w-full text-center">
          <div className="py-8">
            <div className="w-16 h-16 bg-primary/10 rounded-full flex items-center justify-center mx-auto mb-4">
              <span className="text-3xl">⏳</span>
            </div>
            <h2 className="text-xl font-semibold text-text-primary mb-2">
              Waiting for poll
            </h2>
            <p className="text-text-secondary">
              The host hasn't started a poll yet. Hang tight!
            </p>
          </div>
        </Card>
        
        <button 
          onClick={fetchCurrentPoll}
          className="mt-4 text-primary hover:underline text-sm"
        >
          Refresh
        </button>
      </div>
    )
  }
  
  return (
    <div className="min-h-screen bg-background p-4">
      <div className="max-w-lg mx-auto">
        {/* header */}
        <div className="text-center mb-6">
          <span className="inline-flex items-center gap-2 px-3 py-1 bg-success/10 text-success rounded-full text-sm font-medium">
            <span className="w-2 h-2 bg-success rounded-full animate-pulse"></span>
            Live Poll
          </span>
        </div>
        
        {/* poll question */}
        <Card className="mb-4">
          <h2 className="text-xl font-semibold text-text-primary mb-6">
            {currentPoll.question}
          </h2>
          
          {/* options */}
          <div className="flex flex-col gap-3">
            {currentPoll.options.map((option) => {
              const isSelected = selectedOption === option.id
              const showResults = hasAnswered && results
              const voteCount = showResults 
                ? results.options?.find(o => o.id === option.id)?.votes || 0 
                : 0
              const totalVotes = showResults 
                ? results.options?.reduce((sum, o) => sum + (o.votes || 0), 0) 
                : 0
              const percentage = totalVotes > 0 ? Math.round((voteCount / totalVotes) * 100) : 0
              
              return (
                <button
                  key={option.id}
                  onClick={() => !hasAnswered && setSelectedOption(option.id)}
                  disabled={hasAnswered}
                  className={`
                    relative p-4 rounded-lg border text-left transition-all
                    ${isSelected && !hasAnswered ? 'border-primary bg-primary/5' : 'border-border'}
                    ${hasAnswered ? 'cursor-default' : 'hover:border-primary/50 cursor-pointer'}
                  `}
                >
                  <div className="flex justify-between items-center relative z-10">
                    <span className="font-medium text-text-primary">{option.text}</span>
                    {showResults && (
                      <span className="text-text-secondary text-sm">{percentage}%</span>
                    )}
                  </div>
                  
                  {/* result bar */}
                  {showResults && (
                    <div 
                      className="absolute inset-0 bg-primary/10 rounded-lg transition-all"
                      style={{ width: `${percentage}%` }}
                    />
                  )}
                </button>
              )
            })}
          </div>
          
          {/* submit button */}
          {!hasAnswered && (
            <Button 
              onClick={handleAnswer}
              fullWidth
              disabled={!selectedOption}
              className="mt-6"
            >
              Submit Answer
            </Button>
          )}
          
          {/* answered confirmation */}
          {hasAnswered && (
            <p className="text-center text-success mt-6 font-medium">
              Answer submitted!
            </p>
          )}
          
          {error && (
            <p className="text-center text-error mt-4 text-sm">{error}</p>
          )}
        </Card>
      </div>
    </div>
  )
}

export default ParticipantSessionPage

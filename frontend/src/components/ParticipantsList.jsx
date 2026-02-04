// participants list component for host session view

import { Users } from 'lucide-react'
import { Card } from './ui'

function ParticipantsList({ participants = [], className = '' }) {
  if (participants.length === 0) {
    return (
      <Card className={className}>
        <div className="flex items-center gap-2 mb-4">
          <Users className="w-5 h-5 text-text-secondary" />
          <h3 className="font-semibold text-text-primary">Participants</h3>
        </div>
        <p className="text-text-secondary text-sm text-center py-4">
          No one has joined yet
        </p>
      </Card>
    )
  }
  
  return (
    <Card className={className}>
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-2">
          <Users className="w-5 h-5 text-text-secondary" />
          <h3 className="font-semibold text-text-primary">Participants</h3>
        </div>
        <span className="text-sm text-text-secondary">
          {participants.length} joined
        </span>
      </div>
      
      <div className="flex flex-col gap-2 max-h-64 overflow-y-auto">
        {participants.map((participant) => (
          <div 
            key={participant.id}
            className="flex items-center justify-between p-2 rounded-lg bg-background"
          >
            <div className="flex items-center gap-3">
              <div className="w-8 h-8 rounded-full bg-primary/10 flex items-center justify-center">
                <span className="text-sm font-medium text-primary">
                  {participant.displayName?.charAt(0).toUpperCase() || '?'}
                </span>
              </div>
              <span className="text-text-primary text-sm">
                {participant.displayName}
              </span>
            </div>
            
            {participant.answeredCount !== undefined && (
              <span className="text-xs text-text-secondary">
                {participant.answeredCount} answers
              </span>
            )}
          </div>
        ))}
      </div>
    </Card>
  )
}

export default ParticipantsList

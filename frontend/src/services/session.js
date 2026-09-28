// session related api calls

import api from './api'

export const sessionService = {
  // create a new session
  create: (data) => api.post('/sessions', data),
  
  // get session by id
  getById: (id) => api.get(`/sessions/${id}`),
  
  // get all sessions for host
  getAll: () => api.get('/sessions'),
  
  // join a session with code
  join: (joinCode, displayName) => api.post('/join', { joinCode, displayName }),
  
  // end a session
  end: (id) => api.post(`/sessions/${id}/end`),

  // attendance report — who joined, when, and how many answers they gave
  attendance: (id) => api.get(`/sessions/${id}/attendance`),

  // raw participant list
  participants: (id) => api.get(`/sessions/${id}/participants`)
}

export const pollService = {
  // create poll in session
  create: (sessionId, data) => api.post(`/sessions/${sessionId}/polls`, data),
  
  // get polls for session
  getBySession: (sessionId) => api.get(`/sessions/${sessionId}/polls`),
  
  // start a poll
  start: (sessionId, pollId) => api.post(`/sessions/${sessionId}/polls/${pollId}/start`),
  
  // close a poll (backend exposes /close, not /stop)
  stop: (sessionId, pollId) => api.post(`/sessions/${sessionId}/polls/${pollId}/close`),
  
  // submit answer
  answer: (sessionId, pollId, optionId, participantId) => 
    api.post(`/sessions/${sessionId}/polls/${pollId}/answer`, 
      { optionId },
      { headers: { 'X-Participant-Id': participantId } }
    ),
  
  // get results
  getResults: (sessionId, pollId) => api.get(`/sessions/${sessionId}/polls/${pollId}/results`)
}

// attendance report for a session — who joined, when, and how much they answered.
// backs GET /sessions/{id}/attendance, which had no UI until now.

import { useState, useEffect, useMemo } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { ArrowLeft, Users, Download, Search } from 'lucide-react'
import { Button, Card, Input, EmptyState, Loading } from '../components/ui'
import PropTypes from 'prop-types'
import { sessionService, pollService } from '../services/session'

function AttendancePage() {
  const { sessionId } = useParams()
  const navigate = useNavigate()

  const [session, setSession] = useState(null)
  const [rows, setRows] = useState([])
  const [polls, setPolls] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [query, setQuery] = useState('')
  const [sortBy, setSortBy] = useState('joinedAt') // joinedAt | name | answers

  useEffect(() => {
    let cancelled = false

    const load = async () => {
      try {
        const [sessionRes, attendanceRes, pollsRes] = await Promise.all([
          sessionService.getById(sessionId),
          sessionService.attendance(sessionId),
          pollService.getBySession(sessionId),
        ])
        if (cancelled) return
        setSession(sessionRes.data)
        setRows(attendanceRes.data ?? [])
        setPolls(pollsRes.data ?? [])
        setError('')
      } catch (err) {
        if (cancelled) return
        console.error('Failed to load attendance', err)
        setError('Could not load attendance for this session.')
      } finally {
        if (!cancelled) setLoading(false)
      }
    }

    load()
    return () => { cancelled = true }
  }, [sessionId])

  const pollCount = polls.length

  const visible = useMemo(() => {
    const q = query.trim().toLowerCase()
    const filtered = q
      ? rows.filter((r) => (r.participantName ?? '').toLowerCase().includes(q))
      : rows
    const sorted = [...filtered]
    if (sortBy === 'name') {
      sorted.sort((a, b) => (a.participantName ?? '').localeCompare(b.participantName ?? ''))
    } else if (sortBy === 'answers') {
      sorted.sort((a, b) => (b.answersCount ?? 0) - (a.answersCount ?? 0))
    } else {
      sorted.sort((a, b) => new Date(a.joinedAt) - new Date(b.joinedAt))
    }
    return sorted
  }, [rows, query, sortBy])

  const fullyEngaged = rows.filter(
    (r) => pollCount > 0 && (r.answersCount ?? 0) >= pollCount
  ).length

  const time = (iso) =>
    iso ? new Date(iso).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : '—'

  const duration = (row) => {
    if (!row.joinedAt) return '—'
    const end = row.leftAt ? new Date(row.leftAt) : new Date()
    const mins = Math.max(0, Math.round((end - new Date(row.joinedAt)) / 60000))
    if (mins < 60) return `${mins}m`
    return `${Math.floor(mins / 60)}h ${mins % 60}m`
  }

  const exportCsv = () => {
    const header = ['Name', 'Joined', 'Left', 'Duration', 'Answers', 'Of polls']
    const body = visible.map((r) => [
      r.participantName ?? '',
      r.joinedAt ?? '',
      r.leftAt ?? '',
      duration(r),
      r.answersCount ?? 0,
      pollCount,
    ])
    const csv = [header, ...body]
      .map((cols) => cols.map((c) => `"${String(c).replace(/"/g, '""')}"`).join(','))
      .join('\n')

    const url = URL.createObjectURL(new Blob([csv], { type: 'text/csv' }))
    const a = document.createElement('a')
    a.href = url
    a.download = `attendance-session-${sessionId}.csv`
    a.click()
    URL.revokeObjectURL(url)
  }

  if (loading) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <Loading />
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-background">
      <header className="bg-surface border-b border-border sticky top-0 z-10">
        <div className="max-w-6xl mx-auto px-4 py-4 flex items-center gap-4">
          <button
            onClick={() => navigate(`/host/session/${sessionId}`)}
            className="text-text-secondary hover:text-text-primary"
            aria-label="Back to session"
          >
            <ArrowLeft className="w-5 h-5" />
          </button>
          <div>
            <h1 className="font-semibold text-text-primary">Attendance</h1>
            <p className="text-sm text-text-secondary">{session?.title ?? `Session ${sessionId}`}</p>
          </div>
          {rows.length > 0 && (
            <Button variant="secondary" onClick={exportCsv} className="ml-auto">
              <Download className="w-4 h-4" /> Export CSV
            </Button>
          )}
        </div>
      </header>

      <main className="max-w-6xl mx-auto px-4 py-6 space-y-6">
        {error && (
          <Card><p className="text-center py-4 text-text-secondary">{error}</p></Card>
        )}

        {!error && (
          <>
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <Stat label="Attended" value={rows.length} />
              <Stat label="Polls run" value={pollCount} />
              <Stat
                label="Answered every poll"
                value={pollCount > 0 ? fullyEngaged : '—'}
              />
            </div>

            {rows.length === 0 ? (
              <EmptyState
                icon={<Users className="w-8 h-8" />}
                title="No attendance yet"
                description="Attendance is recorded automatically when participants answer a poll."
              />
            ) : (
              <Card>
                <div className="flex flex-col sm:flex-row gap-3 mb-4">
                  <div className="relative flex-1">
                    <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-text-secondary" />
                    <Input
                      value={query}
                      onChange={(e) => setQuery(e.target.value)}
                      placeholder="Search by name"
                      className="pl-9"
                    />
                  </div>
                  <div className="flex gap-2">
                    {[
                      ['joinedAt', 'Join time'],
                      ['name', 'Name'],
                      ['answers', 'Answers'],
                    ].map(([key, label]) => (
                      <button
                        key={key}
                        onClick={() => setSortBy(key)}
                        className={`px-3 py-2 rounded-lg text-sm border transition-colors ${
                          sortBy === key
                            ? 'bg-primary text-white border-primary'
                            : 'border-border text-text-secondary hover:text-text-primary'
                        }`}
                      >
                        {label}
                      </button>
                    ))}
                  </div>
                </div>

                <div className="overflow-x-auto">
                  <table className="w-full text-sm">
                    <thead>
                      <tr className="text-left text-text-secondary border-b border-border">
                        <th className="pb-2 font-medium">Participant</th>
                        <th className="pb-2 font-medium">Joined</th>
                        <th className="pb-2 font-medium">Duration</th>
                        <th className="pb-2 font-medium">Answers</th>
                      </tr>
                    </thead>
                    <tbody>
                      {visible.map((r) => {
                        const answered = r.answersCount ?? 0
                        const pct = pollCount > 0 ? Math.round((answered / pollCount) * 100) : 0
                        return (
                          <tr key={r.id} className="border-b border-border last:border-0">
                            <td className="py-3 text-text-primary">
                              {r.participantName || 'Anonymous'}
                              {r.leftAt && (
                                <span className="ml-2 text-xs text-text-secondary">left</span>
                              )}
                            </td>
                            <td className="py-3 text-text-secondary">{time(r.joinedAt)}</td>
                            <td className="py-3 text-text-secondary">{duration(r)}</td>
                            <td className="py-3">
                              <div className="flex items-center gap-2">
                                <span className="text-text-primary">
                                  {answered}{pollCount > 0 && ` / ${pollCount}`}
                                </span>
                                {pollCount > 0 && (
                                  <div className="w-16 h-1.5 rounded-full bg-border overflow-hidden">
                                    <div
                                      className="h-full bg-primary"
                                      style={{ width: `${pct}%` }}
                                    />
                                  </div>
                                )}
                              </div>
                            </td>
                          </tr>
                        )
                      })}
                    </tbody>
                  </table>
                  {visible.length === 0 && (
                    <p className="text-center py-6 text-text-secondary">No one matches that search.</p>
                  )}
                </div>
              </Card>
            )}
          </>
        )}
      </main>
    </div>
  )
}

function Stat({ label, value }) {
  return (
    <Card>
      <p className="text-text-secondary text-sm">{label}</p>
      <p className="text-2xl font-semibold text-text-primary mt-1">{value}</p>
    </Card>
  )
}

Stat.propTypes = {
  label: PropTypes.string.isRequired,
  value: PropTypes.oneOfType([PropTypes.number, PropTypes.string]).isRequired,
}

export default AttendancePage

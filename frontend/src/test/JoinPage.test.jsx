import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { MemoryRouter } from 'react-router-dom'
import JoinPage from '../pages/JoinPage'

// the page calls the API on submit — stub it so tests stay offline
vi.mock('../services/session', () => ({
  sessionService: { join: vi.fn() },
}))
// toast provider isn't mounted in these tests
vi.mock('../components/Toast', () => ({
  useToast: () => ({ show: vi.fn(), error: vi.fn(), success: vi.fn() }),
}))

import { sessionService } from '../services/session'

function renderAt(path = '/') {
  return render(
    <MemoryRouter initialEntries={[path]}>
      <JoinPage />
    </MemoryRouter>
  )
}

describe('JoinPage', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    localStorage.clear()
  })

  it('renders the join form', () => {
    renderAt()
    expect(screen.getByRole('button', { name: /join/i })).toBeInTheDocument()
  })

  it('pre-fills and upper-cases a join code from the URL', () => {
    renderAt('/?code=ab12cd')
    expect(screen.getByDisplayValue('AB12CD')).toBeInTheDocument()
  })

  it('refuses to submit without a join code', async () => {
    const user = userEvent.setup()
    renderAt()
    await user.click(screen.getByRole('button', { name: /join/i }))
    expect(await screen.findByText(/enter a join code/i)).toBeInTheDocument()
    expect(sessionService.join).not.toHaveBeenCalled()
  })

  it('refuses to submit without a display name', async () => {
    const user = userEvent.setup()
    renderAt('/?code=AB12CD')
    await user.click(screen.getByRole('button', { name: /join/i }))
    expect(await screen.findByText(/enter your name/i)).toBeInTheDocument()
    expect(sessionService.join).not.toHaveBeenCalled()
  })
})

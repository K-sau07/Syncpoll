// 404 not found page

import { useNavigate } from 'react-router-dom'
import { Home } from 'lucide-react'
import { Button, Card } from '../components/ui'

function NotFoundPage() {
  const navigate = useNavigate()
  
  return (
    <div className="min-h-screen bg-background flex items-center justify-center p-4">
      <Card className="max-w-md w-full text-center">
        <div className="py-8">
          <div className="text-6xl font-bold text-primary mb-4">404</div>
          <h1 className="text-xl font-semibold text-text-primary mb-2">
            Page not found
          </h1>
          <p className="text-text-secondary mb-6">
            The page you're looking for doesn't exist or has been moved.
          </p>
          <Button onClick={() => navigate('/')}>
            <Home className="w-4 h-4 mr-2" />
            Back to Home
          </Button>
        </div>
      </Card>
    </div>
  )
}

export default NotFoundPage

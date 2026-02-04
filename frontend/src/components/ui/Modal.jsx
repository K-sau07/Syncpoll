// reusable modal component

import { useEffect } from 'react'
import { X } from 'lucide-react'
import Card from './Card'

function Modal({ 
  isOpen, 
  onClose, 
  title, 
  children,
  size = 'md',
  showClose = true 
}) {
  const sizes = {
    sm: 'max-w-sm',
    md: 'max-w-md',
    lg: 'max-w-lg',
    xl: 'max-w-xl'
  }
  
  // close on escape key
  useEffect(() => {
    const handleEscape = (e) => {
      if (e.key === 'Escape') onClose()
    }
    
    if (isOpen) {
      document.addEventListener('keydown', handleEscape)
      document.body.style.overflow = 'hidden'
    }
    
    return () => {
      document.removeEventListener('keydown', handleEscape)
      document.body.style.overflow = 'unset'
    }
  }, [isOpen, onClose])
  
  if (!isOpen) return null
  
  return (
    <div 
      className="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50"
      onClick={(e) => {
        if (e.target === e.currentTarget) onClose()
      }}
    >
      <Card className={`w-full ${sizes[size]} max-h-[90vh] overflow-y-auto`}>
        <div className="flex items-center justify-between mb-4">
          <h3 className="text-lg font-semibold text-text-primary">{title}</h3>
          {showClose && (
            <button 
              onClick={onClose}
              className="text-text-secondary hover:text-text-primary p-1"
            >
              <X className="w-5 h-5" />
            </button>
          )}
        </div>
        {children}
      </Card>
    </div>
  )
}

export default Modal

// card component for containing content sections

function Card({ 
  children, 
  className = '',
  padding = 'md'
}) {
  const paddingSizes = {
    sm: 'p-4',
    md: 'p-6',
    lg: 'p-8',
    none: 'p-0'
  }
  
  return (
    <div 
      className={`
        bg-surface rounded-xl border border-border shadow-sm
        ${paddingSizes[padding]}
        ${className}
      `}
    >
      {children}
    </div>
  )
}

export default Card

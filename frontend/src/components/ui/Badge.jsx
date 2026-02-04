// badge component for status indicators

function Badge({ children, variant = 'default', className = '' }) {
  const variants = {
    default: 'bg-gray-100 text-text-secondary',
    primary: 'bg-primary/10 text-primary',
    success: 'bg-success/10 text-success',
    warning: 'bg-warning/10 text-warning',
    error: 'bg-error/10 text-error',
    live: 'bg-success/10 text-success'
  }
  
  return (
    <span className={`
      inline-flex items-center gap-1.5 px-2 py-0.5 rounded text-xs font-medium
      ${variants[variant]}
      ${className}
    `}>
      {variant === 'live' && (
        <span className="w-1.5 h-1.5 bg-success rounded-full animate-pulse" />
      )}
      {children}
    </span>
  )
}

export default Badge

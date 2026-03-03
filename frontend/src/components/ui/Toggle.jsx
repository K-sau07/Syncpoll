// toggle switch component

function Toggle({
  label,
  checked = false,
  onChange,
  disabled = false,
  name,
  id,
  className = ''
}) {
  const inputId = id || name
  
  return (
    <label 
      htmlFor={inputId}
      className={`
        flex items-center gap-3 cursor-pointer
        ${disabled ? 'opacity-50 cursor-not-allowed' : ''}
        ${className}
      `}
    >
      <div className="relative">
        <input
          type="checkbox"
          id={inputId}
          name={name}
          checked={checked}
          onChange={onChange}
          disabled={disabled}
          className="sr-only"
        />
        <div className={`
          w-11 h-6 rounded-full transition-colors
          ${checked ? 'bg-primary' : 'bg-gray-200'}
        `}>
          <div className={`
            w-5 h-5 rounded-full bg-white shadow-sm transition-transform
            absolute top-0.5 left-0.5
            ${checked ? 'translate-x-5' : 'translate-x-0'}
          `} />
        </div>
      </div>
      {label && (
        <span className="text-sm text-text-primary">{label}</span>
      )}
    </label>
  )
}

export default Toggle

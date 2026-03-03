// checkbox component

import { Check } from 'lucide-react'

function Checkbox({
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
          w-5 h-5 rounded border-2 flex items-center justify-center transition-colors
          ${checked 
            ? 'bg-primary border-primary' 
            : 'bg-white border-border hover:border-primary/50'}
        `}>
          {checked && <Check className="w-3 h-3 text-white" />}
        </div>
      </div>
      {label && (
        <span className="text-sm text-text-primary">{label}</span>
      )}
    </label>
  )
}

export default Checkbox

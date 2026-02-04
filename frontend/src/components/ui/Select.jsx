// select dropdown component

import { ChevronDown } from 'lucide-react'

function Select({
  label,
  options = [],
  value,
  onChange,
  error,
  disabled = false,
  required = false,
  name,
  id,
  placeholder = 'Select an option',
  className = ''
}) {
  const inputId = id || name
  
  return (
    <div className={`flex flex-col gap-1.5 ${className}`}>
      {label && (
        <label 
          htmlFor={inputId}
          className="text-sm font-medium text-text-primary"
        >
          {label}
          {required && <span className="text-error ml-1">*</span>}
        </label>
      )}
      <div className="relative">
        <select
          id={inputId}
          name={name}
          value={value}
          onChange={onChange}
          disabled={disabled}
          required={required}
          className={`
            w-full px-4 py-2.5 rounded-lg border bg-white text-text-primary
            appearance-none cursor-pointer
            focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent
            disabled:bg-gray-100 disabled:cursor-not-allowed
            ${!value ? 'text-text-secondary' : ''}
            ${error ? 'border-error' : 'border-border'}
          `}
        >
          <option value="" disabled>{placeholder}</option>
          {options.map((option) => (
            <option 
              key={option.value} 
              value={option.value}
            >
              {option.label}
            </option>
          ))}
        </select>
        <ChevronDown className="absolute right-3 top-1/2 -translate-y-1/2 w-4 h-4 text-text-secondary pointer-events-none" />
      </div>
      {error && (
        <span className="text-sm text-error">{error}</span>
      )}
    </div>
  )
}

export default Select

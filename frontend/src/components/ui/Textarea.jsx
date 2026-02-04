// textarea component with label and error states

function Textarea({
  label,
  placeholder,
  value,
  onChange,
  error,
  disabled = false,
  required = false,
  name,
  id,
  rows = 4,
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
      <textarea
        id={inputId}
        name={name}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        disabled={disabled}
        required={required}
        rows={rows}
        className={`
          px-4 py-2.5 rounded-lg border bg-white text-text-primary
          placeholder:text-text-secondary resize-none
          focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent
          disabled:bg-gray-100 disabled:cursor-not-allowed
          ${error ? 'border-error' : 'border-border'}
        `}
      />
      {error && (
        <span className="text-sm text-error">{error}</span>
      )}
    </div>
  )
}

export default Textarea

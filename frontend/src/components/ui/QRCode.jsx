// qr code display component using external api

function QRCode({ value, size = 200, className = '' }) {
  // using qr server api for simplicity - no extra deps needed
  const qrUrl = `https://api.qrserver.com/v1/create-qr-code/?size=${size}x${size}&data=${encodeURIComponent(value)}`
  
  return (
    <div className={`flex flex-col items-center ${className}`}>
      <img 
        src={qrUrl} 
        alt="QR Code" 
        width={size} 
        height={size}
        className="rounded-lg"
      />
    </div>
  )
}

export default QRCode

# Payload Encryption Demo

A demo project showcasing frontend-backend payload encryption using AES-GCM + RSA with key rotation.

For full architectural flow and methodology, see the blog: [Insert Blog URL Here]

🚀 Getting Started
Frontend
```bash
cd frontend
npm install
npm run dev
```

Backend
```bash
cd backend
# For Spring Boot
./mvnw spring-boot:run
# or
./gradlew bootRun
```

### 📝 Functionality

Frontend sends text input encrypted with AES-GCM + RSA.

Backend decrypts, processes the text (converts to uppercase), re-encrypts, and sends it back.

Frontend decrypts and displays the response.

### 📸 Screenshot


# Payload Encryption Demo

A demo project showcasing frontend-backend payload encryption using AES-GCM + RSA with key rotation.

For full architectural flow and methodology, see the blog on [Medium](https://medium.com/@himanshu14sept/from-theory-to-practice-implementing-hybrid-encryption-cfce6949c6ee)

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
#### 1. Public key fetched from backend on mount
<img width="600" height="501" alt="image" src="https://github.com/user-attachments/assets/8709d61f-5f46-4eae-a2b8-4794e1459176" />

#### 2. Frontend payload and backend response displayed on frontend.
<img width="1365" height="719" alt="image" src="https://github.com/user-attachments/assets/a70cde9f-a017-405f-b702-8f3381244644" />


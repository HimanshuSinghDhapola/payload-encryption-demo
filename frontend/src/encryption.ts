// Generate AES-key for payload encryption
async function generateKey(){
    return await window.crypto.subtle.generateKey(
        {
        name: "AES-GCM",
        length: 256
        },
        true,
        ["encrypt", "decrypt"]
    )
}

interface JsonWebKeyWithKid extends JsonWebKey{
    kid: string;
}

// Convert JWK key to crypto key so that it can use for encryption
async function convertJwkToCryptoKey(key: JsonWebKeyWithKid): Promise<CryptoKey>{
    const cryptoKey =  await window.crypto.subtle.importKey(
        "jwk",
        key,
        {
            name: "RSA-OAEP",
            hash: "SHA-256"
        },
        true,
        ["encrypt"]
    );

    (cryptoKey as any).keyId = key.kid;

    return cryptoKey;
}

// Encrypt AES key with RSA
async function encryptAesKeyWithRsaOaep(
  rsaPublicKey: CryptoKey,
  aesRawKey: ArrayBuffer
): Promise<ArrayBuffer> {
  return window.crypto.subtle.encrypt(
    {
      name: "RSA-OAEP",
    },
    rsaPublicKey,
    aesRawKey
  );
}

async function encryptKey(publicKey: CryptoKey, aesKey: CryptoKey): Promise<ArrayBuffer>{
    // Convert aesKey to array-buffer because RSA can encrypt only readable string and CryptoKey is an object
    const aesRawKey = await window.crypto.subtle.exportKey("raw", aesKey);
    
    // Encrypt key with RSA
    const encryptedAesKey = await encryptAesKeyWithRsaOaep(publicKey, aesRawKey);

    return encryptedAesKey
}

// Encrypt the payload using AES-key
async function encrypt(key: CryptoKey, data: string) : Promise<string>{
    const encoder = new TextEncoder();
    const encodedData = encoder.encode(data);

    // Generate random IV vector (12-bytes as AES-GCM is used)
    const iv = window.crypto.getRandomValues(new Uint8Array(12));

    // Encrypt data
    const encryptedData = await window.crypto.subtle.encrypt(
        {
            name: "AES-GCM",
            iv: iv,
        },
        key,
        encodedData
    )

    const combinedDataLength = encryptedData.byteLength + iv.length;
    const combinedData = new Uint8Array(combinedDataLength);
    combinedData.set(iv);
    combinedData.set(new Uint8Array(encryptedData), iv.length);

    // Convert Uint8Array to base64 and return
    return toBase64(combinedData);
}

async function decrypt(key: CryptoKey, data: string): Promise<ArrayBuffer>{
    const combinedData = base64ToArrayBuffer(data);

  // Extract IV (first 12 bytes) and encrypted content
  const iv = combinedData.slice(0, 12);
  const encryptedData = combinedData.slice(12);

  // Decrypt the data
  const decryptedBuffer = await window.crypto.subtle.decrypt(
    {
      name: "AES-GCM",
      iv: iv,
    },
    key,
    encryptedData
  );

  return decryptedBuffer;
}

// Function to convert ArrayBuffer or Uint8Array to base64
function toBase64(buffer: ArrayBuffer | Uint8Array): string{
    // If input is Arraybuffer, create Uint8Array view for byte access
    const bytes = buffer instanceof Uint8Array ? buffer : new Uint8Array(buffer);
    let binary = "";
    bytes.forEach((byte) => (binary += String.fromCharCode(byte)));
    return window.btoa(binary);
}

// Function to convert base64 to arraybuffer
function base64ToArrayBuffer(base64: string): Uint8Array {
  const binary_string = window.atob(base64);
  const len = binary_string.length;
  const bytes = new Uint8Array(len);
  for (let i = 0; i < len; i++) {
    bytes[i] = binary_string.charCodeAt(i);
  }
  return bytes;
}
// Encrypt the key using RSA key

// Decrypt the response of BE

export {generateKey, encrypt, decrypt, encryptKey, convertJwkToCryptoKey, toBase64}
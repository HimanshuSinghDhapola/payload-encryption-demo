import type { AxiosRequestConfig } from "axios";
import { decrypt, encrypt, encryptKey, generateKey, toBase64 } from "./encryption";
import { getPublicKey } from "./util/keyStore";
import axios from "axios";

const BASE_URL = "http://localhost:8080";

interface RequestConfig{
    url: string;
    data: any;
    headers?: Record<string, string>;
    method: "POST" | "GET" | "PUT";
}

const decodeToJson = (data: ArrayBuffer): string => {
  let decoder = new TextDecoder("utf-8");
  const jsonString = decoder.decode(data);
  return JSON.parse(jsonString);
};


export async function request(config: RequestConfig){
    const publicKey = await getPublicKey();
    if(!publicKey) throw new Error("Public Key is not available");

    // Extract keyId
    const keyId = (publicKey as any).keyId;

    const key: CryptoKey = await generateKey();
    const stringifyData = JSON.stringify(config.data);
    const encryptedPayload: string = await encrypt(key, stringifyData);
    const encryptedKey: ArrayBuffer = await encryptKey(publicKey, key);
    const encryptedKeyBase64: string = toBase64(encryptedKey);
    
    const axiosConfig: AxiosRequestConfig = {
        baseURL: BASE_URL,
        url: config.url,
        method: config.method || "POST",
        headers: {
            "Content-Type": "application/json",
            ...(config.headers || {}),
        },
        data: {key: encryptedKeyBase64, data: encryptedPayload, keyId}
    }

    const response = await axios(axiosConfig);
    const decryptedData = await decrypt(key, response.data.data);
    response.data.data = decodeToJson(decryptedData);

    return response;
}
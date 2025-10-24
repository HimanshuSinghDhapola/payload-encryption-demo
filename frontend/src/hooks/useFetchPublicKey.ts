import { useMutation } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { convertJwkToCryptoKey } from "../encryption";

export default function useFetchPublicKey(){
    const [publicKey, setPublicKey] = useState<CryptoKey|undefined>();
    const [error, setError] = useState<String|undefined>();

    const fetchPublicKey = useMutation({
        mutationFn: async() => {
            const res = await fetch("http://localhost:8080/api/key/getPublicKey");
            if(!res.ok){
                throw new Error("Failed to fetch public key")
            }
            const jwk = await res.json();
            return convertJwkToCryptoKey(jwk);
        },
        onSuccess: (cryptoKey) => {
            setPublicKey(cryptoKey);
        },
        onError: (err) => {
            console.log(err);
            setError(err instanceof Error ? err.message : "Unknown error occured not able to fetch key");
        }
    })

    useEffect(()=> {
        fetchPublicKey.mutate();
    },[])

    return {publicKey, error};
}
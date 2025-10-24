let cryptoKey: CryptoKey | null = null;
let pendingResolvers: Array<(k: CryptoKey | null) => void> = [];

export function setPublicKey(key: CryptoKey) {
    cryptoKey = key;
    pendingResolvers.forEach((r) => r(cryptoKey));
    pendingResolvers = [];
}

export function getPublicKey(): Promise<CryptoKey | null> {
    if(cryptoKey) return Promise.resolve(cryptoKey);
    return new Promise((resolve) => pendingResolvers.push(resolve));
}

export function clearPublicKey() {
  cryptoKey = null;
  pendingResolvers.forEach((r) => r(null));
  pendingResolvers = [];
}
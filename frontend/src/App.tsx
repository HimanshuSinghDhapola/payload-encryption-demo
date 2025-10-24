import "./App.css";
import EncryptionDemo from "./EncryptionDemo";
import useFetchPublicKey from "./hooks/useFetchPublicKey";
import { setPublicKey } from "./util/keyStore";

function App() {
  const { publicKey, error } = useFetchPublicKey();

  if (error) {
    return (
      <div className="flex items-center justify-center">
        <h1>{error}</h1>
      </div>
    );
  }

  if (!publicKey) {
    return (
      <div className="flex items-center justify-center">
        <h1>Loading public key...</h1>
      </div>
    );
  }

  if(publicKey){
    setPublicKey(publicKey);
  }

  return <EncryptionDemo />
}

export default App;

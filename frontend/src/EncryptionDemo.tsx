import { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { request } from "./interceptor";

export default function EncryptionDemo() {
  const [message, setMessage] = useState("");
  const [resMessage, setResMessage] = useState("");

  const mutation = useMutation({
    mutationFn: async (msg: string) => 
       await request({
        url: "/api/encryption/process",
        data: { message: msg },
        method: "POST"
      }),
    onSuccess: (data)=>{
      setResMessage(data.data.data.message);
    },
    onError: (err) => {
      console.log(err);
    }
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!message.trim()) return;
    mutation.mutate(message);
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen p-6 bg-gray-50">
      <div className="w-full max-w-lg bg-white shadow-xl rounded-2xl p-6 space-y-5">
        <h1 className="text-2xl font-bold text-center mb-4 text-blue-700">
          🔐 Encryption Demo
        </h1>

        <form onSubmit={handleSubmit} className="flex flex-col space-y-4">
          <textarea
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            placeholder="Enter message to encrypt..."
            className="border border-gray-300 rounded-lg p-3 resize-none h-32 focus:outline-none focus:ring-2 focus:ring-blue-400"
          />
          <button
            type="submit"
            disabled={mutation.isPending || !message.trim()}
            className="bg-blue-600 hover:bg-blue-700 disabled:bg-gray-400 text-white font-semibold py-2 rounded-lg transition-colors"
          >
            {mutation.isPending ? "Encrypting..." : "Send Encrypted Message"}
          </button>
        </form>

        {mutation.isError && (
          <p className="text-red-500 text-center mt-3">
            {(mutation.error as Error)?.message || "Something went wrong"}
          </p>
        )}

        {mutation.isSuccess && (
          <div className="border-t pt-4 space-y-4">
            <div>
              <h2 className="font-semibold text-gray-700">
                Encrypted Payload Sent:
              </h2>
              <pre className="bg-gray-100 p-3 rounded-lg text-sm overflow-x-auto">
                (encrypted data sent to backend)
              </pre>
            </div>

            <div>
              <h2 className="font-semibold text-gray-700">
                Decrypted Response:
              </h2>
              <pre className="bg-green-50 p-3 rounded-lg text-sm text-green-700 overflow-x-auto">
                {resMessage}
              </pre>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

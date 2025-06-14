import { useAuth } from "@clerk/clerk-react";
import { useCallback } from "react";

// remove trailing slash if it exists
const API_URL: string = import.meta.env.VITE_BACKEND_URL.endsWith("/")
  ? import.meta.env.VITE_BACKEND_URL.slice(0, -1)
  : import.meta.env.VITE_BACKEND_URL;

export const useApi = () => {
  const { getToken } = useAuth();

  const fetchWithAuth = useCallback(
    async (url: string, options: RequestInit = {}) => {
      const token = await getToken();
      const headers = {
        ...options.headers,
        Authorization: `Bearer ${token}`
      };

      return fetch(API_URL + url, { ...options, headers });
    },
    [getToken]
  );

  const fetchNoAuth = async (url: string, options: RequestInit = {}) => {
    return fetch(API_URL + url, { ...options });
  };

  const fetchOpenAI = async (url: string, options: RequestInit = {}) => {
    const token = import.meta.env.VITE_OPENAI_API_KEY;
    const headers = {
      ...options.headers,
      Authorization: `Bearer ${token}`
    };

    return fetch(url, { ...options, headers });
  };

  const get = useCallback(
    (url: string) => fetchWithAuth(url).then((res) => res.json()),
    [fetchWithAuth]
  );

  const post = useCallback(
    (url: string, data: unknown) =>
      fetchWithAuth(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
      }).then((res) => res.json()),
    [fetchWithAuth]
  );

  const postNoRes = useCallback(
    (url: string, data: unknown) =>
      fetchWithAuth(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
      }),
    [fetchWithAuth]
  );

  const put = useCallback(
    (url: string, data: unknown) =>
      fetchWithAuth(url, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
      }).then((res) => res.json()),
    [fetchWithAuth]
  );

  const putNoRes = useCallback(
    (url: string, data: unknown) =>
      fetchWithAuth(url, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
      }),
    [fetchWithAuth]
  );

  const patch = useCallback(
    (url: string, data: unknown) =>
      fetchWithAuth(url, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
      })
        .then(async (res) => {
          if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
          const text = await res.text();
          return text ? JSON.parse(text) : null;
        })
        .catch((err) => {
          console.error("Patch error:", err);
          return null;
        }),
    [fetchWithAuth]
  );

  const del = useCallback(
    (url: string) => fetchWithAuth(url, { method: "DELETE" }),
    [fetchWithAuth]
  );

  const postOpenAI = useCallback(
    async (url: string, data: unknown, onChunk?: (chunk: string) => void) => {
      const response = await fetchOpenAI(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
      });

      if (!response.ok || !response.body) {
        throw new Error("OpenAI API request failed");
      }

      const reader = response.body.getReader();
      const decoder = new TextDecoder("utf-8");
      let result = "";

      while (true) {
        const { done, value } = await reader.read();
        if (done) break;

        const chunk = decoder.decode(value, { stream: true });
        const lines = chunk.split("\n").filter((line) => line.trim().startsWith("data: "));

        for (const line of lines) {
          const data = line.replace(/^data: /, "").trim();
          if (data === "[DONE]") return result;

          try {
            const parsed = JSON.parse(data);
            const content = parsed.choices?.[0]?.delta?.content || "";

            result += content;
            if (onChunk) onChunk(content);
          } catch {
            console.warn("Failed to parse chunk:", data);
          }
        }
      }

      return result;
    },
    [fetchOpenAI]
  );

  return {
    fetchWithAuth,
    fetchNoAuth,
    get,
    put,
    putNoRes,
    post,
    postNoRes,
    del,
    patch,
    postOpenAI,
    api_url: API_URL
  };
};

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
    const token = "";
    console.log("token", token);
    console.log("all env vars", import.meta.env);
    const headers = {
      ...options.headers,
      Authorization: `Bearer ${token}`
    };

    return fetch(url, { ...options, headers });
  }

  const get = useCallback(
    (url: string) =>
      fetchWithAuth(url)
        .then((res) => res.json())
        .catch(console.error),
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
      })
        .then(res => res.json())
        .catch(console.error)
      ,
    [fetchWithAuth]
  );

  const put = useCallback(
    (url: string, data: unknown) =>
      fetchWithAuth(url, {
        method: "PUT",
        body: JSON.stringify(data)
      })
        .then((res) => res.json())
        .catch(console.error),
    [fetchWithAuth]
  );

  const del = useCallback(
    (url: string) => fetchWithAuth(url, { method: "DELETE" }).catch(console.error),
    [fetchWithAuth]
  );

  const postOpenAI = useCallback(
    (url: string, data: unknown) =>
      fetchOpenAI(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
      })
        .then(res => res.json())
        .catch(console.error),
    [fetchOpenAI]
  );

  return { fetchWithAuth, fetchNoAuth, get, put, post, del, postOpenAI, api_url: API_URL };
};
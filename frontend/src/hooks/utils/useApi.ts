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

  const del = useCallback(
    (url: string) => fetchWithAuth(url, { method: "DELETE" }),
    [fetchWithAuth]
  );

  return { fetchWithAuth, fetchNoAuth, get, put, putNoRes, post, postNoRes, del, api_url: API_URL };
};

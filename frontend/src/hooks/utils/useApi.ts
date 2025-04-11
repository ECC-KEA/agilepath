import { useAuth } from "@clerk/clerk-react";
import { useCallback } from "react";

const API_URL = import.meta.env.VITE_BACKEND_URL;

export const useApi = () => {
  const { getToken } = useAuth();

  const fetchWithAuth = useCallback(
    async (url: string, options: RequestInit = {}) => {
      const token = await getToken();
      const headers = {
        ...options.headers,
        Authorization: `Bearer ${token}`
      };

      return fetch(url, { ...options, headers });
    },
    [getToken]
  );

  return { fetchWithAuth, api_url: API_URL };
};

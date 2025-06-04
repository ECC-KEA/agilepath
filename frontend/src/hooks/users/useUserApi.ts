import { useApi } from "../utils/useApi";

export default function useUserApi() {
  const { get } = useApi();

  const searchUsers = (search: string) => {
    return get(`/users?q=${search}`);
  };

  const getUser = (id: string) => {
    return get(`/users/${id}`);
  };

  return {
    searchUsers,
    getUser
  };
}

import { useApi } from "../utils/useApi";

export default function useUserApi() {
  const { get } = useApi();

  const searchUsers = (search: string) => {
    return get(`/users?q=${search}`);
  };

  return {
    searchUsers
  };
}

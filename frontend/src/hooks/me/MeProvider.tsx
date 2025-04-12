import { PropsWithChildren, useEffect, useMemo, useState } from "react";
import { IUser } from "../../types/user.types";
import { useApi } from "../utils/useApi";
import MeContext from "./MeContext";

function MeProvider({ children }: Readonly<PropsWithChildren>) {
  const { fetchWithAuth } = useApi();
  const [_me, setMe] = useState<IUser>();

  const me = useMemo(() => _me, [_me]);

  useEffect(() => {
    fetchWithAuth("/auth/profile")
      .then((res) => res.json())
      .then(setMe);
  }, [fetchWithAuth]);

  return (
    <MeContext.Provider
      value={{
        me
      }}
    >
      {children}
    </MeContext.Provider>
  );
}

export default MeProvider;

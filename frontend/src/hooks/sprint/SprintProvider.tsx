import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import { ISprint } from "../../types/sprint.types";
import { useApi } from "../utils/useApi";
import SprintContext from "./SprintContext";
import { useLoading } from "../utils/loading/useLoading";

interface SprintProviderProps extends PropsWithChildren {
  sprintId: string;
}

function SprintProvider({ children, sprintId }: SprintProviderProps) {
  const loader = useLoading();
  const { get } = useApi();
  const [_sprint, setSprint] = useState<ISprint>();

  const sprint = useMemo(() => _sprint, [_sprint]);

  const loadSprint = useCallback(async () => {
    loader.add();
    return get(`/sprints/${sprintId}`).then(setSprint).finally(loader.done);
  }, [get, sprintId]);

  useEffect(() => {
    loadSprint();
  }, [loadSprint]);

  return (
    <SprintContext.Provider
      value={{
        sprint,
        sprintId
      }}
    >
      {children}
    </SprintContext.Provider>
  );
}

export default SprintProvider;

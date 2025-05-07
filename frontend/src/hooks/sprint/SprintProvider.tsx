import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import { ISprint } from "../../types/sprint.types";
import { useApi } from "../utils/useApi";
import SprintContext from "./SprintContext";
import { useLoading } from "../utils/loading/useLoading";

interface SprintProviderProps extends PropsWithChildren {
  sprintId: string;
}

function SprintProvider({ children, sprintId }: Readonly<SprintProviderProps>) {
  const loader = useLoading();
  const { get } = useApi();
  const [sprint, setSprint] = useState<ISprint>();


  const loadSprint = useCallback(async () => {
    loader.add();
    return get(`/sprints/${sprintId}`).then(setSprint).finally(loader.done);
  }, [get, sprintId]);

  useEffect(() => {
    loadSprint();
  }, [loadSprint]);

  const contextValue = useMemo(() => ({
    sprint,
    sprintId
  }), [sprint, sprintId]);

  return (
    <SprintContext.Provider value={contextValue}>
      {children}
    </SprintContext.Provider>
  );
}

export default SprintProvider;

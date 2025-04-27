import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import { ISprint } from "../../types/sprint.types";
import { useApi } from "../utils/useApi";
import SprintContext from "./SprintContext";

interface SprintProviderProps extends PropsWithChildren {
  sprintId: string;
}

function SprintProvider({ children, sprintId }: SprintProviderProps) {
  const { get } = useApi();
  const [_sprint, setSprint] = useState<ISprint>();
  
  const sprint = useMemo(() => _sprint, [_sprint]);

  const loadSprint = useCallback(async () => {
    const sprintData = await get(`/sprints/${sprintId}`);
    console.log("Sprint Data: ", sprintData);
    if (!sprintData || !sprintData.id) {
      throw new Error("Failed to fetch sprint data or malformed data");
    }
    setSprint(sprintData);
  }, [get, sprintId]);

  useEffect(() => {
    loadSprint();
  }, [loadSprint]);

  return (
    <SprintContext.Provider
      value={{
        sprint,
        sprintId,
      }}
    >
      {children}
    </SprintContext.Provider>
  );
}

export default SprintProvider;
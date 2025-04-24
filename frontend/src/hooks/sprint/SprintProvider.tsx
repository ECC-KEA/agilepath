import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import { ISprint } from "../../types/sprint.types";
import { IColumn } from "../../types/column.types";
import { useApi } from "../utils/useApi";
import SprintContext from "./SprintContext";

interface SprintProviderProps extends PropsWithChildren {
  sprintId: string;
}

function SprintProvider({ children, sprintId }: SprintProviderProps) {
  const { get } = useApi();
  const [_sprint, setSprint] = useState<ISprint>();
  const [_columns, setColumns] = useState<IColumn[]>();
  
  const sprint = useMemo(() => _sprint, [_sprint]);
  const columns = useMemo(() => _columns, [_columns]);

  const loadSprint = useCallback(async () => {
    const sprintData = await get(`/sprints/${sprintId}`);
    console.log("Sprint Data: ", sprintData);
    if (!sprintData || !sprintData.id) {
      throw new Error("Failed to fetch sprint data or malformed data");
    }
    setSprint(sprintData);
  }, [get, sprintId]);

  const loadColumns = useCallback(async () => {
    const columnsData = await get(`/sprints/${sprintId}/sprint-columns`);
    console.log("Columns Data: ", columnsData);
    if (!columnsData) {
      throw new Error("Failed to fetch columns data or malformed data");
    }
    setColumns(columnsData[1]);
  }, [get, sprintId]);

  useEffect(() => {
    loadSprint();
  }, [loadSprint]);

  useEffect(() => {
    loadColumns();
  }, [loadColumns]);

  return (
    <SprintContext.Provider
      value={{
        sprint,
        columns
      }}
    >
      {children}
    </SprintContext.Provider>
  );
}

export default SprintProvider;
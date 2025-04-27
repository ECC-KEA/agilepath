import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import { IColumn, INewColumn } from "../../types/column.types";
import { useApi } from "../utils/useApi";
import ColumnContext from "./ColumnContext";

interface ColumnProviderProps extends PropsWithChildren {
  sprintId: string;
}

function ColumnProvider({ children, sprintId }: ColumnProviderProps) {
  const { get, post, del } = useApi();
  const [_columns, setColumns] = useState<IColumn[]>();
  
  const columns = useMemo(() => _columns, [_columns]);

  const loadColumns = useCallback(async () => {
    const columnsData = await get(`/sprints/${sprintId}/sprint-columns`);
    console.log("Columns Data: ", columnsData);
    if (!columnsData) {
      throw new Error("Failed to fetch columns data or malformed data");
    }
    setColumns(columnsData[1]); //for nu, fix på backend efterfølgende
  }, [get, sprintId]);


  const createColumn = useCallback(async (newColumn: INewColumn) => {
    const response = await post(`/sprint-columns`, newColumn);
    
    console.log("Create Column Response: ", response);
    if (!response.ok) {
      throw new Error("Failed to create column or malformed data");
    }

    const createdColumn = await response.json();
    console.log("Created Column: ", createdColumn);
  
    setColumns((prevColumns) => [...(prevColumns || []), createdColumn]);
  }, [post]);

  const deleteColumn = useCallback(async (columnId: string) => {
    await del(`/sprint-columns/${columnId}`);
    setColumns((prevColumns) => prevColumns?.filter((column) => column.id !== columnId));
  }, [del]);

  useEffect(() => {
    loadColumns();
  }, [loadColumns]);


  return (
    <ColumnContext.Provider
      value={{
        columns,
        loadColumns,
        createColumn,
        deleteColumn,
      }}
    >
      {children}
    </ColumnContext.Provider>
  )
}

export default ColumnProvider;
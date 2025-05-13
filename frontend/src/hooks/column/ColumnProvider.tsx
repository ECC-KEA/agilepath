import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import { IColumn, INewColumn } from "../../types/column.types";
import { useApi } from "../utils/useApi";
import ColumnContext from "./ColumnContext";
import { useLoading } from "../utils/loading/useLoading";

interface ColumnProviderProps extends PropsWithChildren {
  sprintId: string | undefined;
}

function ColumnProvider({ children, sprintId }: Readonly<ColumnProviderProps>) {
  const loader = useLoading();
  const { get, post, del } = useApi();
  const [columns, setColumns] = useState<IColumn[]>([]);
  console.log("ColumnProvider", columns);

  const loadColumns = useCallback(async () => {
    loader.add();
    return get(`/sprints/${sprintId}/sprint-columns`).then(setColumns).finally(loader.done);
  }, [get, sprintId]);

  const createColumn = useCallback(
    async (newColumn: INewColumn) => {
      loader.add();
      return post(`/sprint-columns`, newColumn)
        .then((col) => setColumns((prevColumns) => [...(prevColumns || []), col]))
        .finally(loader.done);
    },
    [post, loader]
  );

  const deleteColumn = useCallback(
    async (columnId: string) => {
      loader.add();
      return del(`/sprint-columns/${columnId}`)
        .then(() =>
          setColumns((prevColumns) => prevColumns?.filter((column) => column.id !== columnId))
        )
        .finally(loader.done);
    },
    [del, loader]
  );

  useEffect(() => {
    loadColumns();
  }, [loadColumns]);

  const contextValue = useMemo(() => ({
    columns,
    loadColumns,
    createColumn,
    deleteColumn
  }), [columns, loadColumns, createColumn, deleteColumn]);

  return (
    <ColumnContext.Provider value={contextValue}>
      {children}
    </ColumnContext.Provider>
  );
}

export default ColumnProvider;

import { createContext } from "react";
import { IColumn, INewColumn } from "../../types/column.types";

interface IColumnContext {
  columns: IColumn[];
  loadColumns: () => Promise<void>;
  createColumn: (newColumn: INewColumn) => Promise<void>;
  deleteColumn: (columnId: string) => Promise<void>;
}

const ColumnContext = createContext<IColumnContext | undefined>(undefined);
export default ColumnContext;

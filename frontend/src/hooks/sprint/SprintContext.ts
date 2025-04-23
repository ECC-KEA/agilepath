import { createContext } from "react";
import { ISprint } from "../../types/sprint.types";
import { IColumn } from "../../types/column.types";

interface ISprintContext {
  sprint: ISprint | undefined;
  columns: IColumn[] | undefined;
}

const SprintContext = createContext<ISprintContext | undefined>(undefined);
export default SprintContext;
import { createContext } from "react";
import { ISprint } from "../../types/sprint.types";

interface ISprintContext {
  sprint: ISprint | undefined;
  sprintId: string;
}

const SprintContext = createContext<ISprintContext | undefined>(undefined);
export default SprintContext;

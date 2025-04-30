import { createContext } from "react";
import { IProject } from "../../types/project.types";
import { INewSprint, ISprint } from "../../types/sprint.types";

interface ICurrentProjectContext {
  project: IProject | undefined;
  sprints: ISprint[];
  addSprint: (newSprint: INewSprint) => Promise<void>;
}

const CurrentProjectContext = createContext<ICurrentProjectContext | undefined>(undefined);
export default CurrentProjectContext;

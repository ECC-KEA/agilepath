import { createContext } from "react";
import { IProject } from "../../types/project.types";
import { INewSprint, ISprint } from "../../types/sprint.types";
import { IUser } from "../../types/user.types";

interface ICurrentProjectContext {
  project: IProject | undefined;
  sprints: ISprint[];
  members: IUser[];
  owner: IUser | undefined;
  addSprint: (newSprint: INewSprint) => Promise<void>;
}

const CurrentProjectContext = createContext<ICurrentProjectContext | undefined>(undefined);
export default CurrentProjectContext;

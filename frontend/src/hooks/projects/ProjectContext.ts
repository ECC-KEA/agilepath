import { createContext } from "react";
import { INewProject, IProject } from "../../types/project.types";

interface IProjectContext {
  projects: IProject[];
  createProject: (newProj: INewProject) => Promise<void>;
}

const ProjectContext = createContext<IProjectContext | undefined>(undefined);
export default ProjectContext;

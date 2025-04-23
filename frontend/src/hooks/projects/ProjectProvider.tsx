import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import { INewProject, IProject } from "../../types/project.types";
import { useApi } from "../utils/useApi";
import ProjectContext from "./ProjectContext";

function ProjectProvider({ children }: Readonly<PropsWithChildren>) {
  const { get, post } = useApi();
  const [_projects, setProjects] = useState<IProject[]>([]);

  const projects = useMemo(() => _projects, [_projects]);

  const loadProjects = useCallback(() => {
    return get("/projects").then(setProjects).catch(console.log);
  }, [get]);

  const createProject = useCallback(
    (newProj: INewProject) => {
      return (
        post("/projects", newProj)
          // do nothing with response
          .then(() => {})
          .catch(console.error)
      );
    },
    [post]
  );

  useEffect(() => {
    loadProjects();
  }, [loadProjects]);

  return (
    <ProjectContext.Provider
      value={{
        projects,
        createProject
      }}
    >
      {children}
    </ProjectContext.Provider>
  );
}

export default ProjectProvider;

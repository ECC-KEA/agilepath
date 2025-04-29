import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import { INewProject, IProject } from "../../types/project.types";
import { useApi } from "../utils/useApi";
import ProjectContext from "./ProjectContext";
import toast from "react-hot-toast";
import { useLoading } from "../utils/loading/useLoading";

function ProjectProvider({ children }: Readonly<PropsWithChildren>) {
  const loader = useLoading();
  const { get, post } = useApi();
  const [_projects, setProjects] = useState<IProject[]>([]);

  const projects = useMemo(() => _projects, [_projects]);

  const loadProjects = useCallback(() => {
    loader.add();
    return get("/projects")
      .then(setProjects)
      .catch((e) => {
        toast.error(e);
      })
      .finally(loader.done);
  }, [get]);

  const createProject = useCallback(
    (newProj: INewProject) => {
      loader.add();
      return post("/projects", newProj)
        .then((proj) => setProjects([...projects, proj]))
        .catch((e) => {
          toast.error(e);
        })
        .finally(loader.done);
    },
    [post, loader]
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

import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import useProjects from "./useProjects";
import { INewSprint, ISprint } from "../../types/sprint.types";
import { useApi } from "../utils/useApi";
import CurrentProjectContext from "./CurrentProjectContext";
import { useParams } from "react-router";

function CurrentProjectProvider(props: Readonly<PropsWithChildren>) {
  const { projectID } = useParams();
  const { get, post } = useApi();
  const { projects } = useProjects();
  const project = useMemo(() => {
    return projects.find((p) => p.id === projectID);
  }, [projects, projectID]);
  const [sprints, setSprints] = useState<ISprint[]>([]);

  const getSprints = useCallback(() => {
    get(`/projects/${projectID}/sprints`).then(setSprints).catch(console.error);
  }, [projectID]);

  const addSprint = useCallback(
    (newSprint: INewSprint) => {
      return post("/sprints", newSprint)
        .then((sprint) => setSprints((prev) => [...prev, sprint]))
        .catch(console.error);
    },
    [projectID]
  );

  useEffect(() => {
    getSprints();
  }, [projectID]);

  return (
    <CurrentProjectContext.Provider
      value={{
        project,
        sprints,
        addSprint
      }}
    >
      {props.children}
    </CurrentProjectContext.Provider>
  );
}

export default CurrentProjectProvider;

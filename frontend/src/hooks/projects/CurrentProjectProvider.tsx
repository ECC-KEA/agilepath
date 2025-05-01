import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import useProjects from "./useProjects";
import { INewSprint, ISprint } from "../../types/sprint.types";
import { useApi } from "../utils/useApi";
import CurrentProjectContext from "./CurrentProjectContext";

interface CurrentProjectProviderProps extends PropsWithChildren {
  projectID: string;
}

function CurrentProjectProvider(props: Readonly<CurrentProjectProviderProps>) {
  const { get, post } = useApi();
  const { projects } = useProjects();
  const project = useMemo(() => {
    return projects.find((p) => p.id === props.projectID);
  }, [projects, props.projectID]);
  const [sprints, setSprints] = useState<ISprint[]>([]);

  const getSprints = useCallback(() => {
    get(`/projects/${props.projectID}/sprints`).then(setSprints).catch(console.error);
  }, [props.projectID]);

  const addSprint = useCallback(
    (newSprint: INewSprint) => {
      return post("/sprints", newSprint)
        .then((sprint) => setSprints((prev) => [...prev, sprint]))
        .catch(console.error);
    },
    [props.projectID]
  );

  useEffect(() => {
    getSprints();
  }, [props.projectID]);

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

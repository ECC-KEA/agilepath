import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import useProjects from "./useProjects";
import { INewSprint, ISprint } from "../../types/sprint.types";
import { useApi } from "../utils/useApi";
import CurrentProjectContext from "./CurrentProjectContext";
import { useParams } from "react-router";
import { IUser } from "../../types/user.types";
import { IAddMemberRequest, IProjectMember, MemberRole } from "../../types/project.types";

function CurrentProjectProvider(props: Readonly<PropsWithChildren>) {
  const { projectID } = useParams();
  const { get, post, del, postNoRes, putNoRes } = useApi();
  const { projects } = useProjects();
  const project = useMemo(() => {
    return projects.find((p) => p.id === projectID);
  }, [projects, projectID]);
  const [sprints, setSprints] = useState<ISprint[]>([]);
  const [members, setMembers] = useState<IProjectMember[]>([]);
  const [owner, setOwner] = useState<IUser>();

  const getSprints = useCallback(() => {
    get(`/projects/${projectID}/sprints`).then(setSprints).catch(console.error);
  }, [get, projectID]);

  const getMembers = useCallback(() => {
    get(`/projects/${projectID}/members`).then(setMembers).catch(console.error);
  }, [get, projectID]);

  const getOwner = useCallback(() => {
    get(`/users/${project?.createdBy}`).then(setOwner).catch(console.error);
  }, [get, project]);

  const addMember = useCallback(
    (user: IAddMemberRequest) => {
      return postNoRes(`/projects/${projectID}/members`, user).then(getMembers);
    },
    [postNoRes, projectID]
  );

  const editMemberRole = useCallback(
    (member: IProjectMember, newRole: MemberRole) => {
      return putNoRes(
        `/projects/${projectID}/members/${member.user.id}?role=${newRole}`,
        undefined
      ).then(getMembers);
    },
    [putNoRes, projectID]
  );

  const removeMember = useCallback(
    (member: IProjectMember) => {
      return del(`/projects/${projectID}/members/${member.user.id}`).then(getMembers);
    },
    [del, projectID]
  );

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
    getMembers();
    getOwner();
  }, [project]);

  const contextValue = useMemo(
    () => ({
      project,
      sprints,
      members,
      owner,
      addSprint,
      addMember,
      removeMember,
      editMemberRole
    }),
    [project, sprints, members, owner, addSprint, addMember, removeMember, editMemberRole]
  );

  return (
    <CurrentProjectContext.Provider value={contextValue}>
      {props.children}
    </CurrentProjectContext.Provider>
  );
}

export default CurrentProjectProvider;

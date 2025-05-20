import { createContext } from "react";
import { IAddMemberRequest, IProject, IProjectMember, MemberRole } from "../../types/project.types";
import { INewSprint, ISprint } from "../../types/sprint.types";
import { IUser } from "../../types/user.types";

interface ICurrentProjectContext {
  project: IProject | undefined;
  sprints: ISprint[];
  members: IProjectMember[];
  owner: IUser | undefined;
  addSprint: (newSprint: INewSprint) => Promise<unknown>;
  addMember: (newMember: IAddMemberRequest) => Promise<unknown>;
  removeMember: (member: IProjectMember) => Promise<unknown>;
  editMemberRole: (member: IProjectMember, role: MemberRole) => Promise<unknown>;
}

const CurrentProjectContext = createContext<ICurrentProjectContext | undefined>(undefined);
export default CurrentProjectContext;

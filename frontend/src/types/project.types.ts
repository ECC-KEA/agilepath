import { IUser } from "./user.types";

export interface IProject {
  id: string;
  name: string;
  description?: string;
  framework: Framework;
  createdBy?: string;
  createdAt: string;
  estimationMethod: EstimationMethod;
}

export interface INewProject {
  name: string;
  description?: string;
  framework: Framework;
  estimationMethod: EstimationMethod;
}

export enum EstimationMethod {
  STORY_POINTS = "STORY_POINTS",
  TSHIRT_SIZES = "TSHIRT_SIZES"
}

export enum Framework {
  SCRUM = "SCRUM",
  XP = "XP",
  NONE = "NONE"
}

export enum MemberRole {
  ADMIN = "ADMIN",
  OWNER = "OWNER",
  CONTRIBUTOR = "CONTRIBUTOR"
}

// higher means more permissions. i.e. an Admin has the same permissions as contributor but not the other way round
export const getRoleValue = (role: MemberRole) => {
  switch (role) {
    case MemberRole.ADMIN:
      return 1;
    case MemberRole.OWNER:
      return 2;
    case MemberRole.CONTRIBUTOR:
      return 0;
  }
};

export interface IAddMemberRequest {
  userId: string;
  role: MemberRole;
}

export interface IProjectMember {
  user: IUser;
  role: MemberRole;
}

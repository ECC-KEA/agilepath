export interface IProject {
  id: string;
  name: string;
  description?: string;
  framework: Framework;
  createdBy?: string;
  createdAt: string;
}

export interface INewProject {
  name: string;
  description?: string;
  framework: Framework;
}

export enum Framework {
  SCRUM = "SCRUM",
  XP = "XP",
  NONE = "NONE"
}

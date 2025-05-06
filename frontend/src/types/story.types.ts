import { IUser } from "./user.types";

export interface IStory {
  id: string;
  projectId: string;
  title: string;
  description: string;
  status: string;
  priority: number;
  comments: IComment[];
  tasks: ITask[];
  createdBy: string;
  modifiedBy: string;
  createdAt: string;
  modifiedAt: string;
}

export interface IComment {
  id: string;
  content: string;
  storyId: string;
  taskId: string;
  createdBy: string;
  modifiedBy: string;
  createdAt: string;
  modifiedAt: string;
}

export interface ITask {
  id: string;
  storyId: string;
  sprintColumnId: string;
  title: string;
  description: string;
  estimateTshirt?: TshirtEstimate;
  estimatePoints?: PointEstimate;
  comments: IComment[];
  subtasks: ISubTask[];
  assignees: IUser[];
}

export interface ISubTask {
  id: string;
  content: string;
  storyId: string;
  taskId: string;
  createdBy: string;
  modifiedBy: string;
  createdAt: string;
  modifiedAt: string;
}

export enum TshirtEstimate {
  XSMALL,
  SMALL,
  MEDIUM,
  LARGE,
  XLARGE
}

export enum PointEstimate {
  POINT_1 = 1,
  POINT_2 = 2,
  POINT_3 = 3,
  POINT_5 = 5,
  POINT_8 = 8,
  POINT_13 = 13,
  POINT_21 = 21
}

export interface INewStory {
  projectId: string;
  title: string;
  description?: string;
  status: string;
  priority: number;
}

export enum Status {
  DONE = "DONE",
  TODO = "TODO",
  ARCHIVED = "ARCHIVED"
}

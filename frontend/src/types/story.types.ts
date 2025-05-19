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

export interface ITaskRequest {
  storyId: string;
  sprintColumnId: string;
  title: string;
  description?: string;
  estimateTshirt?: TshirtEstimate;
  estimatePoints?: PointEstimate;
  assigneeIds: string[];
}

export function convertTaskToRequest(task: ITask): ITaskRequest {
  return {
    storyId: task.storyId,
    sprintColumnId: task.sprintColumnId,
    title: task.title,
    description: task.description,
    estimateTshirt: task.estimateTshirt,
    estimatePoints: task.estimatePoints,
    assigneeIds: task.assignees.map((a) => a.id)
  };
}

export interface ISubTask {
  id: string;
  taskId: string;
  title: string;
  description: string;
  isDone: boolean;
  createdBy: string;
  modifiedBy: string;
  createdAt: string;
  modifiedAt: string;
}

export interface INewSubTask {
  taskId: string;
  title: string;
  description: string;
  isDone: boolean;
}

export enum TshirtEstimate {
  XSMALL = "XSMALL",
  SMALL = "SMALL",
  MEDIUM = "MEDIUM",
  LARGE = "LARGE",
  XLARGE = "XLARGE"
}

export enum PointEstimate {
  POINT_1 = "POINT_1",
  POINT_2 = "POINT_2",
  POINT_3 = "POINT_3",
  POINT_5 = "POINT_5",
  POINT_8 = "POINT_8",
  POINT_13 = "POINT_13",
  POINT_21 = "POINT_21"
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
  ARCHIVED = "ARCHIVED",
  IN_PROGRESS = "IN_PROGRESS"
}
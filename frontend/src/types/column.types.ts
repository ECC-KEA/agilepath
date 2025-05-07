import { Status } from "./story.types";

export interface INewColumn {
  sprintId: string;
  name: string;
  columnStatus: Status;
  columnIndex: number;
}

export interface IColumn {
  id: string;
  sprintId: string;
  name: string;
  status: Status;
  columnIndex: number;
}

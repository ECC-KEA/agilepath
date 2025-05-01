export enum ColumnStatus {
  TODO = "TODO",
  IN_PROGRESS = "IN_PROGRESS",
  DONE = "DONE"
}

export interface INewColumn {
  sprintId: string;
  name: string;
  columnStatus: ColumnStatus;
  columnIndex: number;
}

export interface IColumn {
  id: string;
  sprintId: string;
  name: string;
  columnStatus: ColumnStatus;
  columnIndex: number;
}

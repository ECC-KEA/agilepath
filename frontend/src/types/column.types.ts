export enum ColumnStatus {
  TODO = 'TODO',
  IN_PROGRESS = 'IN_PROGRESS',
  DONE = 'DONE',
}

export interface INewColumn {
  name: string;
  status: ColumnStatus;
  sprintId: string;
  columnIndex: number;
}


export interface IColumn {
  id: string;
  sprintId: string;
  name: string;
  status: ColumnStatus;
  columnIndex: number;
}
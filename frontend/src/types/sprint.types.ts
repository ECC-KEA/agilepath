export interface ISprint {
  id: string; 
  projectId: string;
  name: string;
  goal?: string; 
  startDate: string;
  endDate: string;
  createdBy: string;
};

export interface INewSprint {
  projectId: string;
  name: string;
  goal?: string;
  startDate: string;
  endDate: string;
}
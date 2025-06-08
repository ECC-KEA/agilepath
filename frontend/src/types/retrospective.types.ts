export interface ITalkingPoint {
  prompt: string;
  response?: string | null;
}

export interface IRetrospective {
  id: string;
  sprintId: string;
  completedAt?: string | null;
  talkingPoints: ITalkingPoint[];
  teamMood?: string | null;
  keepDoing: string[];
  stopDoing: string[];
  startDoing: string[];
}

export interface INewRetrospective {
  sprintId: string;
  talkingPoints?: ITalkingPoint[];
  teamMood?: string | null;
  keepDoing?: string[];
  stopDoing?: string[];
  startDoing?: string[];
}
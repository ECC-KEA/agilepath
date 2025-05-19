import { createContext } from "react";
import { INewSubTask, ISubTask } from "../../types/story.types";

interface ISubTaskContext {
  subtasks: ISubTask[];
  createSubTask: (subtask: INewSubTask) => Promise<void>;
  updateSubTask: (subtask: ISubTask, id: string) => Promise<void>;
}

const SubTaskContext = createContext<ISubTaskContext | undefined>(undefined);
export default SubTaskContext;
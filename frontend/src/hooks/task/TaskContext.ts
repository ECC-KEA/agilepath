import { createContext } from "react";
import { ITaskRequest, ITask } from "../../types/story.types";

interface ITaskContext {
  tasks: ITask[];
  task: ITask | undefined;
  refreshTasks: () => void;
  createTask: (task: ITaskRequest) => Promise<void>;
  updateTask: (task: ITaskRequest, id: string) => Promise<void>;
}

const TaskContext = createContext<ITaskContext | undefined>(undefined);
export default TaskContext;

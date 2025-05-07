import { createContext } from "react";
import { INewTask, ITask } from "../../types/story.types";

interface ITaskContext {
  tasks: ITask[];
  createTask: (task: INewTask) => Promise<void>;
}

const TaskContext = createContext<ITaskContext | undefined>(undefined);
export default TaskContext;

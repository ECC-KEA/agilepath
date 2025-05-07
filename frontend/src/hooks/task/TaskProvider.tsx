import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import useColumn from "../column/useColumn";
import TaskContext from "./TaskContext";
import { INewTask, ITask } from "../../types/story.types";
import { useApi } from "../utils/useApi";

function TaskProvider({ children }: Readonly<PropsWithChildren>) {
  const { columns } = useColumn();
  const { get, post } = useApi();
  const [_tasks, setTasks] = useState<ITask[]>([]);

  const tasks = useMemo(() => _tasks, [_tasks]);

  const getAllSprintTasks = useCallback(() => {
    const t: ITask[] = [];
    Promise.all(
      columns.map((c) =>
        get(`/sprint-columns/${c.id}/tasks`)
          .then((res) => t.push(...res))
          .catch(console.error)
      )
    )
      .then(() => setTasks(t))
      .catch(console.error);
  }, [columns]);

  useEffect(() => {
    getAllSprintTasks();
  }, [columns]);

  const createTask = useCallback(
    (task: INewTask) => {
      return post("/tasks", task)
        .then((res) => setTasks((prev) => [...prev, res]))
        .catch(console.error);
    },
    [post]
  );

  return (
    <TaskContext.Provider
      value={{
        tasks,
        createTask
      }}
    >
      {children}
    </TaskContext.Provider>
  );
}

export default TaskProvider;

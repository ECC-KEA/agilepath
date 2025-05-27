import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import useColumn from "../column/useColumn";
import TaskContext from "./TaskContext";
import { ITaskRequest, ITask } from "../../types/story.types";
import { useApi } from "../utils/useApi";
import { useParams } from "react-router";

function TaskProvider({ children }: Readonly<PropsWithChildren>) {
  const { taskId } = useParams();
  const { columns } = useColumn();
  const { get, post, put } = useApi();
  const [tasks, setTasks] = useState<ITask[]>([]);

  const task = useMemo(() => {
    if (!taskId) return undefined;
    return tasks.find((s) => s.id === taskId);
  }, [tasks, taskId]);

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
    (task: ITaskRequest) => {
      return post("/tasks", task).then((res) => setTasks((prev) => [...prev, res]));
    },
    [post]
  );

  const updateTask = useCallback(
    (task: ITaskRequest, id: string) => {
      return put(`/tasks/${id}`, task).then((res) =>
        setTasks((prev) => prev.map((t) => (t.id === res.id ? res : t)))
      );
    },
    [put]
  );

  const contextValue = useMemo(
    () => ({
      tasks,
      task,
      createTask,
      updateTask
    }),
    [tasks, createTask, updateTask, task]
  );

  return <TaskContext.Provider value={contextValue}>
    {children}
  </TaskContext.Provider>;
}

export default TaskProvider;

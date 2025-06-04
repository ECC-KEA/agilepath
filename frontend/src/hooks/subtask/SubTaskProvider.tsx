import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import { useApi } from "../utils/useApi";
import { ISubTask, INewSubTask } from "../../types/story.types";
import { useLoading } from "../utils/loading/useLoading";
import { useParams } from "react-router";
import SubTaskContext from "./SubTaskContext";
import useTask from "../task/useTask";

function SubTaskProvider({ children }: Readonly<PropsWithChildren>) {
  const { taskId } = useParams();
  const { refreshTasks } = useTask();
  const loader = useLoading();
  const { get, post, patch } = useApi();
  const [subtasks, setSubtasks] = useState<ISubTask[]>([]);

  const loadSubtasks = useCallback(async () => {
    loader.add();
    return get(`/subtasks/task/${taskId}`).then(setSubtasks).finally(loader.done);
  }, [get, taskId]);

  useEffect(() => {
    loadSubtasks();
  }, [loadSubtasks]);

  const createSubTask = useCallback(
    (subtask: INewSubTask) => {
      return post("/subtasks", subtask)
        .then((res) => setSubtasks((prev) => [...prev, res]))
        .then(refreshTasks)
        .catch(console.error);
    },
    [post]
  );

  const toggleSubTaskDone = useCallback(
    (id: string) => {
      return patch(`/subtasks/${id}/toggle`, "")
        .then((res) => {
          if (!res) return;
          setSubtasks((prev) => prev.map((s) => (s.id === res.id ? res : s)));
        })
        .then(refreshTasks)
        .catch(console.error);
    },
    [patch]
  );

  const contextValue = useMemo(
    () => ({
      subtasks,
      taskId,
      createSubTask,
      toggleSubTaskDone
    }),
    [subtasks, createSubTask, toggleSubTaskDone, taskId]
  );

  return <SubTaskContext.Provider value={contextValue}>{children}</SubTaskContext.Provider>;
}

export default SubTaskProvider;

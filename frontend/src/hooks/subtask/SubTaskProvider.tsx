import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import { useApi } from "../utils/useApi";
import { ISubTask, INewSubTask } from "../../types/story.types";
import { useLoading } from "../utils/loading/useLoading";
import { useParams } from "react-router";
import SubTaskContext from "./SubTaskContext";


function SubTaskProvider({ children }: Readonly<PropsWithChildren>) 
{
  const { taskId } = useParams();
  const loader = useLoading();
  const { get, post, put } = useApi();
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
      .catch(console.error);
    }, [post]
  );

  const updateSubTask = useCallback(
    (subtask: ISubTask, id: string) => {
      return put(`/subtasks/${id}`, subtask)
        .then((res) => setSubtasks((prev) => prev.map((s) => (s.id === res.id ? res : s))))
        .catch(console.error);
    }, [put]
  );

  const contextValue = useMemo(
    () => ({
      subtasks,
      taskId,
      createSubTask,
      updateSubTask
    }),
    [subtasks, createSubTask, updateSubTask, taskId]
  );

  return (
    <SubTaskContext.Provider value={contextValue}>
      {children}
    </SubTaskContext.Provider>
  );
}

export default SubTaskProvider;
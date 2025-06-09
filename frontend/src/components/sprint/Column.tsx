import { IColumn } from "../../types/column.types";
import { FaPlus } from "react-icons/fa6";
import useColumn from "../../hooks/column/useColumn";
import { notifyError, notifySuccess } from "../../helpers/notify";
import useTask from "../../hooks/task/useTask";
import { useMemo, useState } from "react";
import TaskBox from "./TaskBox";
import Button from "../generic/buttons/Button";
import ShowIf from "../generic/ShowIf";
import ExistingTaskModal from "./ExistingTaskModal";
import NewTaskModal from "./NewTaskModal";
import { useDroppable } from "@dnd-kit/core";
import { taskSearchPredicate } from "../../helpers/taskHelpers";
import { PiTrash } from "react-icons/pi";

interface IColumnProps {
  column: IColumn;
  search: string;
}

export default function Column({ column, ...props }: IColumnProps) {
  const { setNodeRef } = useDroppable({ id: column.id });
  const { deleteColumn } = useColumn();
  const { tasks } = useTask();
  const [showAddExistingTaskModal, setShowAddExistingTaskModal] = useState<boolean>(false);
  const [showCreateNewTaskModal, setShowCreateNewTaskModal] = useState<boolean>(false);

  const colTasks = useMemo(() => {
    return tasks.filter(
      (t) => t.sprintColumnId === column.id && taskSearchPredicate(t, props.search)
    );
  }, [tasks, column, props.search]);

  const handleDeleteColumn = async () => {
    deleteColumn(column.id)
      .then(() => notifySuccess(`Successfully deleted "${column.name}"`))
      .catch(() => notifyError(`Error deleting column "${column.name}"`));
  };

  return (
    <div
      ref={setNodeRef}
      className="flex flex-col h-[calc(100vh-265px)] justify-between pb-2 flex-1 text-center border-ap-onyx-50/50 border rounded-md shadow-sm max-w-90 min-w-90 flex-shrink-0"
    >
      <div className="flex items-center justify-between p-2 border-b border-ap-onyx-50/20 bg-ap-onyx-50/10">
        <div className="font-semibold text-sm text-ap-onyx-300 ml-4">{column.name}</div>
        <div
          className="text-xl cursor-pointer hover:brightness-90"
          onClick={() => handleDeleteColumn()}
          title="Delete column"
        >
          <PiTrash className="text-ap-coral-500" />
        </div>
      </div>

      <div className="h-full p-2 overflow-y-auto flex flex-col items-center gap-2">
        {colTasks.map((t) => (
          <TaskBox
            key={t.id}
            task={t}
            column={column}
          />
        ))}
      </div>

      <div className="pt-2 border-t border-ap-onyx-50/50">
        <Button
          text={
            <span className="flex gap-1 items-center">
              <FaPlus className="text-ap-lavender-800" />
              Add Task
            </span>
          }
          onClick={() => setShowCreateNewTaskModal(true)}
          className="bg-white shadow border border-ap-onyx-50 px-8"
        />
      </div>
      <ShowIf if={showAddExistingTaskModal}>
        <ExistingTaskModal
          column={column}
          show={showAddExistingTaskModal}
          onClose={() => setShowAddExistingTaskModal(false)}
        />
      </ShowIf>
      <ShowIf if={showCreateNewTaskModal}>
        <NewTaskModal
          column={column}
          show={showCreateNewTaskModal}
          onClose={() => setShowCreateNewTaskModal(false)}
        />
      </ShowIf>
    </div>
  );
}
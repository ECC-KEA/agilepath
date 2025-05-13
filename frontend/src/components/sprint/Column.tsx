import { IColumn } from "../../types/column.types";
import { FaPlus } from "react-icons/fa6";
import { TiDelete } from "react-icons/ti";
import useColumn from "../../hooks/column/useColumn";
import { notifyError, notifySuccess } from "../../helpers/notify";
import useTask from "../../hooks/task/useTask";
import { useMemo, useState } from "react";
import TaskBox from "./TaskBox";
import Button from "../generic/buttons/Button";
import AddTaskPopper from "./AddTaskPopper";
import { useOutsideClick } from "../../hooks/utils/useOutsideClick";
import ShowIf from "../generic/ShowIf";
import ExistingTaskModal from "./ExistingTaskModal";
import NewTaskModal from "./NewTaskModal";
import { useDroppable } from "@dnd-kit/core";

interface IColumnProps {
  column: IColumn;
}

export default function Column({ column }: IColumnProps) {
  const { setNodeRef } = useDroppable({ id: column.id });
  const { deleteColumn } = useColumn();
  const { tasks } = useTask();
  const [showAddExistingTaskModal, setShowAddExistingTaskModal] = useState<boolean>(false);
  const [showCreateNewTaskModal, setShowCreateNewTaskModal] = useState<boolean>(false);

  const colTasks = useMemo(() => {
    return tasks.filter((t) => t.sprintColumnId === column.id);
  }, [tasks, column]);

  const handleDeleteColumn = async () => {
    deleteColumn(column.id)
      .then(() => notifySuccess(`Successfully deleted "${column.name}"`))
      .catch(() => notifyError(`Error deleting column "${column.name}"`));
  };

  return (
    <div
      ref={setNodeRef}
      className="flex flex-col h-[calc(100vh-250px)] justify-between pb-2 flex-1 text-center border-ap-onyx-200 border rounded-md shadow-sm shadow-ap-onyx-400 max-w-88 min-w-88 flex-shrink-0"
    >
      <div className="flex items-center justify-between p-2 border-b border-ap-onyx-50/50">
        <div className="w-8"></div>
        <div className="text-xl">{column.name}</div>
        <div
          className="text-3xl cursor-pointer"
          onClick={() => handleDeleteColumn()}
        >
          <TiDelete className="text-red-500" />
        </div>
      </div>

      <div className="h-full p-2 overflow-y-auto flex justify-center">
        {colTasks.map((t) => (
          <TaskBox
            key={t.id}
            task={t}
            column={column}
          />
        ))}
      </div>

      <div className="p-2 border-t border-ap-onyx-50/50">
        <AddTaskButton
          onAddExistingClick={() => setShowAddExistingTaskModal(true)}
          onCreateNewClick={() => setShowCreateNewTaskModal(true)}
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

interface AddTaskButtonProps {
  onCreateNewClick: () => void;
  onAddExistingClick: () => void;
}

function AddTaskButton(props: Readonly<AddTaskButtonProps>) {
  const [popperAnchorEl, setPopperAnchorEl] = useState<null | HTMLElement>(null);
  const outsideClickRef = useOutsideClick(() => setPopperAnchorEl(null));
  return (
    <div ref={outsideClickRef}>
      <Button
        text={
          <span className="flex gap-1 items-center">
            <FaPlus className="text-ap-lavender-800" />
            Add
          </span>
        }
        className="bg-white shadow border border-ap-onyx-50 px-4"
        onClick={(e) => setPopperAnchorEl(popperAnchorEl ? null : e.currentTarget)}
      />
      <AddTaskPopper
        anchorEl={popperAnchorEl}
        onAddExistingClick={props.onAddExistingClick}
        onCreateNewClick={props.onCreateNewClick}
      />
    </div>
  );
}

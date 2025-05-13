import { FaPlus } from "react-icons/fa6";
import ShowIf from "../components/generic/ShowIf";
import CreateColumnModal from "../components/sprint/CreateColumnModal";
import Column from "../components/sprint/Column";
import useColumn from "../hooks/column/useColumn";
import { useState } from "react";
import Input from "../components/generic/inputs/Input";
import { DndContext, DragEndEvent } from "@dnd-kit/core";
import useTask from "../hooks/task/useTask";
import { ITask, convertTaskToRequest } from "../types/story.types";
import { notifyError } from "../helpers/notify";

function SprintBoard() {
  const { columns } = useColumn();
  const { updateTask } = useTask();
  const [showCreateColumnModal, setShowCreateColumnModal] = useState(false);

  const handleTaskDragEnd = (e: DragEndEvent) => {
    const { active, over } = e;
    const task: ITask = active.data.current as ITask;

    if (over && task) {
      const tmp = convertTaskToRequest(task);
      tmp.sprintColumnId = over.id.toString();
      updateTask(tmp, task.id).catch(() => notifyError("Failed to move task"));
    }
  };

  return (
    <div className="sprint-board w-full">
      <div className="flex w-full h-full">
        <div className="flex flex-col w-full h-full p-4 gap-4">
          <Input
            className="w-72"
            placeholder="Search..."
          />
          <div className="flex gap-6 w-full max-h-full overflow-auto">
            <DndContext onDragEnd={handleTaskDragEnd}>
              {columns.map((column) => (
                <Column
                  column={column}
                  key={column.id}
                />
              ))}
            </DndContext>
            <FaPlus
              className="m-4 text-4xl text-ap-lavender-800 border-ap-onyx-200 border rounded-md p-2 cursor-pointer"
              onClick={() => setShowCreateColumnModal(true)}
            />
          </div>
        </div>
      </div>

      <ShowIf if={showCreateColumnModal}>
        <CreateColumnModal
          show={showCreateColumnModal}
          onClose={() => setShowCreateColumnModal(false)}
        />
      </ShowIf>
    </div>
  );
}

export default SprintBoard;

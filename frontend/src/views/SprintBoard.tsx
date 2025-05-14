import { FaPlus } from "react-icons/fa6";
import ShowIf from "../components/generic/ShowIf";
import CreateColumnModal from "../components/sprint/CreateColumnModal";
import Column from "../components/sprint/Column";
import useColumn from "../hooks/column/useColumn";
import { useState } from "react";
import Input from "../components/generic/inputs/Input";
import { DndContext, DragEndEvent, DragOverlay, DragStartEvent } from "@dnd-kit/core";
import useTask from "../hooks/task/useTask";
import { ITask, convertTaskToRequest } from "../types/story.types";
import { notifyError } from "../helpers/notify";
import TaskBox from "../components/sprint/TaskBox";

function SprintBoard() {
  const { columns } = useColumn();
  const { updateTask } = useTask();
  const [draggedTask, setDraggedTask] = useState<ITask | null>(null);
  const [showCreateColumnModal, setShowCreateColumnModal] = useState(false);
  const [search, setSearch] = useState<string>("");

  const handleTaskDragEnd = (e: DragEndEvent) => {
    const { active, over } = e;
    const task: ITask = active.data.current as ITask;

    if (over && task) {
      const tmp = convertTaskToRequest(task);
      tmp.sprintColumnId = over.id.toString();
      updateTask(tmp, task.id).catch(() => notifyError("Failed to move task"));
    }
    setDraggedTask(null);
  };
  const handleTaskDragStart = (e: DragStartEvent) => {
    const { active } = e;
    setDraggedTask((active.data.current as ITask) ?? null);
  };

  const handleTaskDragCancel = () => {
    setDraggedTask(null);
  };

  return (
    <div className="sprint-board w-full overflow-auto">
      <div className="flex w-full h-full">
        <div className="flex flex-col w-full h-full p-4 gap-4">
          <div className="flex items-center w-full justify-between">
            <Input
              className="w-72"
              placeholder="Search..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
            <FaPlus
              className="m-4 text-4xl text-ap-lavender-800 border-ap-onyx-200 border rounded-md p-2 cursor-pointer flex-shrink-0"
              onClick={() => setShowCreateColumnModal(true)}
            />
          </div>
          <div className="flex gap-6 w-full max-h-full overflow-auto relative">
            <DndContext
              onDragEnd={handleTaskDragEnd}
              onDragStart={handleTaskDragStart}
              onDragCancel={handleTaskDragCancel}
              // modifiers={[restrictToFirstScrollableAncestor]}
            >
              {columns.map((column) => (
                <Column
                  column={column}
                  key={column.id}
                />
              ))}
              <DragOverlay
                className="absolute"
                dropAnimation={null}
              >
                {!!draggedTask && (
                  <TaskBox
                    task={draggedTask}
                    column={columns.find((c) => c.id === draggedTask.sprintColumnId) ?? columns[0]}
                    isDragOverlay
                  />
                )}
              </DragOverlay>
            </DndContext>
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

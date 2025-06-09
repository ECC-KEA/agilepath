import { FaPlus } from "react-icons/fa6";
import ShowIf from "../components/generic/ShowIf";
import CreateColumnModal from "../components/sprint/CreateColumnModal";
import Column from "../components/sprint/Column";
import useColumn from "../hooks/column/useColumn";
import { useState } from "react";
import { DndContext, DragEndEvent, DragOverlay, DragStartEvent } from "@dnd-kit/core";
import useTask from "../hooks/task/useTask";
import { ITask, convertTaskToRequest } from "../types/story.types";
import { notifyError } from "../helpers/notify";
import TaskBox from "../components/sprint/TaskBox";
import SearchInput from "../components/generic/inputs/SearchInput";
import Button from "../components/generic/buttons/Button";
import { useNavigate, useLocation } from "react-router";
import useSprint from "../hooks/sprint/useSprint";
import EndSprintModal from "../components/sprint/EndSprintModal";

function SprintBoard() {
  const { sprint } = useSprint();
  const navigate = useNavigate();
  const location = useLocation();
  const { columns } = useColumn();
  const { updateTask } = useTask();
  const [showEndSprintModal, setShowEndSprintModal] = useState(false);
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

  const navigateToRetrospective = () => {
    const baseUrl = location.pathname.substring(0, location.pathname.lastIndexOf("/"));
    navigate(`${baseUrl}/retrospective`);
  }

  return (
    <div className="sprint-board w-full overflow-auto">
      <div className="flex w-full h-full">
        <div className="flex flex-col w-full h-full p-4 gap-4">
          <div className="flex items-center w-full justify-between">
            <SearchInput
              containerClassName="w-88"
              placeholder="Search by Task title, ID or assignee"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
            <div className="flex items-center">
              <Button
                text={
                  <div className="flex items-center gap-2">
                    <FaPlus className="text-ap-lavender-800"/> Add Column
                  </div>
                }
                className=" border-ap-onyx-200 border rounded-md p-2 cursor-pointer flex-shrink-0"
                onClick={() => setShowCreateColumnModal(true)}
              />
              <ShowIf if={(sprint?.endDate) ? new Date(sprint.endDate) < new Date() : false}>
                <Button 
                  text="Go to Retrospective"
                  className="m-4  border-ap-onyx-200 border rounded-md p-2 cursor-pointer flex-shrink-0"
                  onClick={() => navigateToRetrospective()}
                />
              </ShowIf>
              <ShowIf if={(sprint?.endDate) ? new Date(sprint.endDate) >= new Date() : true}>
                <Button
                  text="End Sprint"
                  className="m-4  border-ap-onyx-200 border rounded-md p-2 cursor-pointer flex-shrink-0"
                  onClick={() => setShowEndSprintModal(true)}
                />
              </ShowIf> 
            </div>
            
          </div>
          <div className="flex gap-6 w-full max-h-full overflow-auto relative">
            <DndContext
              onDragEnd={handleTaskDragEnd}
              onDragStart={handleTaskDragStart}
              onDragCancel={handleTaskDragCancel}
            >
              {columns.map((column) => (
                <Column
                  search={search}
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
      <ShowIf if={showEndSprintModal}>
        <EndSprintModal 
          show={showEndSprintModal}
          onClose={() => setShowEndSprintModal(false)}
        />
      </ShowIf>
    </div>
  );
}

export default SprintBoard;

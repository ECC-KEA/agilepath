import { useDraggable } from "@dnd-kit/core";
import { IColumn } from "../../types/column.types";
import { ITask } from "../../types/story.types";
import StatusLabel from "../status/StatusLabel";
import { FaGripLines } from "react-icons/fa6";
import { useNavigate, useLocation } from "react-router";

interface TaskBoxProps {
  task: ITask;
  column: IColumn;
  isDragOverlay?: boolean;
}

function TaskBox(props: Readonly<TaskBoxProps>) {
  const { attributes, listeners, setNodeRef, isDragging } = useDraggable({
    id: props.task.id,
    data: props.task
  });

  const navigate = useNavigate();
  const location = useLocation();

  const handleClick = () => {
    const segments = location.pathname.split('/');
    segments[segments.length - 1] = `edit/${props.task.id}`; 
    const newPath = segments.join('/');
    navigate(newPath);
  };

  return (
    <div
      className={`
        ${props.isDragOverlay ? "absolute backdrop-blur-2xl" : ""} 
        ${isDragging ? "opacity-50" : ""}
        p-2  rounded shadow w-80 h-28 bg-ap-onyx-50/20
      `}
      ref={setNodeRef}
      onClick={handleClick}
    >
      <div
        className={`${props.isDragOverlay ? "cursor-grabbing" : "cursor-grab"} flex items-center justify-center`}
        {...attributes}
        {...listeners}
      >
        <FaGripLines className="text-xl text-ap-onyx-50" />
      </div>
      <div className="flex justify-between items-center text-sm">
        <div className="truncate w-20">#{props.task.id}</div>
        <StatusLabel status={props.column.status} />
      </div>
      <div className="my-2 mx-4 max-w-64 line-clamp-2 text-left">{props.task.title}</div>
    </div>
  );
}
export default TaskBox;

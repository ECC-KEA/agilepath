import { useDraggable } from "@dnd-kit/core";
import { IColumn } from "../../types/column.types";
import { ITask } from "../../types/story.types";
import StatusLabel from "../status/StatusLabel";
import { CSS } from "@dnd-kit/utilities";
import { FaGripLines } from "react-icons/fa6";
interface TaskBoxProps {
  task: ITask;
  column: IColumn;
}
function TaskBox(props: Readonly<TaskBoxProps>) {
  const { attributes, listeners, setNodeRef, transform, isDragging } = useDraggable({
    id: props.task.id,
    data: props.task
  });

  const style = {
    transform: CSS.Translate.toString(transform)
  };
  return (
    <div
      className={`${isDragging ? "absolute" : ""} p-2 bg-ap-onyx-50/20 rounded shadow w-80 h-28`}
      ref={setNodeRef}
      style={style}
    >
      <div
        className={`${isDragging ? "cursor-grabbing" : "cursor-grab"} flex items-center justify-center`}
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

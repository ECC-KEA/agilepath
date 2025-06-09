import { useDraggable } from "@dnd-kit/core";
import { IColumn } from "../../types/column.types";
import { ITask } from "../../types/story.types";
import StatusLabel from "../status/StatusLabel";
import { FaGripLines } from "react-icons/fa6";
import { useNavigate, useLocation } from "react-router";
import { Avatar, AvatarGroup, LinearProgress } from "@mui/material";
import { useMemo } from "react";
import ShowIf from "../generic/ShowIf";

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

  const donePercentage = useMemo(() => {
    if (props.task.subtasks.length === 0) return 0;
    return (
      (props.task.subtasks.filter((st) => st.isDone).length / props.task.subtasks.length) * 100
    );
  }, [props.task]);

  const handleClick = () => {
    const segments = location.pathname.split("/");
    segments[segments.length - 1] = `edit/${props.task.id}`;
    const newPath = segments.join("/");
    navigate(newPath);
  };

  return (
    <div
      className={`
        ${props.isDragOverlay ? "absolute backdrop-blur-2xl" : ""} 
        ${isDragging ? "opacity-50" : ""}
        p-2  rounded shadow w-80 bg-ap-onyx-50/20 cursor-pointer
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
        <StatusLabel status={props.column.status} />
        <AvatarGroup
          max={3}
          spacing={"small"}
        >
          {props.task.assignees.map((a) => (
            <Avatar
              key={"assignee" + a.id}
              src={a.avatarUrl}
              sx={{ height: 28, width: 28 }}
            />
          ))}
        </AvatarGroup>
      </div>
      <div className="my-2 mx-4 max-w-64 line-clamp-2 text-left">{props.task.title}</div>
      <ShowIf if={props.task.subtasks.length > 0}>
        <div className="text-ap-lavender-300">
          <LinearProgress
            variant="determinate"
            value={donePercentage}
            color="inherit"
          />
        </div>
      </ShowIf>
    </div>
  );
}
export default TaskBox;

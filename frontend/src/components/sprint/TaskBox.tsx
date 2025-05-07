import { IColumn } from "../../types/column.types";
import { ITask } from "../../types/story.types";
import StatusLabel from "../status/StatusLabel";

interface TaskBoxProps {
  task: ITask;
  column: IColumn;
}
function TaskBox(props: Readonly<TaskBoxProps>) {
  return (
    <div className="p-2 bg-ap-onyx-50/20 rounded shadow">
      <div className="flex justify-between items-center text-sm">
        <div className="truncate w-20">#{props.task.id}</div>
        <StatusLabel status={props.column.status} />
      </div>
      <div className="my-2 mx-4">{props.task.title}</div>
    </div>
  );
}
export default TaskBox;

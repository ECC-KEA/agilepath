import { ISubTask } from "../../types/story.types";
import useSubTask from "../../hooks/subtask/useSubTask";
import Input from "../generic/inputs/Input";

interface SubTaskBoxProps {
  subtask: ISubTask;
}

function SubTaskBox(props: Readonly<SubTaskBoxProps>) {
  const { toggleSubTaskDone } = useSubTask();
  const handleCheck = () => {
    toggleSubTaskDone(props.subtask.id);
  };

  return (
    <div className="p-2 bg-white rounded shadow">
      <div className="flex items-center gap-2">
        <Input
          type="checkbox"
          checked={props.subtask.isDone}
          className="mr-2 accent-ap-lavender-900"
          onChange={handleCheck}
        />
        <div className="">{props.subtask.title}</div>
        <div>{props.subtask.description}</div>
      </div>
    </div>
  );
}

export default SubTaskBox;

import { ISubTask } from "../../types/story.types";
import useSubTask from "../../hooks/subtask/useSubTask";

interface SubTaskBoxProps {
  subtask: ISubTask;
}

function SubTaskBox(props: Readonly<SubTaskBoxProps>) {
  const { updateSubTask } = useSubTask();
  const handleCheck = () => {
    const updatedSubTask = {
      ...props.subtask,
      isDone: !props.subtask.isDone,
    };

    void updateSubTask(updatedSubTask, props.subtask.id)
      .then(() => {
        console.log("Subtask updated successfully");
      })
      .catch((error) => {
        console.error("Error updating subtask:", error);
      });
  }

  return (
    <div className="p-2 bg-white rounded shadow">
      <div className="flex items-center gap-2">
        <input
          type="checkbox"
          checked={props.subtask.isDone}
          className="mr-2"
          onChange={handleCheck}
        />
        <div className="font-bold">{props.subtask.title}</div>
        <div>{props.subtask.description}</div>
      </div>
    </div>
  );
}

export default SubTaskBox;
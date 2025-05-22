import { forwardRef, useImperativeHandle, useState } from "react";
import useTask from "../../hooks/task/useTask";
import { ITaskRequest  } from "../../types/story.types";
import { notifyError, notifySuccess } from "../../helpers/notify";
import Input from "../generic/inputs/Input";
import useStory from "../../hooks/story/useStory";
import useColumn from "../../hooks/column/useColumn"
import TextArea from "../generic/inputs/CustomTextArea";
import { TshirtEstimate } from "../../types/story.types";

interface TaskHandlerProps {
  show: boolean;
}

export interface TaskHandlerHandle {
  handleCreateTask: () => void;
}


const TaskHandler = forwardRef<TaskHandlerHandle, TaskHandlerProps>((props, ref) => {
  const { createTask } = useTask();
  const { story } = useStory();
  const { columns } = useColumn();

  const [title, setTitle] = useState<string>("");
  const [description, setDescription] = useState<string>("");

  useImperativeHandle(ref, () => ({
    handleCreateTask
  }));

  const handleCreateTask = () => {
    if (!story || columns.length === 0) return;

    const sprintColumnId = columns.find((c) => c.status === "TODO")?.id ?? columns[0].id;
    
    const tmp: ITaskRequest = {
      assigneeIds: [], // TODO: select m. members i projekt,
      sprintColumnId: sprintColumnId,
      storyId: story.id,
      title,
      description,
      estimateTshirt: TshirtEstimate.MEDIUM
    };
    void createTask(tmp)
      .then(() => notifySuccess("Successfully created task"))
      .catch(() => notifyError("Failed to create task"));
  };

  return (
      <div className="flex flex-col gap-4">
        <div className="flex flex-col">
          <div className="text-ap-onyx-200">Title</div>
          <Input
            type="text"
            placeholder="Task title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
        </div>
        <div>
          <div className="text-ap-onyx-400 flex justify-between">
            Description<div className="italic">Optional</div>
          </div>
          <TextArea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Task description"
            className="w-full"
          />
        </div>
      </div>
  );
})
export default TaskHandler;

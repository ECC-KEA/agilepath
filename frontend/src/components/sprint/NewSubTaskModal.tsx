import { ITask, INewSubTask } from "../../types/story.types";
import useSubTask from "../../hooks/subtask/useSubTask";
import Modal from "../generic/Modal";
import { useMemo, useState } from "react";
import Input from "../generic/inputs/Input";
import TextArea from "../generic/inputs/CustomTextArea";
import { notifyError, notifySuccess } from "../../helpers/notify";

interface NewSubTaskModalProps {
  task: ITask;
  show: boolean;
  onClose: () => void;
}

function NewSubTaskModal(props: Readonly<NewSubTaskModalProps>) {
  const { createSubTask } = useSubTask();
  const [title, setTitle] = useState<string>("");
  const [description, setDescription] = useState<string>("");

  const handleCreateSubTask = () => {
    const tmp: INewSubTask = {
      taskId: props.task.id,
      title,
      description,
      isDone: false
    };
    void createSubTask(tmp)
      .then(() => notifySuccess("Successfully created task"))
      .catch(() => notifyError("Failed to create task"));
    props.onClose();
  }

  const disableAction = useMemo(() => {
    return (title.length < 3 && title.length > 100)  || (description.length > 2000 && description == "");
  }, [title, description]);

  return (
    <Modal
      title="New Sub Task"
      show={props.show}
      onClose={props.onClose}
      actionText="Create"
      onAction={handleCreateSubTask}
      disableAction={disableAction}
    >
      <div className="flex flex-col gap-4 p-4">
        <div className="flex flex-col">
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
          <div className="text-ap-onyx-400 flex justify-between">Description</div>
          <TextArea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Task description"
            className="w-full"
          />
        </div>
        </div>
      </div>
    </Modal>
  )
}

export default NewSubTaskModal;
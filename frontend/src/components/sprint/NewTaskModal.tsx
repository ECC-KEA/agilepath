import { useMemo, useState } from "react";
import useTask from "../../hooks/task/useTask";
import { IColumn } from "../../types/column.types";
import { ITaskRequest } from "../../types/story.types";
import Modal from "../generic/Modal";
import { notifyError, notifySuccess } from "../../helpers/notify";
import Input from "../generic/inputs/Input";
import CustomSelect from "../generic/select/CustomSelect";
import useStory from "../../hooks/story/useStory";
import TextArea from "../generic/inputs/CustomTextArea";

interface NewTaskModalProps {
  column: IColumn;
  show: boolean;
  onClose: () => void;
}
function NewTaskModal(props: Readonly<NewTaskModalProps>) {
  const { createTask } = useTask();
  const { stories } = useStory();

  const [storyID, setStoryID] = useState<string>("");
  const [title, setTitle] = useState<string>("");
  const [description, setDescription] = useState<string>("");

  const storyOptions = useMemo(() => {
    return stories.map((s) => ({
      label: s.title,
      value: s.id
    }));
  }, [stories]);

  const disableAction = useMemo(() => {
    return storyID === "" || title === "";
  }, [storyID, title]);

  const handleCreateTask = () => {
    const tmp: ITaskRequest = {
      assigneeIds: [], // TODO: select m. members i projekt,
      sprintColumnId: props.column.id,
      storyId: storyID,
      title,
      description
    };
    void createTask(tmp)
      .then(() => notifySuccess("Successfully created task"))
      .catch(() => notifyError("Failed to create task"));
    props.onClose();
  };

  return (
    <Modal
      title="New Task"
      show={props.show}
      onClose={props.onClose}
      actionText="Create"
      onAction={handleCreateTask}
      disableAction={disableAction}
    >
      <div className="flex flex-col gap-4 p-4">
        <div className="flex flex-col">
          <div className="text-ap-onyx-200">Story</div>
          <CustomSelect
            options={storyOptions}
            value={storyOptions.find((o) => o.value === storyID)}
            onChange={(o) => {
              if (o) {
                setStoryID(o.value);
              }
            }}
          />
        </div>
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
    </Modal>
  );
}
export default NewTaskModal;

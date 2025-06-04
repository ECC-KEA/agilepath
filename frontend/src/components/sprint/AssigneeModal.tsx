import { Avatar } from "@mui/material";
import { name } from "../../helpers/userHelpers";
import useCurrentProject from "../../hooks/projects/useCurrentProject";
import useTask from "../../hooks/task/useTask";
import { ITask, ITaskRequest } from "../../types/story.types";
import Modal from "../generic/Modal";
import CustomSelect from "../generic/select/CustomSelect";
import { useMemo } from "react";
import { FaX } from "react-icons/fa6";

interface AssigneeModalProps {
  task: ITask;
  show: boolean;
  onClose: () => void;
}
function AssigneeModal(props: Readonly<AssigneeModalProps>) {
  const { members } = useCurrentProject();
  const { updateTask } = useTask();
  const memberOptions = useMemo(() => {
    return members
      .filter((m) => props.task.assignees.every((a) => a.id !== m.user.id))
      .map((m) => ({
        label: (
          <span className="flex items-center gap-3">
            <Avatar src={m.user.avatarUrl} />
            {name(m.user)}
          </span>
        ),
        value: m.user
      }));
  }, [props.task, members]);
  const onAddAssignee = (userId: string) => {
    const tmp: ITaskRequest = {
      assigneeIds: [...props.task.assignees.map((a) => a.id), userId],
      sprintColumnId: props.task.sprintColumnId,
      storyId: props.task.storyId,
      title: props.task.title,
      description: props.task.description,
      estimatePoints: props.task.estimatePoints,
      estimateTshirt: props.task.estimateTshirt
    };
    updateTask(tmp, props.task.id);
  };

  const onRemoveAssignee = (userId: string) => {
    const tmp: ITaskRequest = {
      assigneeIds: [...props.task.assignees.filter((a) => a.id !== userId).map((a) => a.id)],
      sprintColumnId: props.task.sprintColumnId,
      storyId: props.task.storyId,
      title: props.task.title,
      description: props.task.description,
      estimatePoints: props.task.estimatePoints,
      estimateTshirt: props.task.estimateTshirt
    };
    updateTask(tmp, props.task.id);
  };

  return (
    <Modal
      title="Assignees"
      show={props.show}
      onClose={props.onClose}
      closeText="Close"
    >
      <div>
        <div className="text-sm text-ap-onyx-200">Add assignee</div>
        <CustomSelect
          options={memberOptions}
          onChange={(o) => {
            if (o) {
              onAddAssignee(o.value.id);
            }
          }}
          value={null}
          filterOption={(o, search) => {
            const u = o.data?.value;
            const nameMatch = u?.fullName?.toLowerCase().includes(search.toLowerCase());
            const emailMatch = u?.email.toLowerCase().includes(search.toLowerCase());
            const userNameMatch = u?.githubUsername?.toLowerCase().includes(search.toLowerCase());
            return Boolean(nameMatch) || Boolean(emailMatch) || Boolean(userNameMatch);
          }}
        />
        <div className="flex flex-col gap-2 mt-4">
          {props.task.assignees.map((a) => (
            <div
              key={"selected-assignee" + a.id}
              className="flex items-center w-full gap-2"
            >
              <FaX
                className="text-sm text-ap-onyx-200 cursor-pointer hover:brightness-90"
                title="Remove assignee"
                onClick={() => onRemoveAssignee(a.id)}
              />
              <Avatar src={a.avatarUrl} />
              {name(a)}
            </div>
          ))}
        </div>
      </div>
    </Modal>
  );
}
export default AssigneeModal;

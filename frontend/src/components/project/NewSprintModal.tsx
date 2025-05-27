import { INewSprint } from "../../types/sprint.types";
import Modal from "../generic/Modal";
import { useState } from "react";
import useCurrentProject from "../../hooks/projects/useCurrentProject";
import { notifyError, notifySuccess } from "../../helpers/notify";
import Input from "../generic/inputs/Input";
import TextArea from "../generic/inputs/CustomTextArea";
import { getNowDatePlusDays } from "../../helpers/timeHelpers";
import ShowIf from "../generic/ShowIf";

interface NewSprintModalProps {
  show: boolean;
  onClose: () => void;
}
function NewSprintModal(props: Readonly<NewSprintModalProps>) {
  const { project, sprints, addSprint } = useCurrentProject();

  const [name, setName] = useState<string>("");
  const [startDate, setStartDate] = useState<string>(new Date().toISOString().substring(0, 10));
  const [endDate, setEndDate] = useState<string>(
    getNowDatePlusDays(14).toISOString().substring(0, 10)
  );
  const [goal, setGoal] = useState<string>("");
  const [copyLastSprintColumns, setCopyLastSprintColumns] = useState<boolean>(sprints.length > 0);

  const handleCreateNewSprint = () => {
    if (!project) return;
    const tmp: INewSprint = {
      projectId: project.id,
      name: name,
      goal: goal,
      startDate: startDate,
      endDate: endDate,
      copyLastSprintColumns: copyLastSprintColumns
    };

    addSprint(tmp)
      .then(() => notifySuccess(`Successfully created sprint "${tmp.name}"`))
      .catch(() => notifyError("Something went wrong while creating sprint"));
    props.onClose();
  };

  return (
    <Modal
      title="New sprint"
      onClose={props.onClose}
      show={props.show}
      actionText="Create"
      onAction={handleCreateNewSprint}
      closeText="Cancel"
    >
      <div className="flex flex-col gap-4">
        <div>
          <div className="text-ap-onyx-400">Name</div>
          <Input
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Sprint name"
            className="w-full"
          />
        </div>
        <div className="flex items-center gap-4">
          <div>
            <div className="text-ap-onyx-400">Start</div>
            <Input
              type="date"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
              className=""
            />
          </div>
          <div>
            <div className="text-ap-onyx-400">End</div>
            <Input
              type="date"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
              className=""
            />
          </div>
        </div>
        <div>
          <div className="text-ap-onyx-400 flex justify-between">
            Goal<div className="italic">Optional</div>
          </div>
          <TextArea
            value={goal}
            onChange={(e) => setGoal(e.target.value)}
            placeholder="Sprint goal"
            className="w-full"
          />
        </div>
        <ShowIf if={sprints.length > 0}>
          <div className="flex items-center gap-1">
            <Input
              type="checkbox"
              checked={copyLastSprintColumns}
              onChange={(e) => setCopyLastSprintColumns(e.target.checked)}
              className="size-5 accent-ap-lavender-900"
            />
            <div className="text-ap-onyx-400">Copy columns from last sprint</div>
          </div>
        </ShowIf>
      </div>
    </Modal>
  );
}
export default NewSprintModal;

import { useState } from "react";
import useCurrentProject from "../../hooks/projects/useCurrentProject";
import useStory from "../../hooks/story/useStory";
import { INewStory, Status } from "../../types/story.types";
import Modal from "../generic/Modal";
import { notifyError, notifySuccess } from "../../helpers/notify";
import Input from "../generic/inputs/Input";
import TextArea from "../generic/inputs/CustomTextArea";
import Tooltip from "../generic/tooltips/Tooltip";

interface NewStoryModalProps {
  show: boolean;
  onClose: () => void;
}
function NewStoryModal(props: Readonly<NewStoryModalProps>) {
  const { project } = useCurrentProject();
  const { createStory } = useStory();
  const [title, setTitle] = useState<string>("");
  const [description, setDescription] = useState<string>("");
  const [acceptanceCriteria, setAcceptanceCriteria] = useState<string>("");

  const handleCreate = () => {
    if (!project) return;
    const tmp: INewStory = {
      projectId: project.id,
      status: Status.TODO,
      priority: 0,
      title: title,
      description: description,
      acceptanceCriteria: acceptanceCriteria
    };
    void createStory(tmp)
      .then(() => notifySuccess("Successfully created story"))
      .catch(() => notifyError("Failed to create story"));
    props.onClose();
  };

  return (
    <Modal
      title="New Story"
      onClose={props.onClose}
      show={props.show}
      actionText="Create"
      onAction={handleCreate}
    >
      <div className="flex flex-col gap-4">
        <div>
          <div className="text-ap-onyx-400">Title</div>
          <Input
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="Story title"
            className="w-full"
          />
        </div>
        <div>
          <div className="text-ap-onyx-400 flex justify-between">

            <div className="flex items-center gap-1">
              Description
              <Tooltip
                text="A short description gives context<br />and helps others understand the<br />purpose and scope of the story."
                className="ml-1"
                id="storyDescriptionTooltip"
              />
            </div>
            <div className="italic">Optional</div>
          </div>
          <TextArea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Story description"
            className="w-full"
          />
        </div>
        <div>
          <div className="text-ap-onyx-400 flex justify-between">
            <div className="flex items-center gap-1">
              Acceptance Criteria
              <Tooltip
                text="Acceptance criteria help clarify what 'done' means.<br />You can use the 'Given-When-Then' format to describe<br />expected behavior clearly."
                className="ml-1"
                id="storyAcceptanceCriteriaTooltip"
              />
            </div>
            <div className="italic">Optional</div>
          </div>
          <TextArea
            value={acceptanceCriteria}
            onChange={(e) => setAcceptanceCriteria(e.target.value)}
            placeholder="Story acceptance criteria"
            className="w-full"
          />
        </div>
      </div>
    </Modal>
  );
}
export default NewStoryModal;

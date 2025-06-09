import { useMemo, useState } from "react";
import useProjects from "../../hooks/projects/useProjects";
import { EstimationMethod, Framework, INewProject } from "../../types/project.types";
import Modal from "../generic/Modal";
import { notifyError, notifySuccess } from "../../helpers/notify";
import Input from "../generic/inputs/Input";
import TextArea from "../generic/inputs/CustomTextArea";
import Tooltip from "../generic/tooltips/Tooltip";

interface CreateProjectModalProps {
  show: boolean;
  onClose: () => void;
}
function CreateProjectModal(props: Readonly<CreateProjectModalProps>) {
  const { createProject } = useProjects();

  const [name, setName] = useState<string>("");
  const [description, setDescription] = useState<string>("");
  const [framework, setFramework] = useState<Framework>(Framework.NONE);
  const [estimationMethod, setEstimationMethod] = useState<EstimationMethod>(
    EstimationMethod.STORY_POINTS
  );

  const disableCreate = useMemo(() => {
    return name === "";
  }, [name]);

  const handleCreateProject = () => {
    const tmp: INewProject = {
      name,
      framework,
      description,
      estimationMethod
    };
    createProject(tmp)
      .then(() => notifySuccess(`Successfully created Project "${name}"`))
      .catch(() => notifyError("Something went wrong while creating Project"));
    props.onClose();
  };

  return (
    <Modal
      title="New Project"
      show={props.show}
      onClose={props.onClose}
      actionText="Create"
      onAction={handleCreateProject}
      disableAction={disableCreate}
      disableEnterSubmit
    >
      <div className="flex flex-col gap-4">
        <div>
          <div className="text-ap-onyx-200">Title</div>
          <Input
            placeholder="Project title"
            onChange={(e) => setName(e.target.value)}
            value={name}
            className="w-full"
          />
        </div>
        <div>
          <div className="text-ap-onyx-200">Agile Framework</div>
          <div className="flex items-center gap-4 select-none my-1">
            <label className="flex items-center text-sm gap-1">
              <Input
                type="radio"
                name="framework"
                value={Framework.SCRUM}
                onChange={() => setFramework(Framework.SCRUM)}
                checked={framework === Framework.SCRUM}
                className="accent-ap-lavender-900"
              />
              <div>SCRUM</div>
              <Tooltip
                text="Scrum offers a simple, structured approach<br />to managing work in short, focused cycles."
                id="scrumTooltip"
              />
            </label>
            <label className="flex items-center text-sm gap-1">
              <Input
                type="radio"
                name="framework"
                value={Framework.XP}
                onChange={() => setFramework(Framework.XP)}
                checked={framework === Framework.XP}
                className="accent-ap-lavender-900"
              />
              <div>XP</div>
              <Tooltip
                text="XP focuses on writing high-quality code<br />through constant feedback, pair programming,<br />and frequent releases."
                id="xpTooltip"
              />
            </label>
            <label className="flex items-center text-sm gap-1">
              <Input
                type="radio"
                name="framework"
                value={Framework.NONE}
                onChange={() => setFramework(Framework.NONE)}
                checked={framework === Framework.NONE}
                className="accent-ap-lavender-900"
              />
              None
            </label>
          </div>
        </div>
        <div>
          <div className="text-ap-onyx-200">Estimation method</div>
          <div className="flex items-center gap-4 select-none my-1">
            <label className="flex items-center text-sm gap-1">
              <Input
                type="radio"
                name="estimation"
                value={EstimationMethod.STORY_POINTS}
                onChange={() => setEstimationMethod(EstimationMethod.STORY_POINTS)}
                checked={estimationMethod === EstimationMethod.STORY_POINTS}
                className="accent-ap-lavender-900"
              />
              <div>
                Story points
              </div>
              <Tooltip
                text="Story points use numbers (like Fibonacci)<br />to estimate effort more precisely.<br />Gives finer control, but harder to grasp at first."
                id="storyPointsTooltip"
              />
            </label>
            <label className="flex items-center text-sm gap-1">
              <Input
                type="radio"
                name="estimation"
                value={EstimationMethod.TSHIRT_SIZES}
                onChange={() => setEstimationMethod(EstimationMethod.TSHIRT_SIZES)}
                checked={estimationMethod === EstimationMethod.TSHIRT_SIZES}
                className="accent-ap-lavender-900"
              />
              <div>
                T-shirt sizes
              </div>
              <Tooltip
                text="T-shirt sizes (S, M, L...) are a simple way<br />to estimate work. <br />Easier to use, but offer less precision."
                id="tshirtTooltip"
              />
            </label>
          </div>
        </div>
        <div>
          <div className="text-ap-onyx-200 w-full flex justify-between">
            <div className="flex items-center gap-1">
              Description 
              <Tooltip
                text="A clear description helps your team understand<br />the purpose, scope, and context of the task —<br />it saves questions later."
                id="descriptionTooltip"
              />
            </div>
            
            <span className="italic text-sm">Optional</span>
          </div>
          <TextArea
            placeholder="Description"
            onChange={(e) => setDescription(e.target.value)}
            value={description}
            className="w-full"
            enableResize
          />
        </div>
      </div>
    </Modal>
  );
}
export default CreateProjectModal;

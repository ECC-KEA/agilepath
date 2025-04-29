import { useMemo, useState } from "react";
import useProjects from "../../hooks/projects/useProjects";
import { Framework, INewProject } from "../../types/project.types";
import Modal from "../generic/Modal";
import { notifyError, notifySuccess } from "../../helpers/notify";
import Input from "../generic/inputs/Input";
import TextArea from "../generic/inputs/CustomTextArea";

interface CreateProjectModalProps {
  show: boolean;
  onClose: () => void;
}
function CreateProjectModal(props: Readonly<CreateProjectModalProps>) {
  const { createProject } = useProjects();

  const [name, setName] = useState<string>("");
  const [description, setDescription] = useState<string>("");
  const [framework, setFramework] = useState<Framework>(Framework.NONE);

  const disableCreate = useMemo(() => {
    return name === "";
  }, [name]);

  const handleCreateProject = () => {
    const tmp: INewProject = {
      name,
      framework,
      description
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
                placeholder="Project title"
                value={Framework.SCRUM}
                onChange={() => setFramework(Framework.SCRUM)}
                checked={framework === Framework.SCRUM}
                className="accent-ap-lavender-900"
              />
              SCRUM
            </label>
            <label className="flex items-center text-sm gap-1">
              <Input
                type="radio"
                name="framework"
                placeholder="Project title"
                value={Framework.XP}
                onChange={() => setFramework(Framework.XP)}
                checked={framework === Framework.XP}
                className="accent-ap-lavender-900"
              />
              XP
            </label>
            <label className="flex items-center text-sm gap-1">
              <Input
                type="radio"
                name="framework"
                placeholder="Project title"
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
          <div className="text-ap-onyx-200 w-full flex justify-between">
            Description <span className="italic text-sm">Optional</span>
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

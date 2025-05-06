import { FaPlus } from "react-icons/fa6";
import Button from "../components/generic/buttons/Button";
import ShowIf from "../components/generic/ShowIf";
import { PropsWithChildren, useState } from "react";
import CreateProjectModal from "../components/projects/CreateProjectModal";
import ProjectList from "../components/projects/ProjectList";

function Projects() {
  const [showCreateProjectModal, setShowCreateProjectModal] = useState<boolean>(false);

  return (
    <div>
      <Header>
        <div className="flex justify-between w-full">
          Projects
          <Button
            text={
              <span className="flex items-center gap-1">
                <FaPlus /> New Project
              </span>
            }
            onClick={() => setShowCreateProjectModal(true)}
            className="bg-ap-lavender-700 text-white pr-3 text-lg"
          />
        </div>
      </Header>
      <ProjectList />
      <ShowIf if={showCreateProjectModal}>
        <CreateProjectModal
          show={showCreateProjectModal}
          onClose={() => setShowCreateProjectModal(false)}
        />
      </ShowIf>
    </div>
  );
}

function Header({ children }: Readonly<PropsWithChildren>) {
  return (
    <div className="bg-ap-onyx-50/20 border-b border-ap-onyx-200">
      <div className="flex items-center gap-2 p-4 text-ap-onyx-500">
        <div className="text-2xl w-full">{children}</div>
      </div>
    </div>
  );
}

export default Projects;

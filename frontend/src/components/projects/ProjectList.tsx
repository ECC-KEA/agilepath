import { useNavigate } from "react-router";
import useProjects from "../../hooks/projects/useProjects";
import { Framework, IProject } from "../../types/project.types";
import ShowIf from "../generic/ShowIf";

function ProjectList() {
  const { projects } = useProjects();

  return (
    <div className="flex flex-col gap-2 p-4 max-h-[calc(100vh-200px)] overflow-y-auto">
      {projects.map((p) => (
        <ProjectListItem
          project={p}
          key={p.id + "project-list-item"}
        />
      ))}
    </div>
  );
}

interface ProjectListItemProps {
  project: IProject;
}

function ProjectListItem(props: Readonly<ProjectListItemProps>) {
  const navigate = useNavigate();
  return (
    <div
      onClick={() => navigate("/projects/" + props.project.id + "/overview")}
      className="cursor-pointer hover:bg-ap-cyan-50/10 flex gap-2 justify-between p-3 border border-ap-lavender-50 rounded-lg w-96 shadow-xs"
    >
      <div className="text-lg">{props.project.name}</div>
      <ShowIf if={props.project.framework === Framework.SCRUM}>
        <div className="bg-ap-cyan-900 text-white rounded-xl py-1 px-3 text-xs flex items-center">
          {props.project.framework}
        </div>
      </ShowIf>
      <ShowIf if={props.project.framework === Framework.XP}>
        <div className="bg-ap-mint-900 text-white rounded-xl py-1 px-3 text-xs flex items-center">
          {props.project.framework}
        </div>
      </ShowIf>
    </div>
  );
}
export default ProjectList;

import { PropsWithChildren } from "react";
import { FaArrowLeft } from "react-icons/fa6";
import { NavLink, To, useNavigate, useParams } from "react-router";
import ShowIf from "../generic/ShowIf";

interface ProjectHeaderProps {
  projectName: string;
}

function ProjectHeader(props: Readonly<ProjectHeaderProps>) {
  const navigate = useNavigate();
  const { projectID, sprintId } = useParams();
  const basePath = sprintId
    ? `/projects/${projectID}/sprint/${sprintId}`
    : `/projects/${projectID}`;

  return (
    <div className="bg-ap-onyx-50/20 border-b border-ap-onyx-200">
      <div className="flex flex-row items-center justify-start gap-2 p-4 text-ap-onyx-500">
        <FaArrowLeft
          className="text-2xl cursor-pointer"
          onClick={() => navigate("/")}
        />
        <h1 className="text-2xl">{props.projectName}</h1>
      </div>
      <div className="flex flex-row gap-2 pl-58">
        <HeaderLink to={`${basePath}/overview`}>board</HeaderLink>
        <ShowIf if={!!sprintId}>
          <HeaderLink to={`${basePath}/stats`}>stats</HeaderLink>
        </ShowIf>
        <ShowIf if={!sprintId}>
          <HeaderLink to={`${basePath}/contributors`}>contributors</HeaderLink>
        </ShowIf>
      </div>
    </div>
  );
}

interface HeaderLinkProps extends PropsWithChildren {
  to: To;
}

function HeaderLink(props: Readonly<HeaderLinkProps>) {
  return (
    <NavLink
      to={props.to}
      className={({ isActive }) =>
        `${isActive ? "text-ap-lavender-600 underline" : "text-ap-onyx-700 hover:text-ap-lavender-400 hover:underline"} cursor-pointer`
      }
    >
      {props.children}
    </NavLink>
  );
}

export default ProjectHeader;

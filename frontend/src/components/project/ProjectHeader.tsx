import { PropsWithChildren } from "react";
import { FaArrowLeft } from "react-icons/fa6";
import { NavLink, To, useNavigate } from "react-router";

interface ProjectHeaderProps {
  projectName: string;
}

function ProjectHeader(props: Readonly<ProjectHeaderProps>) {
  const navigate = useNavigate();
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
        <HeaderLink to="overview">board</HeaderLink>
        <HeaderLink to="stats">stats</HeaderLink>
        <HeaderLink to="members">members</HeaderLink>
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

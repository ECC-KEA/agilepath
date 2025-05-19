import { Outlet, useParams } from "react-router";
import useCurrentProject from "../hooks/projects/useCurrentProject";
import { PropsWithChildren } from "react";
import CurrentProjectProvider from "../hooks/projects/CurrentProjectProvider";
import ProjectSidebar from "../components/project/ProjectSidebar";
import ProjectHeader from "../components/project/ProjectHeader";
import StoryProvider from "../hooks/story/StoryProvider";

export function ProjectWrapper({ children }: Readonly<PropsWithChildren>) {
  const { projectID } = useParams();
  if (!projectID) {
    return null; // TODO return 404 page
  }
  return (
    <CurrentProjectProvider>
      <StoryProvider>{children}</StoryProvider>
    </CurrentProjectProvider>
  );
}

function Project() {
  const { project } = useCurrentProject();

  if (!project) {
    return null; // TODO return 404 page
  }

  return (
    <div className="h-full w-full flex flex-col">
      <ProjectHeader projectName={project.name} />
      <div className="flex-1 flex">
        <ProjectSidebar />
        <Outlet />
      </div>
    </div>
  );
}

export default Project;

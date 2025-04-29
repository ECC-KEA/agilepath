import useProjects from "../hooks/projects/useProjects";

function Projects() {
  const { projects, createProject } = useProjects();

  console.log(projects);

  return <div>projects</div>;
}

export default Projects;

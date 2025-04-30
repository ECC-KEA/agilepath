import { FaArrowLeft } from "react-icons/fa6";

interface ProjectHeaderProps {
  projectName: string;
}

function ProjectHeader(props: Readonly<ProjectHeaderProps>) {
  return (
    <div className="bg-ap-onyx-50/20 border-b border-ap-onyx-200">
      <div className="flex flex-row items-center justify-start gap-2 p-4 text-ap-onyx-500">
        <FaArrowLeft className="text-2xl" />
        <h1 className="text-2xl">{props.projectName}</h1>
      </div>
      <div className="flex flex-row gap-2">
        <div className="w-50" />
        <div className="text-ap-lavender-600 underline cursor-pointer">board</div>
        <div className="text-ap-onyx-700 cursor-pointer">stats</div>
      </div>
    </div>
  );
}

export default ProjectHeader;

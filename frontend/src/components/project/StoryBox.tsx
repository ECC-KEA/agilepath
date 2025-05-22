import { IStory, Status } from "../../types/story.types";
import StatusLabel from "../status/StatusLabel";
import { useNavigate } from "react-router";

interface StoryBoxProps {
  story: IStory;
}
function StoryBox(props: Readonly<StoryBoxProps>) {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/projects/${props.story.projectId}/edit/${props.story.id}`);
  }

  return (
    <div 
      onClick={handleClick}
      className="p-2 bg-white rounded shadow cursor-pointer"
    >
      <div className="flex justify-between items-center text-sm">
        {/* TODO: replace with issue id */}
        <div className="truncate w-20">#{props.story.id}</div>
        <StatusLabel status={props.story.status as Status} />
      </div>
      <div className="my-2 mx-4">{props.story.title}</div>
    </div>
  );
}
export default StoryBox;

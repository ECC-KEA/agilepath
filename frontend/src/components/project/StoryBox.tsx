import { IStory, Status } from "../../types/story.types";
import StatusLabel from "../status/StatusLabel";

interface StoryBoxProps {
  story: IStory;
}
function StoryBox(props: Readonly<StoryBoxProps>) {
  return (
    <div className="p-2 bg-white rounded shadow">
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

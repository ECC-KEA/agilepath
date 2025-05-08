import { useMemo, useState } from "react";
import { IStory, Status } from "../../types/story.types";
import StoryBox from "./StoryBox";
import StatusLabel from "../status/StatusLabel";
import ShowIf from "../generic/ShowIf";
import Button from "../generic/buttons/Button";
import { FaPlus } from "react-icons/fa6";
import NewStoryModal from "./NewStoryModal";

interface StoryColumnProps {
  status: Status;
  stories: IStory[];
}
function StoryColumn(props: Readonly<StoryColumnProps>) {
  const [showNewStoryModal, setShowNewStoryModal] = useState<boolean>(false);

  const filteredStories = useMemo(
    () => props.stories.filter((s) => s.status === props.status),
    [props.stories, props.status]
  );

  return (
    <div className={`bg-ap-onyx-50/25 w-96`}>
      <div className="flex justify-between items-center p-2">
        {/* Hack to align title center */}
        <div className="select-none px-2 text-transparent">{props.status}</div>
        <div className="capitalize text-xl">
          {props.status === Status.TODO ? "Backlog" : props.status.toLowerCase()}
        </div>
        <StatusLabel status={props.status} />
      </div>
      <div className="h-[calc(100vh-350px)] overflow-y-auto mx-2 flex-col flex gap-2">
        {filteredStories.map((s) => (
          <StoryBox
            key={s.id + "-" + props.status}
            story={s}
          />
        ))}
      </div>
      <ShowIf if={props.status === Status.TODO}>
        <div className="w-full flex items-center justify-center py-2">
          <Button
            text={
              <span className="flex items-center gap-2">
                <FaPlus className="text-ap-lavender-900" />
                New Story
              </span>
            }
            className="bg-white px-10 border border-ap-onyx-50"
            onClick={() => setShowNewStoryModal(true)}
          />
        </div>
      </ShowIf>
      <ShowIf if={showNewStoryModal}>
        <NewStoryModal
          show={showNewStoryModal}
          onClose={() => setShowNewStoryModal(false)}
        />
      </ShowIf>
    </div>
  );
}
export default StoryColumn;

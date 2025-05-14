import { Status } from "../types/story.types";
import StoryColumn from "../components/project/StoryColumn";
import { useMemo, useState } from "react";
import useStory from "../hooks/story/useStory";
import SearchInput from "../components/generic/inputs/SearchInput";
import { storySearchPredicate } from "../helpers/storyHelpers";

function ProjectOverview() {
  const { stories } = useStory();
  const [search, setSearch] = useState<string>("");

  const filteredStories = useMemo(
    () => stories.filter((s) => storySearchPredicate(s, search)),
    [stories, search]
  );

  return (
    <div className="p-4">
      <SearchInput
        type="search"
        placeholder="Search by Story title or ID"
        containerClassName="w-96"
        value={search}
        onChange={(e) => setSearch(e.target.value)}
      />
      <div className="mt-4 flex border border-ap-onyx-50 divide-ap-onyx-50 rounded-md divide-x overflow-hidden">
        <StoryColumn
          status={Status.TODO}
          stories={filteredStories}
        />
        <StoryColumn
          status={Status.DONE}
          stories={filteredStories}
        />
        <StoryColumn
          status={Status.ARCHIVED}
          stories={filteredStories}
        />
      </div>
    </div>
  );
}
export default ProjectOverview;

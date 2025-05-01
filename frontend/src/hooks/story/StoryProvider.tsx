import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import useCurrentProject from "../projects/useCurrentProject";
import { IStory } from "../../types/story.types";
import StoryContext from "./StoryContext";
import { useApi } from "../utils/useApi";

function StoryProvider({ children }: Readonly<PropsWithChildren>) {
  const { project } = useCurrentProject();
  const { get } = useApi();
  const [_stories, setStories] = useState<IStory[]>([]);

  const stories = useMemo(() => _stories, [_stories]);

  const getStories = useCallback(() => {
    return get(`/projects/${project?.id}/stories`).then(setStories).catch(console.error);
  }, [get, project]);

  useEffect(() => {
    void getStories();
  }, [project]);

  return (
    <StoryContext.Provider
      value={{
        stories
      }}
    >
      {children}
    </StoryContext.Provider>
  );
}

export default StoryProvider;

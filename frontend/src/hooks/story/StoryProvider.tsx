import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import { INewStory, IStory } from "../../types/story.types";
import StoryContext from "./StoryContext";
import { useApi } from "../utils/useApi";
import { useParams } from "react-router";

function StoryProvider({ children }: Readonly<PropsWithChildren>) {
  const { projectID, storyId } = useParams();
  const { get, post } = useApi();
  const [stories, setStories] = useState<IStory[]>([]);

  const getStories = useCallback(() => {
    return get(`/projects/${projectID}/stories`).then(setStories).catch(console.error);
  }, [get, projectID]);

  const story = useMemo(() => {
    if (!storyId) return undefined;
    return stories.find((s) => s.id === storyId);
  }, [stories, storyId]);

  const createStory = useCallback(
    (story: INewStory) => {
      return post(`/stories`, story).then((res) => setStories((prev) => [...prev, res]));
    },
    [post]
  );

  useEffect(() => {
    void getStories();
  }, [projectID]);

  const contextValue = useMemo(
    () => ({
      stories,
      story,
      createStory
    }),
    [stories, story, createStory]
  );

  return <StoryContext.Provider value={contextValue}>{children}</StoryContext.Provider>;
}

export default StoryProvider;
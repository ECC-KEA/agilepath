import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import { INewStory, IStory } from "../../types/story.types";
import StoryContext from "./StoryContext";
import { useApi } from "../utils/useApi";
import { useParams } from "react-router";

function StoryProvider({ children }: Readonly<PropsWithChildren>) {
  const { projectID } = useParams();
  const { get, post } = useApi();
  const [stories, setStories] = useState<IStory[]>([]);

  const getStories = useCallback(() => {
    return get(`/projects/${projectID}/stories`).then(setStories).catch(console.error);
  }, [get, projectID]);

  const createStory = useCallback(
    (story: INewStory) => {
      return post(`/stories`, story)
        .then((res) => setStories((prev) => [...prev, res]))
        .catch(console.error);
    },
    [post]
  );

  useEffect(() => {
    void getStories();
  }, [projectID]);

  const contextValue = useMemo(
    () => ({
      stories,
      createStory
    }),
    [stories, createStory]
  );

  return <StoryContext.Provider value={contextValue}>{children}</StoryContext.Provider>;
}

export default StoryProvider;

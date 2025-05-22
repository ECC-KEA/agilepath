import { createContext } from "react";
import { INewStory, IStory } from "../../types/story.types";

interface IStoryContext {
  stories: IStory[];
  story?: IStory;
  createStory: (story: INewStory) => Promise<void>;
}

const StoryContext = createContext<IStoryContext | undefined>(undefined);
export default StoryContext;

import { createContext } from "react";
import { IStory } from "../../types/story.types";

interface IStoryContext {
  stories: IStory[];
}

const StoryContext = createContext<IStoryContext | undefined>(undefined);
export default StoryContext;

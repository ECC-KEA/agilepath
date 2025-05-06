import { useContext } from "react";
import StoryContext from "./StoryContext";

export default function useStory() {
  const context = useContext(StoryContext);
  if (!context) {
    throw new Error("useStory must be used within StoryProvider");
  }
  return context;
}

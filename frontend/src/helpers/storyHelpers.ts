import { IStory } from "../types/story.types";

export function storySearchPredicate(s: IStory, searchVal: string) {
  const search = searchVal.toLowerCase();
  return s.title?.toLowerCase().includes(search) || s.id.includes(search);
}

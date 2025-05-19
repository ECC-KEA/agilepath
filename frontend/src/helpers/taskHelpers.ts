import { ITask } from "../types/story.types";

export function taskSearchPredicate(t: ITask, searchVal: string) {
  const s = searchVal.toLowerCase();
  return (
    t.title.toLowerCase().includes(s) ||
    t.id.includes(s) ||
    t.assignees.some((a) => a.fullName?.toLowerCase().includes(s))
  );
}

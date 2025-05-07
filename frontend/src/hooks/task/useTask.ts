import { useContext } from "react";
import TaskContext from "./TaskContext";

export default function useTask() {
  const context = useContext(TaskContext);
  if (!context) {
    throw new Error("useTask must be used withing TaskProvider");
  }
  return context;
}

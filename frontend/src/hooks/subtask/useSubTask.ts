import { useContext } from "react";
import SubTaskContext from "./SubTaskContext";

export default function useSubTask() {
  const context = useContext(SubTaskContext);
  if (!context) {
    throw new Error("useSubtask must be used within SubTaskProvider");
  }
  return context;
}
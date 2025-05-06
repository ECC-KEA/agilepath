import { useContext } from "react";
import CurrentProjectContext from "./CurrentProjectContext";

export default function useCurrentProject() {
  const context = useContext(CurrentProjectContext);
  if (!context) {
    throw new Error("useCurrentProject must be used within CurrentProjectProvider");
  }
  return context;
}

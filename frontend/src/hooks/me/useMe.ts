import { useContext } from "react";
import MeContext from "./MeContext";

export default function useMe() {
  const context = useContext(MeContext);
  if (!context) {
    throw new Error("useMe must be used within MeProvider");
  }
  return context;
}

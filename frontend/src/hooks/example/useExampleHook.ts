import { useContext } from "react";
import ExampleContext from "./ExampleContext";

export default function useExampleHook() {
  const context = useContext(ExampleContext);
  if (!context) {
    throw new Error("useExampleHook must be used within a ExampleProvider");
  }
  return context;
}

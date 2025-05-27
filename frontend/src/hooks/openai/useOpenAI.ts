import { useContext } from "react";
import OpenAIContext from "./OpenAIContext";

export default function useOpenAI() {
  const context = useContext(OpenAIContext);
  if (!context) {
    throw new Error("useOpenAI must be used within an OpenAIProvider");
  }
  return context;
}
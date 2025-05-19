import { createContext } from "react";
import { IOpenAIResponse, IOpenAIRequest, IStreamedResponse } from "../../types/openai.types";

interface IOpenAIContext {
  response: IStreamedResponse | undefined;
  sendMessage:  (body: IOpenAIRequest, onChunk?: (chunk: string) => void) => Promise<string>;
}

const OpenAIContext = createContext<IOpenAIContext | undefined>(undefined);
export default OpenAIContext;
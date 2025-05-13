import { createContext } from "react";
import { IOpenAIResponse, IOpenAIRequest } from "../../types/openai.types";

interface IOpenAIContext {
  response: IOpenAIResponse | undefined;
  sendMessage: (body: IOpenAIRequest) => Promise<IOpenAIResponse>;
}

const OpenAIContext = createContext<IOpenAIContext | undefined>(undefined);
export default OpenAIContext;
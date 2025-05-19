import { PropsWithChildren, useCallback, useMemo, useState } from "react";
import { IOpenAIRequest, IStreamedResponse } from "../../types/openai.types";
import { useApi } from "../utils/useApi";
import OpenAIContext from "./OpenAIContext";

function OpenAIProvider({ children}: PropsWithChildren) {
    const { postOpenAI } = useApi();
    const [response, setResponse] = useState<IStreamedResponse | undefined>(undefined);

    const sendMessage = useCallback(
      (body: IOpenAIRequest, onChunk?: (chunk: string) => void) => {
        return postOpenAI(`https://api.openai.com/v1/chat/completions`, body, onChunk)
          .then((finalText) => {
            setResponse({ content: finalText }); 
            return finalText;
          })
          .catch((error) => {
            console.error("Error sending message to OpenAI:", error);
            throw error;
          });
      },
      [postOpenAI]
    );

    const contextValue = useMemo(() => ({
      response,
      sendMessage,
    }),[response, sendMessage]);

    return (
      <OpenAIContext.Provider value={contextValue}>
        {children}
      </OpenAIContext.Provider>
    );
}

export default OpenAIProvider;
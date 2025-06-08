import { PropsWithChildren, useCallback, useMemo, useState } from "react";
import { IAssistant } from "../../types/assistant.types";
import { useApi } from "../utils/useApi";
import AssistantContext from "./AssistantContext";
import { useLoading } from "../utils/loading/useLoading";

function AssistantProvider({ children }: Readonly<PropsWithChildren>) {
  const loader = useLoading();
  const { get } = useApi();
  const [assistant, setAssistant] = useState<IAssistant | undefined>(undefined);

  const loadAssistant = useCallback(async (assistantName: string): Promise<IAssistant> => {
    loader.add();
    try {
      const data = await get(`/assistants/${assistantName}`);
      setAssistant(data);
      return data;
    } finally {
      loader.done();
    }
  }, [get, loader]);

  const contextValue = useMemo(() => ({
    assistant,
    loadAssistant
  }), [assistant, loadAssistant]);

  return (
    <AssistantContext.Provider value={contextValue}>
      {children}
    </AssistantContext.Provider>
  );
}

export default AssistantProvider;
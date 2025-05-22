import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import { IAssistant } from "../../types/assistant.types";
import { useApi } from "../utils/useApi";
import AssistantContext from "./AssistantContext";
import { useLoading } from "../utils/loading/useLoading";

interface AssistantProviderProps extends PropsWithChildren {
  assistantId: string;
}

function AssistantProvider({ children, assistantId }: Readonly<AssistantProviderProps>) {
  const loader = useLoading();
  const { get } = useApi();
  const [assistant, setAssistant] = useState<IAssistant>();

  const loadAssistant = useCallback(async () => {
    loader.add();
    return get(`/assistants/${assistantId}`).then(setAssistant).finally(loader.done);
  }, [get, assistantId]);

  useEffect(() => {
    loadAssistant();
  }, [loadAssistant]);

  const contextValue = useMemo(() => ({
    assistant,
    assistantId
  }), [assistant, assistantId]);

  return (
    <AssistantContext.Provider value={contextValue}>
      {children}
    </AssistantContext.Provider>
  );
}

export default AssistantProvider;
import { createContext} from 'react';
import { IAssistant } from '../../types/assistant.types';

interface IAssistantContext {
  assistant: IAssistant | undefined;
  assistantId: string;
}

const AssistantContext = createContext<IAssistantContext | undefined>(undefined);
export default AssistantContext;
export interface IAssistant {
    id: string;
    name: string;
    description?: string;
    model?: string;
    prompt?: string;
    temperature?: number;
    topP?: number;
    maxTokens?: number;
}

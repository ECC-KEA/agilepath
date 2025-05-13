interface IOpenAIRequest {
    model: string;
    messages: IRequestMessage[];
    temperature?: number;
    top_p?: number;
    n?: number;
    stream?: boolean;
    stop?: string | string[];
    max_tokens?: number;
    presence_penalty?: number;
    frequency_penalty?: number;
}

interface IRequestMessage {
    role: string;
    content: string;
}

interface IOpenAIResponse {
    id: string;
    object: string;
    created: number;
    model: string;
    choices: IChoice[];
    usage: IUsage;
    service_tier: string;
    system_fingerprint: string;
}

interface IChoice {
    index: number;
    message: IMessage;
    logprobs: null | any;
    finish_reason: string;
}

interface IMessage {
    role: string;
    content: string;
    refusal: null | any;
    annotations: any[];
}

interface IUsage {
    prompt_tokens: number;
    completion_tokens: number;
    total_tokens: number;
    prompt_tokens_details: IPromptTokensDetails;
    completion_tokens_details: ICompletionTokensDetails;
}

interface IPromptTokensDetails {
    cached_tokens: number;
    audio_tokens: number;
}

interface ICompletionTokensDetails {
    reasoning_tokens: number;
    audio_tokens: number;
    accepted_prediction_tokens: number;
    rejected_prediction_tokens: number;
}

export type { IOpenAIResponse, IChoice, IMessage, IUsage, IPromptTokensDetails, ICompletionTokensDetails, IOpenAIRequest };
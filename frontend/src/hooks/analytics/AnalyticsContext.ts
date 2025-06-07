import {createContext} from "react";
import {
    IBurndownData,
    ISprintAnalysis,
    ISprintComparison,
    ISprintMetadata,
    ISprintMetrics,
    ISprintTrends,
    ITaskMetrics
} from "../../types/analytics.types";

interface IAnalyticsContext {
    getSprintAnalysis: (sprintId: string) => Promise<ISprintAnalysis>;
    getSprintMetrics: (sprintId: string) => Promise<ISprintMetrics>;
    getSprintMetadata: (sprintId: string) => Promise<ISprintMetadata>;
    getTaskMetrics: (sprintId: string) => Promise<ITaskMetrics[]>;
    getTaskMetricsById: (sprintId: string, taskId: string) => Promise<ITaskMetrics>;
    getBurndownData: (sprintId: string) => Promise<IBurndownData>;
    compareSprintToPrevious: (sprintId: string) => Promise<ISprintComparison>;
    getSprintTrends: (projectId: string, sprintCount?: number) => Promise<ISprintTrends>;
}

const AnalyticsContext = createContext<IAnalyticsContext | undefined>(undefined);
export default AnalyticsContext;
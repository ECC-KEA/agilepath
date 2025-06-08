import {PropsWithChildren, useCallback, useMemo} from "react";
import {useApi} from "../utils/useApi";
import {useLoading} from "../utils/loading/useLoading";
import AnalyticsContext from "./AnalyticsContext";
import {
    IBurndownData,
    ISprintAnalysis,
    ISprintComparison,
    ISprintMetadata,
    ISprintMetrics,
    ISprintTrends,
    ITaskMetrics
} from "../../types/analytics.types";

function AnalyticsProvider({children}: Readonly<PropsWithChildren>) {
    const {get} = useApi();
    const {add, done} = useLoading();

    // Sprint Analysis Methods
    const getSprintAnalysis = useCallback(
        (sprintId: string): Promise<ISprintAnalysis> => {
            add();
            return get(`/analytics/sprints/${sprintId}/analysis`)
                .catch((error) => {
                    console.error('Failed to fetch sprint analysis:', error);
                    throw error;
                })
                .finally(done);
        },
        [get]
    );

    const getSprintMetrics = useCallback(
        (sprintId: string): Promise<ISprintMetrics> => {
            add();
            return get(`/analytics/sprints/${sprintId}/metrics`)
                .catch((error) => {
                    console.error('Failed to fetch sprint metrics:', error);
                    throw error;
                })
                .finally(done);
        },
        [get]
    );

    const getSprintMetadata = useCallback(
        (sprintId: string): Promise<ISprintMetadata> => {
            add();
            return get(`/analytics/sprints/${sprintId}/metadata`)
                .catch((error) => {
                    console.error('Failed to fetch sprint metadata:', error);
                    throw error;
                })
                .finally(done);
        },
        [get]
    );

    // Task Analysis Methods
    const getTaskMetrics = useCallback(
        (sprintId: string): Promise<ITaskMetrics[]> => {
            add();
            return get(`/analytics/sprints/${sprintId}/tasks/metrics`)
                .catch((error) => {
                    console.error('Failed to fetch task metrics:', error);
                    throw error;
                })
                .finally(done);
        },
        [get]
    );

    const getTaskMetricsById = useCallback(
        (sprintId: string, taskId: string): Promise<ITaskMetrics> => {
            add();
            return get(`/analytics/sprints/${sprintId}/tasks/${taskId}/metrics`)
                .catch((error) => {
                    console.error('Failed to fetch task metrics by ID:', error);
                    throw error;
                })
                .finally(done);
        },
        [get]
    );

    // Burndown Chart Methods
    const getBurndownData = useCallback(
        (sprintId: string): Promise<IBurndownData> => {
            add();
            return get(`/analytics/sprints/${sprintId}/burndown`)
                .catch((error) => {
                    console.error('Failed to fetch burndown data:', error);
                    throw error;
                })
                .finally(done);
        },
        [get]
    );

    // Comparative Analysis Methods
    const compareSprintToPrevious = useCallback(
        (sprintId: string): Promise<ISprintComparison> => {
            add();
            return get(`/analytics/sprints/${sprintId}/comparison`)
                .catch((error) => {
                    console.error('Failed to fetch sprint comparison:', error);
                    throw error;
                })
                .finally(done);
        },
        [get]
    );

    const getSprintTrends = useCallback(
        (projectId: string, sprintCount = 3): Promise<ISprintTrends> => {
            add();
            return get(`/analytics/projects/${projectId}/trends?sprintCount=${sprintCount}`)
                .catch((error) => {
                    console.error('Failed to fetch sprint trends:', error);
                    throw error;
                })
                .finally(done);
        },
        [get]
    );

    const contextValue = useMemo(
        () => ({
            getSprintAnalysis,
            getSprintMetrics,
            getSprintMetadata,
            getTaskMetrics,
            getTaskMetricsById,
            getBurndownData,
            compareSprintToPrevious,
            getSprintTrends
        }),
        [
            getSprintAnalysis,
            getSprintMetrics,
            getSprintMetadata,
            getTaskMetrics,
            getTaskMetricsById,
            getBurndownData,
            compareSprintToPrevious,
            getSprintTrends
        ]
    );

    return (
        <AnalyticsContext.Provider value={contextValue}>
            {children}
        </AnalyticsContext.Provider>
    );
}

export default AnalyticsProvider;
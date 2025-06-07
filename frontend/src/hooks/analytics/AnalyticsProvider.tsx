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
    const loader = useLoading();

    // Sprint Analysis Methods
    const getSprintAnalysis = useCallback(
        (sprintId: string): Promise<ISprintAnalysis> => {
            loader.add();
            return get(`/analytics/sprints/${sprintId}/analysis`)
                .catch((error) => {
                    console.error('Failed to fetch sprint analysis:', error);
                    throw error;
                })
                .finally(loader.done);
        },
        [get, loader]
    );

    const getSprintMetrics = useCallback(
        (sprintId: string): Promise<ISprintMetrics> => {
            loader.add();
            return get(`/analytics/sprints/${sprintId}/metrics`)
                .catch((error) => {
                    console.error('Failed to fetch sprint metrics:', error);
                    throw error;
                })
                .finally(loader.done);
        },
        [get, loader]
    );

    const getSprintMetadata = useCallback(
        (sprintId: string): Promise<ISprintMetadata> => {
            loader.add();
            return get(`/analytics/sprints/${sprintId}/metadata`)
                .catch((error) => {
                    console.error('Failed to fetch sprint metadata:', error);
                    throw error;
                })
                .finally(loader.done);
        },
        [get, loader]
    );

    // Task Analysis Methods
    const getTaskMetrics = useCallback(
        (sprintId: string): Promise<ITaskMetrics[]> => {
            loader.add();
            return get(`/analytics/sprints/${sprintId}/tasks/metrics`)
                .catch((error) => {
                    console.error('Failed to fetch task metrics:', error);
                    throw error;
                })
                .finally(loader.done);
        },
        [get, loader]
    );

    const getTaskMetricsById = useCallback(
        (sprintId: string, taskId: string): Promise<ITaskMetrics> => {
            loader.add();
            return get(`/analytics/sprints/${sprintId}/tasks/${taskId}/metrics`)
                .catch((error) => {
                    console.error('Failed to fetch task metrics by ID:', error);
                    throw error;
                })
                .finally(loader.done);
        },
        [get, loader]
    );

    // Burndown Chart Methods
    const getBurndownData = useCallback(
        (sprintId: string): Promise<IBurndownData> => {
            loader.add();
            return get(`/analytics/sprints/${sprintId}/burndown`)
                .catch((error) => {
                    console.error('Failed to fetch burndown data:', error);
                    throw error;
                })
                .finally(loader.done);
        },
        [get, loader]
    );

    // Comparative Analysis Methods
    const compareSprintToPrevious = useCallback(
        (sprintId: string): Promise<ISprintComparison> => {
            loader.add();
            return get(`/analytics/sprints/${sprintId}/comparison`)
                .catch((error) => {
                    console.error('Failed to fetch sprint comparison:', error);
                    throw error;
                })
                .finally(loader.done);
        },
        [get, loader]
    );

    const getSprintTrends = useCallback(
        (projectId: string, sprintCount = 3): Promise<ISprintTrends> => {
            loader.add();
            return get(`/analytics/projects/${projectId}/trends?sprintCount=${sprintCount}`)
                .catch((error) => {
                    console.error('Failed to fetch sprint trends:', error);
                    throw error;
                })
                .finally(loader.done);
        },
        [get, loader]
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
import {IBurndownData, ISprintInsight} from "../types/analytics.types.ts";

export function formatBurndownData(burndownData: IBurndownData) {
    const optimalMap = new Map(burndownData.optimalPath.map(point => [point.date, point.remainingWork]));
    const actualMap = new Map(burndownData.actualProgress.map(point => [point.date, point.remainingWork]));

    // Get all unique dates and sort them
    const allDates = Array.from(new Set([
        ...burndownData.optimalPath.map(p => p.date),
        ...burndownData.actualProgress.map(p => p.date)
    ])).sort((a, b) => a.localeCompare(b));

    return allDates.map(date => ({
        date: new Date(date).toLocaleDateString(),
        optimal: optimalMap.get(date) ?? null,
        actual: actualMap.get(date) ?? null
    }));
}

export function handleSprintInsight(insight: ISprintInsight): string {
    switch (insight.type) {
        case 'LowCompletionRate':
            return `Low completion rate: ${insight.completionRate}%`;
        case 'HighReopenRate':
            return `High reopen rate: ${insight.reopenedTasks} tasks reopened`;
        case 'LowCollaboration':
            return `Low collaboration score: ${insight.score}`;
        case 'FrequentReassignments':
            return `Frequent reassignments: ${insight.reassignedTasks} tasks reassigned`;
        case 'ScopeIncrease':
            return `Scope increase: ${insight.addedTasks} tasks added`;
    }
}

export interface MetricThreshold {
    readonly min: number;
    readonly color: ColorKey;
}

export const COLOR_CLASS_MAP = {
    'ap-mint': {
        bg: 'bg-ap-mint-100',
        bgDark: 'bg-ap-mint-600',
        border: 'border-ap-mint-200',
        text: 'text-ap-mint-800',
        textLight: 'text-ap-mint-600'
    },
    'ap-coral': {
        bg: 'bg-ap-coral-100',
        bgDark: 'bg-ap-coral-600',
        border: 'border-ap-coral-200',
        text: 'text-ap-coral-800',
        textLight: 'text-ap-coral-600'
    },
    'ap-cyan': {
        bg: 'bg-ap-cyan-100',
        bgDark: 'bg-ap-cyan-600',
        border: 'border-ap-cyan-200',
        text: 'text-ap-cyan-800',
        textLight: 'text-ap-cyan-600'
    },
    'ap-lavender': {
        bg: 'bg-ap-lavender-100',
        bgDark: 'bg-ap-lavender-600',
        border: 'border-ap-lavender-200',
        text: 'text-ap-lavender-800',
        textLight: 'text-ap-lavender-600'
    },
    'ap-onyx': {
        bg: 'bg-ap-onyx-100',
        bgDark: 'bg-ap-onyx-600',
        border: 'border-ap-onyx-200',
        text: 'text-ap-onyx-800',
        textLight: 'text-ap-onyx-600'
    }
} as const;

export type ColorKey = keyof typeof COLOR_CLASS_MAP;

// Helper function to get classes for a color
export const getColorClasses = (color: ColorKey) => COLOR_CLASS_MAP[color];

// Generic metric color mapper
export const getMetricColor = (value: number, thresholds: readonly MetricThreshold[]): ColorKey => {
    for (const {min, color} of thresholds) {
        if (value >= min) return color;
    }
    return thresholds[thresholds.length - 1].color; // fallback to last color
};

export const METRIC_COLOR_THRESHOLDS = {
    completionRate: [
        {min: 0.8, color: "ap-mint"},
        {min: 0.6, color: "ap-coral"},
        {min: 0, color: "ap-coral"}
    ],
    collaborationScore: [
        {min: 7, color: "ap-mint"},
        {min: 4, color: "ap-cyan"},
        {min: 0, color: "ap-coral"}
    ],
    reassignments: [
        {min: 3, color: "ap-coral"},
        {min: 0, color: "ap-mint"}
    ],
    reopenedTasks: [
        {min: 1, color: "ap-coral"},
        {min: 0, color: "ap-mint"}
    ]
} as const;

// Convenience functions for common metrics
export const getCompletionRateColor = (rate: number): ColorKey =>
    getMetricColor(rate, METRIC_COLOR_THRESHOLDS.completionRate);

export const getCollaborationScoreColor = (score: number): ColorKey =>
    getMetricColor(score, METRIC_COLOR_THRESHOLDS.collaborationScore);

export const getReopenedTasksColor = (count: number): ColorKey =>
    getMetricColor(count, METRIC_COLOR_THRESHOLDS.reopenedTasks);

export const getReassignmentsColor = (count: number): ColorKey =>
    getMetricColor(count, METRIC_COLOR_THRESHOLDS.reassignments);
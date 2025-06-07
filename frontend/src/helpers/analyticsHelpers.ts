import {IBurndownData} from "../types/analytics.types.ts";

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

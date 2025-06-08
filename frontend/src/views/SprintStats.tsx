import {useEffect, useState} from "react";
import {useParams} from "react-router";
import {CartesianGrid, Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis} from "recharts";
import useAnalytics from "../hooks/analytics/useAnalytics";
import useSprint from "../hooks/sprint/useSprint";
import ShowIf from "../components/generic/ShowIf";
import {IBurndownData, ISprintAnalysis} from "../types/analytics.types";
import {Tooltip as ReactTooltip} from "react-tooltip";
import ComplexityBar from "../components/analytics/ComplexityBar.tsx";
import {
    formatBurndownData,
    getCollaborationScoreColor,
    getCompletionRateColor,
    getReassignmentsColor,
    getReopenedTasksColor,
    handleSprintInsight
} from "../helpers/analyticsHelpers.ts";
import MetricCard from "../components/analytics/MetricCard.tsx";


function SprintStats() {
    const {sprintId} = useParams();
    const {sprint} = useSprint();
    const {getSprintAnalysis, getBurndownData} = useAnalytics();

    const [analysis, setAnalysis] = useState<ISprintAnalysis | null>(null);
    const [burndownData, setBurndownData] = useState<IBurndownData | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (!sprintId) return;

        setLoading(true);
        Promise.all([
            getSprintAnalysis(sprintId),
            getBurndownData(sprintId)
        ])
            .then(([analysisData, burndown]) => {
                setAnalysis(analysisData);
                setBurndownData(burndown);
            })
            .catch(console.error)
            .finally(() => setLoading(false));
    }, [sprintId, getSprintAnalysis, getBurndownData]);

    if (loading) {
        return (
            <div className="p-4 flex items-center justify-center h-96">
                <div className="text-ap-onyx-400">Loading sprint analytics...</div>
            </div>
        );
    }

    if (!analysis || !burndownData) {
        return (
            <div className="p-4 flex items-center justify-center h-96">
                <div className="text-ap-coral-600">Failed to load sprint analytics</div>
            </div>
        );
    }

    return (
        <div className="p-4 space-y-6">
            {/* Header */}
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-2xl font-semibold text-ap-onyx-800">Sprint Analytics</h1>
                    <ShowIf if={!!sprint}>
                        <p className="text-ap-onyx-400">{sprint?.name}</p>
                    </ShowIf>
                </div>
            </div>

            {/* Key Metrics Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                <MetricCard
                    title="Velocity"
                    value={analysis.metrics.velocity.toString()}
                    subtitle="Story Points"
                    color="ap-lavender"
                    tooltip="Total story points completed in this sprint"
                />
                <MetricCard
                    title="Completion Rate"
                    value={`${Math.round(analysis.metrics.completionRate)}%`}
                    subtitle="Tasks Completed"
                    color={getCompletionRateColor(analysis.metrics.completionRate)}
                    tooltip="Percentage of planned tasks that were completed"
                />
                <MetricCard
                    title="Avg Cycle Time"
                    value={Math.round(analysis.metrics.avgCycleTimeHours).toString()}
                    subtitle="Hours"
                    color="ap-cyan"
                    tooltip="Average time from task start to completion"
                />
                <MetricCard
                    title="Collaboration Score"
                    value={analysis.metrics.collaborationScore.toString()}
                    subtitle="Team Engagement"
                    color={getCollaborationScoreColor(analysis.metrics.collaborationScore)}
                    tooltip="Score based on comments, discussions, and team interactions"
                />
            </div>

            {/* Secondary Metrics Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                <MetricCard
                    title="Reopened Tasks"
                    value={analysis.metrics.numReopenedTasks.toString()}
                    subtitle="Tasks"
                    color={getReopenedTasksColor(analysis.metrics.numReopenedTasks)}
                    tooltip="Number of tasks that were reopened during the sprint"
                />
                <MetricCard
                    title="Scope Changes"
                    value={`+${analysis.metrics.numAddedTasksDuringSprint} / -${analysis.metrics.numRemovedTasksDuringSprint}`}
                    subtitle="Added / Removed"
                    color="ap-onyx"
                    tooltip="Tasks added or removed during the sprint"
                />
                <MetricCard
                    title="Reassignments"
                    value={analysis.metrics.numReassignedTasks.toString()}
                    subtitle="Tasks"
                    color={getReassignmentsColor(analysis.metrics.numReassignedTasks)}
                    tooltip="Number of tasks that were reassigned to different team members"
                />
            </div>

            {/* Burndown Chart */}
            <div className="bg-white p-6 rounded shadow border border-ap-onyx-50">
                <h2 className="text-lg font-semibold text-ap-onyx-800 mb-4">Burndown Chart</h2>
                <div className="h-96">
                    <ResponsiveContainer width="100%" height="100%">
                        <LineChart data={formatBurndownData(burndownData)}>
                            <CartesianGrid strokeDasharray="3 3" stroke="#c8cdd0"/>
                            <XAxis
                                dataKey="date"
                                stroke="#5f686d"
                                fontSize={12}
                            />
                            <YAxis
                                stroke="#5f686d"
                                fontSize={12}
                            />
                            <Tooltip
                                contentStyle={{
                                    backgroundColor: 'white',
                                    border: '1px solid #c8cdd0',
                                    borderRadius: '6px'
                                }}
                            />
                            <Line
                                type="monotone"
                                dataKey="optimal"
                                stroke="#7145d9"
                                strokeDasharray="5 5"
                                name="Ideal Burndown"
                                dot={false}
                            />
                            <Line
                                type="monotone"
                                dataKey="actual"
                                stroke="#ff4735"
                                strokeWidth={2}
                                name="Actual Progress"
                            />
                        </LineChart>
                    </ResponsiveContainer>
                </div>
            </div>

            {/* Task Complexity Breakdown */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="bg-white p-6 rounded shadow border border-ap-onyx-50">
                    <h2 className="text-lg font-semibold text-ap-onyx-800 mb-4">Task Complexity</h2>
                    <div className="space-y-3">
                        <ComplexityBar
                            label="Simple"
                            count={analysis.metrics.tasksByComplexity.simple}
                            total={analysis.metadata.totalTasks}
                            color="ap-mint"
                        />
                        <ComplexityBar
                            label="Moderate"
                            count={analysis.metrics.tasksByComplexity.moderate}
                            total={analysis.metadata.totalTasks}
                            color="ap-cyan"
                        />
                        <ComplexityBar
                            label="Complex"
                            count={analysis.metrics.tasksByComplexity.complex}
                            total={analysis.metadata.totalTasks}
                            color="ap-lavender"
                        />
                    </div>
                </div>

                {/* Sprint Insights */}
                <div className="bg-white p-6 rounded shadow border border-ap-onyx-50">
                    <h2 className="text-lg font-semibold text-ap-onyx-800 mb-4">Key Insights</h2>
                    <ShowIf if={analysis.insights.length == 0}>
                        <div className="text-ap-mint-600 text-sm">🎉 No issues detected - great sprint!</div>
                    </ShowIf>
                    <ShowIf if={analysis.insights.length > 0}>
                        <div className="space-y-2">
                            {analysis.insights.map((insight, index) => (
                                <div key={index} className="text-sm text-ap-coral-600 bg-ap-coral-50 p-2 rounded">
                                    {handleSprintInsight(insight)}
                                </div>
                            ))}
                        </div>
                    </ShowIf>
                </div>
            </div>

            {/* Team Performance */}
            <div className="bg-white p-6 rounded shadow border border-ap-onyx-50">
                <h2 className="text-lg font-semibold text-ap-onyx-800 mb-4">Team Performance</h2>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                    <div className="text-center">
                        <div className="text-2xl font-bold text-ap-lavender-800">{analysis.metadata.teamCapacity}</div>
                        <div className="text-sm text-ap-onyx-400">Team Capacity</div>
                    </div>
                    <div className="text-center">
                        <div
                            className="text-2xl font-bold text-ap-cyan-800">{Math.round(analysis.metrics.avgSubtaskCount * 10) / 10}</div>
                        <div className="text-sm text-ap-onyx-400">Avg Subtasks per Task</div>
                    </div>
                    <div className="text-center">
                        <div
                            className="text-2xl font-bold text-ap-mint-800">{analysis.metrics.numTasksWithComments}</div>
                        <div className="text-sm text-ap-onyx-400">Tasks with Comments</div>
                    </div>
                </div>
            </div>

            {/* React Tooltip */}
            <ReactTooltip id="metric-tooltip"/>
        </div>
    );
}

export default SprintStats;

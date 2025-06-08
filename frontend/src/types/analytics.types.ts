export interface IBurndownData {
    sprintId: string;
    totalWork: number;
    optimalPath: IBurndownPoint[];
    actualProgress: IBurndownPoint[];
    startDate: string;
    endDate: string;
}

export interface IBurndownPoint {
    date: string;
    remainingWork: number;
}

export interface ISprintAnalysis {
    sprintInfo: ISprintInfo;
    metrics: ISprintMetrics;
    metadata: ISprintMetadata;
    taskMetrics: ITaskMetrics[];
    insights: ISprintInsight[];
}

export interface ISprintInfo {
    id: string;
    name: string;
    startDate: string;
    endDate: string;
}

export interface ISprintMetrics {
    sprintId: string;
    velocity: number;
    durationDays: number;
    completionRate: number;
    avgCycleTimeHours: number;
    longestCycleTimeTaskId?: string;
    numReopenedTasks: number;
    numAddedTasksDuringSprint: number;
    numRemovedTasksDuringSprint: number;
    numReassignedTasks: number;
    numTasksWithComments: number;
    avgTaskCommentCount: number;
    tasksWithoutComments: number;
    collaborationScore: number;
    avgSubtaskCount: number;
    tasksByComplexity: ITaskComplexity;
    totalAssignmentChanges: number;
    totalStatusTransitions: number;
}

export interface ISprintMetadata {
    sprintId: string;
    teamCapacity: number;
    totalTasks: number;
    completedTasks: number;
    plannedPoints: number;
}

export interface ITaskMetrics {
    taskId: string;
    cycleTimeHours: number;
    wasReopened: boolean;
    wasAddedDuringSprint: boolean;
    wasRemovedDuringSprint: boolean;
    wasReassigned: boolean;
    commentCount: number;
    isCompleted: boolean;
    subtaskCount: number;
    timeToFirstComment?: number;
    lastActivityTime?: string;
    assignmentChanges: number;
    statusTransitions: number;
}

export type ISprintInsight =
    | { type: 'LowCompletionRate'; completionRate: number }
    | { type: 'HighReopenRate'; reopenedTasks: number }
    | { type: 'LowCollaboration'; score: number }
    | { type: 'FrequentReassignments'; reassignedTasks: number }
    | { type: 'ScopeIncrease'; addedTasks: number };

export interface ITaskComplexity {
    simple: number;
    moderate: number;
    complex: number;
}

export interface ISprintComparison {
    current: ISprintMetrics;
    previous: ISprintMetrics;
    velocityChange: number;
    completionRateChange: number;
    avgCycleTimeChange: number;
    collaborationScoreChange: number;
}

export interface ISprintTrends {
    sprints: ISprintMetrics[];
    avgVelocity: number;
    avgCompletionRate: number;
    avgCycleTime: number;
    avgCollaborationScore: number;
    velocityTrend: string;
    completionRateTrend: string;
}
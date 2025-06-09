import {FaClipboardList, FaEdit, FaPlay, FaSyncAlt, FaUsers} from "react-icons/fa";
import {IconType} from "react-icons";

export interface CeremonyTip {
    id: string;
    title: string;
    description: string;
    importance: string;
    bestPractices: string[];
    whenToUse: string;
    icon: IconType;
    iconColor: string;
}

export const ceremoniesTips: CeremonyTip[] = [
    {
        id: "sprint-planning",
        title: "Sprint Planning",
        description: "A collaborative meeting where the team selects work from the product backlog and creates a plan for the upcoming sprint.",
        importance: "Sets clear expectations and ensures everyone understands what needs to be accomplished during the sprint.",
        bestPractices: [
            "Review team velocity from previous sprints",
            "Break down user stories into manageable tasks",
            "Ensure story acceptance criteria are clear",
            "Consider writing acceptance criteria with the Given/When/Then format",
            "Consider team capacity and holidays"
        ],
        whenToUse: "At the beginning of each sprint cycle",
        icon: FaClipboardList,
        iconColor: "text-ap-lavender-600"
    },
    {
        id: "daily-standup",
        title: "Daily Standup",
        description: "A brief daily meeting where team members share progress, plans, and blockers.",
        importance: "Keeps the team synchronized, identifies blockers early, and promotes accountability.",
        bestPractices: [
            "Keep it timeboxed to 15 minutes maximum",
            "Focus on what was done, what's planned, and any blockers",
            "Address detailed discussions offline",
            "Ensure everyone participates actively"
        ],
        whenToUse: "Every working day at the same time",
        icon: FaUsers,
        iconColor: "text-ap-lavender-600"
    },
    {
        id: "sprint-review",
        title: "Sprint Review",
        description: "A demonstration of completed work to stakeholders and gathering feedback on the product increment.",
        importance: "Validates that the right features were built and gathers valuable stakeholder feedback.",
        bestPractices: [
            "Demo working software, not PowerPoint slides",
            "Invite relevant stakeholders and users",
            "Focus on value delivered to the customer",
            "Gather feedback for future improvements"
        ],
        whenToUse: "At the end of each sprint",
        icon: FaPlay,
        iconColor: "text-ap-lavender-600"
    },
    {
        id: "sprint-retrospective",
        title: "Sprint Retrospective",
        description: "A team reflection meeting to discuss what went well, what didn't, and how to improve.",
        importance: "Enables continuous improvement and team growth by addressing process issues proactively.",
        bestPractices: [
            "Create a safe environment for honest feedback",
            "Keep it blame-free and constructive",
            "Focus on actionable improvements",
            "Rotate facilitation among team members",
            "Follow up on previous retrospective actions"
        ],
        whenToUse: "After the sprint review, before the next planning",
        icon: FaSyncAlt,
        iconColor: "text-ap-lavender-600"
    },
    {
        id: "backlog-refinement",
        title: "Backlog Refinement",
        description: "Ongoing activity to review, estimate, and prioritize items in the product backlog.",
        importance: "Ensures the backlog is ready for sprint planning and reduces planning meeting duration.",
        bestPractices: [
            "Involve the whole development team",
            "Break down large items into smaller ones",
            "Add acceptance criteria to user stories",
            "Re-prioritize based on changing business needs"
        ],
        whenToUse: "Throughout the sprint as an ongoing activity",
        icon: FaEdit,
        iconColor: "text-ap-lavender-600"
    }
];
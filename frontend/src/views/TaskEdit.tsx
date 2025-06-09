import Button from "../components/generic/buttons/Button";
import useAssistant from "../hooks/assistant/useAssistant";
import useOpenAI from "../hooks/openai/useOpenAI";
import ShowIf from "../components/generic/ShowIf";
import useTask from "../hooks/task/useTask";
import { useState } from "react";
import { FaPlus, FaTshirt } from "react-icons/fa";
import useSubTask from "../hooks/subtask/useSubTask";
import SubTaskBox from "../components/sprint/SubTaskBox";
import NewSubTaskModal from "../components/sprint/NewSubTaskModal";
import Comments from "../components/comment/Comments";
import CommentProvider from "../hooks/comment/CommentProvider";
import { PiOpenAiLogoDuotone } from "react-icons/pi";
import Markdown from "react-markdown";
import { Avatar, AvatarGroup } from "@mui/material";
import AssigneeModal from "../components/sprint/AssigneeModal";
import CustomSelect from "../components/generic/select/CustomSelect";
import useCurrentProject from "../hooks/projects/useCurrentProject";
import { ITaskRequest, PointEstimate, TshirtEstimate } from "../types/story.types";
import { EstimationMethod } from "../types/project.types";
import { notifyError } from "../helpers/notify";
import Tooltip from "../components/generic/tooltips/Tooltip";
import {Tooltip as ReactTooltip} from "react-tooltip";

function TaskEdit() {
  const { loadAssistant } = useAssistant();
  const { sendMessage } = useOpenAI();
  const { task, updateTask } = useTask();
  const { subtasks } = useSubTask();
  const { project } = useCurrentProject();
  const [showCreateNewTaskModal, setShowCreateNewTaskModal] = useState(false);
  const [showAssigneeModal, setShowAssigneeModal] = useState(false);
  const [openAIResponse, setOpenAIResponse] = useState<string | undefined>(undefined);
  const [helperAsked, setHelperAsked] = useState(false);

  if (!task) return <div>Loading...</div>;

  const tshirtEstimateOptions = [
    {
      label: (
        <span className="flex items-center gap-2">
          <FaTshirt className="text-ap-lavender-500" />
          XS
        </span>
      ),
      value: TshirtEstimate.XSMALL
    },
    {
      label: (
        <span className="flex items-center gap-2">
          <FaTshirt className="text-ap-lavender-500" />S
        </span>
      ),
      value: TshirtEstimate.SMALL
    },
    {
      label: (
        <span className="flex items-center gap-2">
          <FaTshirt className="text-ap-lavender-500" />M
        </span>
      ),
      value: TshirtEstimate.MEDIUM
    },
    {
      label: (
        <span className="flex items-center gap-2">
          <FaTshirt className="text-ap-lavender-500" />L
        </span>
      ),
      value: TshirtEstimate.LARGE
    },
    {
      label: (
        <span className="flex items-center gap-2">
          <FaTshirt className="text-ap-lavender-500" />
          XL
        </span>
      ),
      value: TshirtEstimate.XLARGE
    }
  ];

  const storyPointEstimateOptions = [
    { label: "1", value: PointEstimate.POINT_1 },
    { label: "2", value: PointEstimate.POINT_2 },
    { label: "3", value: PointEstimate.POINT_3 },
    { label: "5", value: PointEstimate.POINT_5 },
    { label: "8", value: PointEstimate.POINT_8 },
    { label: "13", value: PointEstimate.POINT_13 },
    { label: "21", value: PointEstimate.POINT_21 }
  ];

  const handleBreakdown = (assistantName: string) => {
    setOpenAIResponse(undefined);
    loadAssistant(assistantName)
      .then((assistant) => {
        console.log("Loaded AI assistant:", assistant);
        const systemMessage = {
          role: "system",
          content: assistant?.prompt ?? "You are a helpful assistant."
        };

        const userMessage = {
          role: "user",
          content: JSON.stringify({
            task_header: task.title,
            task_description: task.description
          })
        };

        const body = {
          model: assistant?.model ?? "gpt-4o-mini",
          messages: [systemMessage, userMessage],
          stream: true
        };

        const handleChunk = (chunk: string) => {
          setOpenAIResponse((prev) => (prev ? prev + chunk : chunk));
        };

        return sendMessage(body, handleChunk)
          .then((response) => {
            console.log("Final response from OpenAI:", response);
          });
      })
      .catch(() => {
        notifyError("Error loading AI assistant or sending message");
      });
  };

  const handleUpdateEstimateTshirt = (estimate: TshirtEstimate) => {
    const tmp: ITaskRequest = {
      assigneeIds: task.assignees.map((a) => a.id),
      sprintColumnId: task.sprintColumnId,
      storyId: task.storyId,
      title: task.title,
      description: task.description,
      estimateTshirt: estimate
    };
    updateTask(tmp, task.id);
  };

  const handleUpdateEstimatePoints = (estimate: PointEstimate) => {
    const tmp: ITaskRequest = {
      assigneeIds: task.assignees.map((a) => a.id),
      sprintColumnId: task.sprintColumnId,
      storyId: task.storyId,
      title: task.title,
      description: task.description,
      estimatePoints: estimate
    };
    updateTask(tmp, task.id);
  };

  return (
    <div className="flex h-[calc(100vh-140px)] overflow-y-auto w-full relative">
      <div className="flex flex-col w-full h-full">
        <div className="flex flex-col gap-4 p-4 min-w-1/2 border-r border-ap-onyx-50/50">
          <div className="sticky top-0 bg-white border-b border-ap-onyx-50 pb-4 flex flex-col gap-4">
            <div className="text-ap-onyx-400 text-sm">{task.id}</div>
            <div className="flex justify-between">
              <div className="text-ap-onyx-800 font-bold truncate">{task.title}</div>
              <ShowIf if={project?.estimationMethod === EstimationMethod.TSHIRT_SIZES}>
                <label>
                  <div className="flex items-center gap-1">
                    <span className="text-xs">Estimate (T-shirt size)</span>
                    <Tooltip
                      text="Use larger sizes for tasks that seem bigger or more complex. XS means small effort, while XL suggests much more work or uncertainty."
                      className="ml-1"
                      id="tshirtEstimateTooltip"
                    />
                  </div>
                  <CustomSelect
                    options={tshirtEstimateOptions}
                    value={
                      tshirtEstimateOptions.find((o) => o.value === task.estimateTshirt) ?? null
                    }
                    onChange={(o) => {
                      if (o) {
                        handleUpdateEstimateTshirt(o.value);
                      }
                    }}
                    className="w-26"
                  />
                </label>
              </ShowIf>
              <ShowIf if={project?.estimationMethod === EstimationMethod.STORY_POINTS}>
                <label>
                  <div className="flex items-center gap-1">
                    <span className="text-xs">Estimate (Story points)</span>
                    <Tooltip
                      text="Story points reflect relative effort using the Fibonacci sequence. Higher numbers mean more work, complexity, or uncertainty."
                      className="ml-1"
                      id="storyPointEstimateTooltip"
                    />
                  </div>
                  <CustomSelect
                    options={storyPointEstimateOptions}
                    value={
                      storyPointEstimateOptions.find((o) => o.value === task.estimatePoints) ?? null
                    }
                    onChange={(o) => {
                      if (o) {
                        handleUpdateEstimatePoints(o.value);
                      }
                    }}
                    classNames={{
                      menuList: (base) => `${base} text-right`
                    }}
                    className="w-34 text-right"
                    isSearchable={false}
                  />
                </label>
              </ShowIf>
            </div>
          </div>
          <ShowIf if={!!task.description && task.description !== ""}>
            <div className="text-ap-onyx-800 border-b pb-2 border-ap-onyx-50  whitespace-pre-line">
              {task.description}
            </div>
          </ShowIf>
          <div className="flex justify-between items-center">
            <div className="flex gap-4">
              <Button
                text={
                  <span className="flex items-center gap-2 truncate">
                    <FaPlus className="text-ap-lavender-800" />
                    Add subtask
                  </span>
                }
                className="bg-white px-10 border border-ap-onyx-50/50 w-fit"
                onClick={() => setShowCreateNewTaskModal(true)}
              />
              <ShowIf if={!helperAsked}>
                <Button
                  text={
                    <span className="flex items-center gap-2">
                      <PiOpenAiLogoDuotone className="flex-shrink-0 text-xl" />
                      Help
                    </span>
                  }
                  className="bg-gradient-to-br to-ap-lavender-900 from-ap-cyan-900 text-white px-10"
                  data-tooltip-id="taskHelperTooltip"
                  data-tooltip-content="Click to get AI help for task breakdown"
                  onClick={() => {
                    handleBreakdown("task_helper");
                    setHelperAsked(true);
                  }}
                />
                <ReactTooltip id="taskHelperTooltip" />
              </ShowIf>
              <ShowIf if={helperAsked}>
                <Button
                  text={
                    <span className="flex items-center gap-2">
                      <PiOpenAiLogoDuotone className="flex-shrink-0 text-xl" />
                      More help
                    </span>
                  }
                  className="bg-gradient-to-br to-ap-lavender-900 from-ap-cyan-900 text-white px-10"
                  data-tooltip-id="taskBreakTheGlassTooltip"
                  data-tooltip-content="Click to ask AI to break down the task into concrete subtasks"
                  onClick={() => {
                    handleBreakdown("task_break_the_glass");
                  }}
                />
                <ReactTooltip id="taskBreakTheGlassTooltip" />
              </ShowIf>
            </div>
            <div className="flex gap-2">
              <div
                className="cursor-pointer"
                onClick={() => setShowAssigneeModal(true)}
              >
                <ShowIf if={task.assignees.length > 0}>
                  <div className="text-xs">Assignees</div>
                  <AvatarGroup
                    max={3}
                    spacing={"small"}
                  >
                    {task.assignees.map((a) => (
                      <Avatar
                        key={"assignee" + a.id}
                        src={a.avatarUrl}
                      />
                    ))}
                  </AvatarGroup>
                </ShowIf>
                <ShowIf if={task.assignees.length === 0}>
                  <div className="text-xs text-right text-ap-lavender-900 hover:underline">
                    Assign
                  </div>
                </ShowIf>
              </div>
            </div>
          </div>
          <div>
            <ShowIf if={subtasks.length > 0}>
              <div className="text-sm font-semibold">Subtasks</div>
              <div className="max-h-[calc(100vh-400px)] flex flex-col gap-2 overflow-y-auto">
                {subtasks.map((subtask) => (
                  <SubTaskBox
                    key={subtask.id}
                    subtask={subtask}
                  />
                ))}
              </div>
            </ShowIf>
          </div>
        </div>
        <div className="p-2 flex flex-col gap-2 border-r border-ap-onyx-50/50">
          <CommentProvider taskId={task.id}>
            <Comments task={task} />
          </CommentProvider>
        </div>
      </div>
      <div className="w-1/2 flex-shrink-0">
        <ShowIf if={!!openAIResponse}>
          <div className="flex flex-col">
            <div className="sticky top-0 flex gap-4 text-xl items-center bg-gradient-to-br p-4 to-ap-lavender-900 from-ap-cyan-900 text-white">
              <PiOpenAiLogoDuotone className="flex-shrink-0" />
              Task breakdown help
            </div>
            <div className="p-2 text-sm">
              <Markdown>{openAIResponse}</Markdown>
            </div>
          </div>
        </ShowIf>
      </div>

      <ShowIf if={showCreateNewTaskModal}>
        <NewSubTaskModal
          task={task}
          show={showCreateNewTaskModal}
          onClose={() => setShowCreateNewTaskModal(false)}
        />
      </ShowIf>
      <ShowIf if={showAssigneeModal}>
        <AssigneeModal
          show={showAssigneeModal}
          onClose={() => setShowAssigneeModal(false)}
          task={task}
        />
      </ShowIf>
    </div>
  );
}
export default TaskEdit;

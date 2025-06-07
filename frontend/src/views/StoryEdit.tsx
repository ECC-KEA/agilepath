import StatusLabel from "../components/status/StatusLabel";
import { ITask, ITaskRequest, PointEstimate, Status, TshirtEstimate } from "../types/story.types";
import Button from "../components/generic/buttons/Button";
import NewStoryTaskModal from "../components/project/NewStoryTaskModal";
import Comments from "../components/comment/Comments";
import CommentProvider from "../hooks/comment/CommentProvider";
import useAssistant from "../hooks/assistant/useAssistant";
import useOpenAI from "../hooks/openai/useOpenAI";
import ShowIf from "../components/generic/ShowIf";
import useStory from "../hooks/story/useStory";
import { useLoading } from "../hooks/utils/loading/useLoading";
import { useCallback, useState } from "react";
import { FaPlus, FaTshirt } from "react-icons/fa";
import { notifyError } from "../helpers/notify";
import { PiOpenAiLogoDuotone } from "react-icons/pi";
import Markdown from "react-markdown";
import CustomSelect from "../components/generic/select/CustomSelect";
import useCurrentProject from "../hooks/projects/useCurrentProject";
import { EstimationMethod } from "../types/project.types";
import { useApi } from "../hooks/utils/useApi";
import { createdAtSortPredicate } from "../helpers/timeHelpers";

function StoryEdit() {
  const { loadAssistant } = useAssistant();
  const { sendMessage } = useOpenAI();
  const { story } = useStory();
  const [showCreateNewTaskModal, setShowCreateNewTaskModal] = useState(false);
  const [openAIResponse, setOpenAIResponse] = useState<string | undefined>(undefined);
  const [helperAsked, setHelperAsked] = useState(false);

  if (!story) {
    return <div>Loading...</div>;
  }

  const handleBreakdown = (assistantName: string) => {
    setOpenAIResponse(undefined);
    loadAssistant(assistantName)
      .then((loadedAssistant) => {
        console.log("Loaded AI assistant:", loadedAssistant);
        const systemMessage = {
          role: "system",
          content: loadedAssistant?.prompt ?? "You are a helpful assistant."
        };

        const userMessage = {
          role: "user",
          content: JSON.stringify({
            task_header: story.title,
            task_description: story.description
          })
        };

        const body = {
          model: loadedAssistant?.model ?? "gpt-4o-mini",
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

  return (
    <div className="flex h-[calc(100vh-140px)] overflow-y-auto w-full relative">
      <div className="flex flex-col w-full h-full">
        <div className="flex flex-col gap-4 p-4 min-w-1/2 border-r border-ap-onyx-50/50">
          <div className="sticky top-0 bg-white border-b border-ap-onyx-50 pb-4 flex flex-col gap-4">
            <div className="text-ap-onyx-400 text-sm">{story.id}</div>
            <div className="flex gap-2">
              <StatusLabel
                status={story.status as Status}
                className="w-fit h-fit"
              />
              <div className="text-ap-onyx-800 font-bold">{story.title}</div>
            </div>
          </div>
          <ShowIf if={!!story.description && story.description !== ""}>
            <div className="text-ap-onyx-800 border-b pb-2 border-ap-onyx-50  whitespace-pre-line">
              {story.description}
            </div>
          </ShowIf>
          <div className="flex flex-row gap-4">
            <Button
              text={
                <span className="flex items-center gap-2 truncate">
                  <FaPlus className="text-ap-lavender-800" />
                  Add task
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
                title="Click to get AI help for Story breakdown"
                onClick={() => {
                  handleBreakdown("story_helper");
                  setHelperAsked(true);
                }}
              />
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
                title="Click to ask AI to break down the story into concrete tasks"
                onClick={() => {
                  handleBreakdown("story_break_the_glass");
                }}
              />
            </ShowIf>
          </div>
          <div>
            <div className="text-sm font-semibold">Tasks</div>
            <div className="max-h-[calc(100vh-400px)] flex flex-col gap-2 overflow-y-auto">
              {story.tasks.sort(createdAtSortPredicate).map((t) => (
                <TaskListItem
                  key={"storytask" + t.id}
                  task={t}
                />
              ))}
            </div>
          </div>
        </div>
        <div className="p-2 flex flex-col gap-2 border-r border-ap-onyx-50/50">
          <CommentProvider storyId={story.id}>
            <Comments story={story} />
          </CommentProvider>
        </div>
      </div>
      <div className="w-1/2 flex-shrink-0">
        <ShowIf if={!!openAIResponse}>
          <div className="flex flex-col">
            <div className="sticky top-0 flex gap-4 text-xl items-center bg-gradient-to-br p-4 to-ap-lavender-900 from-ap-cyan-900 text-white">
              <PiOpenAiLogoDuotone className="flex-shrink-0" />
              Story breakdown help
            </div>
            <div className="p-2 text-sm">
              <Markdown>{openAIResponse}</Markdown>
            </div>
          </div>
        </ShowIf>
      </div>

      <ShowIf if={showCreateNewTaskModal}>
        <NewStoryTaskModal
          show={showCreateNewTaskModal}
          onClose={() => {
            setShowCreateNewTaskModal(false);
          }}
        />
      </ShowIf>
    </div>
  );
}

interface TaskListItemProps {
  task: ITask;
}

function TaskListItem(props: Readonly<TaskListItemProps>) {
  const { put } = useApi();
  const { project } = useCurrentProject();
  const { getStories } = useStory();

  const updateTask = useCallback(
    (task: ITaskRequest, id: string) => {
      return put(`/tasks/${id}`, task).then(getStories);
    },
    [put]
  );

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
  const handleUpdateEstimateTshirt = (estimate: TshirtEstimate) => {
    const tmp: ITaskRequest = {
      assigneeIds: props.task.assignees.map((a) => a.id),
      sprintColumnId: props.task.sprintColumnId,
      storyId: props.task.storyId,
      title: props.task.title,
      description: props.task.description,
      estimateTshirt: estimate
    };
    updateTask(tmp, props.task.id);
  };

  const handleUpdateEstimatePoints = (estimate: PointEstimate) => {
    const tmp: ITaskRequest = {
      assigneeIds: props.task.assignees.map((a) => a.id),
      sprintColumnId: props.task.sprintColumnId,
      storyId: props.task.storyId,
      title: props.task.title,
      description: props.task.description,
      estimatePoints: estimate
    };
    updateTask(tmp, props.task.id);
  };
  return (
    <div
      className={`
        p-2  rounded shadow w-lg h-20 bg-ap-onyx-50/20
      `}
    >
      <div className="flex justify-between items-center text-sm">
        <div className="truncate w-20">#{props.task.id}</div>
      </div>
      <div className="flex justify-between">
        <div
          className="my-2
         mx-4 max-w-80 line-clamp-2 truncate text-left"
        >
          {props.task.title}
        </div>
        <ShowIf if={project?.estimationMethod === EstimationMethod.TSHIRT_SIZES}>
          <label className="flex items-center gap-2">
            <span className="text-xs">Estimate</span>
            <CustomSelect
              options={tshirtEstimateOptions}
              value={
                tshirtEstimateOptions.find((o) => o.value === props.task.estimateTshirt) ?? null
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
          <label className="flex items-center gap-2">
            <span className="text-xs">Estimate</span>
            <CustomSelect
              options={storyPointEstimateOptions}
              value={
                storyPointEstimateOptions.find((o) => o.value === props.task.estimatePoints) ?? null
              }
              onChange={(o) => {
                if (o) {
                  handleUpdateEstimatePoints(o.value);
                }
              }}
              classNames={{
                menuList: (base) => `${base} text-right`
              }}
              className="w-20 text-right"
              isSearchable={false}
            />
          </label>
        </ShowIf>
      </div>
    </div>
  );
}

export default StoryEdit;

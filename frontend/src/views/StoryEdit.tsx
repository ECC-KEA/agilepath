import StatusLabel from "../components/status/StatusLabel";
import { ITask, Status } from "../types/story.types";
import Button from "../components/generic/buttons/Button";
import NewStoryTaskModal from "../components/project/NewStoryTaskModal";
import Comments from "../components/comment/Comments";
import CommentProvider from "../hooks/comment/CommentProvider";
import useAssistant from "../hooks/assistant/useAssistant";
import useOpenAI from "../hooks/openai/useOpenAI";
import ShowIf from "../components/generic/ShowIf";
import useStory from "../hooks/story/useStory";
import { useLoading } from "../hooks/utils/loading/useLoading";
import { useState } from "react";
import { FaPlus } from "react-icons/fa";
import { notifyError } from "../helpers/notify";
import { PiOpenAiLogoDuotone } from "react-icons/pi";
import Markdown from "react-markdown";

function StoryEdit() {
  const { assistant } = useAssistant();
  const { sendMessage } = useOpenAI();
  const { story } = useStory();
  const loader = useLoading();
  const [showCreateNewTaskModal, setShowCreateNewTaskModal] = useState(false);
  const [openAIResponse, setOpenAIResponse] = useState<string | undefined>(undefined);

  if (!story) {
    return <div>Loading...</div>;
  }

  const handleBreakdown = () => {
    loader.add();
    const systemMessage = {
      role: "system",
      content: assistant?.prompt ?? "You are a helpful assistant."
    };

    const userMessage = {
      role: "user",
      content: JSON.stringify({
        task_header: story.title,
        task_description: story.description
      })
    };

    const body = {
      model: assistant?.model ?? "gpt-4o-mini",
      messages: [systemMessage, userMessage],
      stream: true
    };

    const handleChunk = (chunk: string) => {
      setOpenAIResponse((prev) => {
        if (prev) {
          return prev + chunk;
        } else {
          return chunk;
        }
      });
    };

    sendMessage(body, handleChunk)
      .then((response) => {
        console.log("Final response from OpenAI:", response);
      })
      .catch(() => {
        notifyError("Error getting AI assistance");
      })
      .finally(() => {
        loader.done();
      });
  };

  return (
    <div className="flex h-[calc(100vh-140px)] overflow-y-auto w-full divide-x divide-ap-onyx-50/50 relative">
      <div className="flex flex-col w-full h-full">
        <div className="flex flex-col gap-4 p-4 min-w-1/2">
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
            <Button
              text={
                <span className="flex items-center gap-2">
                  <PiOpenAiLogoDuotone className="flex-shrink-0 text-xl" />
                  Help
                </span>
              }
              className="bg-gradient-to-br to-ap-lavender-900 from-ap-cyan-900 text-white px-10"
              title="Click to get AI help for Story breakdown"
              onClick={handleBreakdown}
            />
          </div>
          <div>
            <div className="text-sm font-semibold">Tasks</div>
            <div className="max-h-[calc(100vh-400px)] flex flex-col gap-2 overflow-y-auto">
              {story.tasks.map((t) => (
                <TaskListItem
                  key={"storytask" + t.id}
                  task={t}
                />
              ))}
            </div>
          </div>
        </div>
        <div className="p-2 flex flex-col gap-2">
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
            <div className="p-2 text-sm whitespace-pre-line">
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
  return (
    <div
      className={`
        p-2  rounded shadow w-80 h-20 bg-ap-onyx-50/20
      `}
    >
      <div className="flex justify-between items-center text-sm">
        <div className="truncate w-20">#{props.task.id}</div>
      </div>
      <div className="my-2 mx-4 max-w-64 line-clamp-2 text-left">{props.task.title}</div>
    </div>
  );
}

export default StoryEdit;

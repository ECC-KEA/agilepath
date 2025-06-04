import Button from "../components/generic/buttons/Button";
import useAssistant from "../hooks/assistant/useAssistant";
import useOpenAI from "../hooks/openai/useOpenAI";
import ShowIf from "../components/generic/ShowIf";
import useTask from "../hooks/task/useTask";
import { useLoading } from "../hooks/utils/loading/useLoading";
import { useState } from "react";
import { FaPlus } from "react-icons/fa";
import useSubTask from "../hooks/subtask/useSubTask";
import SubTaskBox from "../components/sprint/SubTaskBox";
import NewSubTaskModal from "../components/sprint/NewSubTaskModal";
import Comments from "../components/comment/Comments";
import CommentProvider from "../hooks/comment/CommentProvider";
import { PiOpenAiLogoDuotone } from "react-icons/pi";
import Markdown from "react-markdown";
import { Avatar, AvatarGroup } from "@mui/material";
import AssigneeModal from "../components/sprint/AssigneeModal";

function TaskEdit() {
  const { assistant } = useAssistant();
  const { sendMessage } = useOpenAI();
  const { task } = useTask();
  const { subtasks } = useSubTask();
  const loader = useLoading();
  const [showCreateNewTaskModal, setShowCreateNewTaskModal] = useState(false);
  const [showAssigneeModal, setShowAssigneeModal] = useState(false);
  const [openAIResponse, setOpenAIResponse] = useState<string | undefined>(undefined);
  if (!task) return <div>Loading...</div>;

  const handleBreakdown = () => {
    loader.add();
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
      setOpenAIResponse((prev) => {
        if (prev) {
          return prev + chunk;
        } else {
          return chunk;
        }
      });
    };

    sendMessage(body, handleChunk)
      .catch((error) => {
        console.error("Error sending message to OpenAI:", error);
      })
      .finally(loader.done);
  };

  return (
    <div className="flex h-[calc(100vh-140px)] overflow-y-auto w-full relative">
      <div className="flex flex-col w-full h-full">
        <div className="flex flex-col gap-4 p-4 min-w-1/2 border-r border-ap-onyx-50/50">
          <div className="sticky top-0 bg-white border-b border-ap-onyx-50 pb-4 flex flex-col gap-4">
            <div className="text-ap-onyx-400 text-sm">{task.id}</div>
            <div className="flex gap-2">
              <div className="text-ap-onyx-800 font-bold">{task.title}</div>
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

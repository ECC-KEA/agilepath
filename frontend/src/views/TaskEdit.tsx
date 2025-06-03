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

function TaskEdit() {
  const { assistant } = useAssistant();
  const { sendMessage } = useOpenAI();
  const { task } = useTask();
  const { subtasks } = useSubTask();
  const loader = useLoading();
  const [showCreateNewTaskModal, setShowCreateNewTaskModal] = useState(false);
  const [OpenAIResponse, setOpenAIResponse] = useState<string | undefined>(undefined);
  if (!task) return <div>Loading...</div>;
  console.log(task);

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
    <div className="flex flex-row gap-4">
      <div className="flex flex-col gap-4 p-4 w-2/3">
        <div className="flex gap-4">
          <div className="text-ap-onyx-800 font-bold">{task.title}</div>
          <div className="text-ap-onyx-400 text-sm">{task.id}</div>
        </div>
        <div className="text-ap-onyx-800 border-t border-b pt-2 pb-2 border-ap-onyx-50  whitespace-pre-line">
          {task.description}
        </div>
        <ShowIf if={subtasks.length > 0}>
          <div className="text-ap-onyx-800 font-bold">Subtasks</div>
          <div className="flex flex-col gap-2">
            {subtasks.map((subtask) => (
              <SubTaskBox
                key={subtask.id}
                subtask={subtask}
              />
            ))}
          </div>
        </ShowIf>
        <div className="flex flex-row gap-4">
          <Button
            text={
              <span className="flex items-center gap-2">
                <FaPlus className="text-ap-lavender-800" />
                Add subtask
              </span>
            }
            className="bg-white px-10 border border-ap-onyx-50 w-fit"
            onClick={() => setShowCreateNewTaskModal(true)}
          />
          <Button
            text={
              <span className="flex items-center gap-2">
                Help breaking this task down into subtasks
              </span>
            }
            className="bg-white px-10 border border-ap-onyx-50 w-fit"
            onClick={handleBreakdown}
          />
        </div>
      </div>
      <div className="w-min-1/3 h-[calc(100vh-200px)] overflow-y-auto">
        <ShowIf if={!!OpenAIResponse}>
          <div className="flex flex-col gap-4 border-l border-ap-onyx-50/50 p-4">
            <div className="font-bold">Breaking down task into subtasks</div>
            <div className="text-ap-onyx-800  border-ap-onyx-400 whitespace-pre-line text-sm">
              {OpenAIResponse}
            </div>
          </div>
        </ShowIf>
        <CommentProvider taskId={task.id}>
          <Comments task={task} />
        </CommentProvider>
      </div>

      <ShowIf if={showCreateNewTaskModal}>
        <NewSubTaskModal
          task={task}
          show={showCreateNewTaskModal}
          onClose={() => setShowCreateNewTaskModal(false)}
        />
      </ShowIf>
    </div>
  );
}
export default TaskEdit;

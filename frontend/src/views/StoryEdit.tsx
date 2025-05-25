import StatusLabel from "../components/status/StatusLabel";
import { Status } from "../types/story.types";
import Button from "../components/generic/buttons/Button";
import NewStoryTaskModal from "../components/project/NewStoryTaskModal";
import useAssistant from "../hooks/assistant/useAssistant";
import useOpenAI from "../hooks/openai/useOpenAI";
import ShowIf from "../components/generic/ShowIf";
import useStory from "../hooks/story/useStory";
import { useLoading } from "../hooks/utils/loading/useLoading";
import { useState } from "react";
import {FaPlus} from "react-icons/fa";


function StoryEdit() {
  const { assistant } = useAssistant();
  const { sendMessage } = useOpenAI();
  const { story } = useStory();
  const loader = useLoading();
  const [showCreateNewTaskModal, setShowCreateNewTaskModal] = useState(false);
  const [OpenAIResponse, setOpenAIResponse] = useState<string | undefined>(undefined);

  
  if (!story) {
    return <div>Loading...</div>;
  }

  const handleBreakdown = () => {
  loader.add();
  const systemMessage = {
    role: "system",
    content: assistant?.prompt ?? "You are a helpful assistant.",
  };

  const userMessage = {
    role: "user",
    content: JSON.stringify({
      task_header: story.title,
      task_description: story.description,
    }),
  };

  const body = {
    model: assistant?.model ?? "gpt-4o-mini",
    messages: [systemMessage, userMessage],
    stream: true, 
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
    .then(() => {
      loader.done();
    })
    .catch((error) => {
      console.error("Error sending message to OpenAI:", error);
    });
};


  return (
    <div className="flex flex-row gap-4">
      <div className="flex flex-col gap-4 p-4 w-min-2/3">
        <div className="flex gap-4">
          <div className="text-ap-onyx-800 font-bold">{story.title}</div>
          {/* TODO: replace with issue id */}
          <div className="text-ap-onyx-400 text-sm">{story.id}</div>
        </div>
        <StatusLabel status={story.status as Status} className="w-fit" />
        <div className="text-ap-onyx-800 border-t border-b pt-2 pb-2 border-ap-onyx-50  whitespace-pre-line">
          {story.description}
        </div>
        <div className="flex flex-row gap-4">
          <Button 
            text={
              <span className="flex items-center gap-2">
                <FaPlus className="text-ap-lavender-800" />
                Add task
              </span>
            }
            className="bg-white px-10 border border-ap-onyx-50 w-fit"
            onClick={() => setShowCreateNewTaskModal(true)}
          />
          <Button
            text={
              <span className="flex items-center gap-2">
                Help breaking this story down into tasks
              </span>
            }
            className="bg-white px-10 border border-ap-onyx-50 w-fit"
            onClick={handleBreakdown}
          />
        </div>
      </div>
      <div className="w-min-1/3">
        <ShowIf if={!!OpenAIResponse}>
          <div className="flex flex-col gap-4 border-l border-ap-onyx-50/50 p-4">
            <div className="font-bold">Breaking down story into tasks</div>
            <div className="text-ap-onyx-800  border-ap-onyx-400 whitespace-pre-line text-sm">{OpenAIResponse}</div>
          </div>
        </ShowIf> 
      </div>

      <ShowIf if={showCreateNewTaskModal}>
          <NewStoryTaskModal 
            show={showCreateNewTaskModal}
            onClose={() => setShowCreateNewTaskModal(false)}
          />
      </ShowIf>
    </div>
  );
}

export default StoryEdit;
import StatusLabel from "../components/status/StatusLabel";
import { Status } from "../types/story.types";
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

function StoryEdit() {
  const { assistant } = useAssistant();
  const { sendMessage } = useOpenAI();
  const { story, getStories } = useStory();
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
  console.log(story);

  return (
    <div className="flex flex-col h-full overflow-y-auto">
      <div className="flex">
        <div className="flex flex-col gap-4 p-4 min-w-1/2 overflow-y-auto">
          <div className="flex gap-4">
            <div className="text-ap-onyx-800 font-bold">{story.title}</div>
            {/* TODO: replace with issue id */}
            <div className="text-ap-onyx-400 text-sm">{story.id}</div>
          </div>
          <StatusLabel
            status={story.status as Status}
            className="w-fit"
          />
          <ShowIf if={!!story.description && story.description !== ""}>
            <div className="text-ap-onyx-800 border-t border-b pt-2 pb-2 border-ap-onyx-50  whitespace-pre-line">
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
              className="bg-white px-10 border border-ap-onyx-50 w-fit"
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
          <div className="max-h-[calc(100vh-400px)]">
            {story.tasks.map((t) => (
              <div key={"storytask" + t.id}>{t.title}</div>
            ))}
          </div>
        </div>
        <div className="w-min-1/3 max-h-[calc(100vh-400px)] overflow-y-auto">
          <ShowIf if={!!OpenAIResponse || true}>
            <div className="flex flex-col gap-4 border-l border-ap-onyx-50/50 p-4">
              <div className="font-bold">Breaking down task into subtasks</div>
              <div className="text-ap-onyx-800  border-ap-onyx-400 whitespace-pre-line text-sm">
                {/* {OpenAIResponse} */}
                Lorem ipsum dolor sit amet consectetur adipisicing elit. Pariatur doloremque magnam
                atque rerum beatae obcaecati ratione ad, veritatis provident dicta eos nostrum,
                culpa suscipit quisquam nisi quaerat quod nobis aperiam! Lorem ipsum dolor sit amet
                consectetur adipisicing elit. Pariatur doloremque magnam atque rerum beatae
                obcaecati ratione ad, veritatis provident dicta eos nostrum, culpa suscipit quisquam
                nisi quaerat quod nobis aperiam! Lorem ipsum dolor sit amet consectetur adipisicing
                elit. Pariatur doloremque magnam atque rerum beatae obcaecati ratione ad, veritatis
                provident dicta eos nostrum, culpa suscipit quisquam nisi quaerat quod nobis
                aperiam! Lorem ipsum dolor sit amet consectetur adipisicing elit. Pariatur
                doloremque magnam atque rerum beatae obcaecati ratione ad, veritatis provident dicta
                eos nostrum, culpa suscipit quisquam nisi quaerat quod nobis aperiam! Lorem ipsum
                dolor sit amet consectetur adipisicing elit. Pariatur doloremque magnam atque rerum
                beatae obcaecati ratione ad, veritatis provident dicta eos nostrum, culpa suscipit
                quisquam nisi quaerat quod nobis aperiam! Lorem ipsum dolor sit amet consectetur
                adipisicing elit. Pariatur doloremque magnam atque rerum beatae obcaecati ratione
                ad, veritatis provident dicta eos nostrum, culpa suscipit quisquam nisi quaerat quod
                nobis aperiam! Lorem ipsum dolor sit amet consectetur adipisicing elit. Pariatur
                doloremque magnam atque rerum beatae obcaecati ratione ad, veritatis provident dicta
                eos nostrum, culpa suscipit quisquam nisi quaerat quod nobis aperiam! Lorem ipsum
                dolor sit amet consectetur adipisicing elit. Pariatur doloremque magnam atque rerum
                beatae obcaecati ratione ad, veritatis provident dicta eos nostrum, culpa suscipit
                quisquam nisi quaerat quod nobis aperiam! Lorem ipsum dolor sit amet consectetur
                adipisicing elit. Pariatur doloremque magnam atque rerum beatae obcaecati ratione
                ad, veritatis provident dicta eos nostrum, culpa suscipit quisquam nisi quaerat quod
                nobis aperiam! Lorem ipsum dolor sit amet consectetur adipisicing elit. Pariatur
                doloremque magnam atque rerum beatae obcaecati ratione ad, veritatis provident dicta
                eos nostrum, culpa suscipit quisquam nisi quaerat quod nobis aperiam! Lorem ipsum
                dolor sit amet consectetur adipisicing elit. Pariatur doloremque magnam atque rerum
                beatae obcaecati ratione ad, veritatis provident dicta eos nostrum, culpa suscipit
                quisquam nisi quaerat quod nobis aperiam! Lorem ipsum dolor sit amet consectetur
                adipisicing elit. Pariatur doloremque magnam atque rerum beatae obcaecati ratione
                ad, veritatis provident dicta eos nostrum, culpa suscipit quisquam nisi quaerat quod
                nobis aperiam! Lorem ipsum dolor sit amet consectetur adipisicing elit. Pariatur
                doloremque magnam atque rerum beatae obcaecati ratione ad, veritatis provident dicta
                eos nostrum, culpa suscipit quisquam nisi quaerat quod nobis aperiam! Lorem ipsum
                dolor sit amet consectetur adipisicing elit. Pariatur doloremque magnam atque rerum
                beatae obcaecati ratione ad, veritatis provident dicta eos nostrum, culpa suscipit
                quisquam nisi quaerat quod nobis aperiam! Lorem ipsum dolor sit amet consectetur
                adipisicing elit. Pariatur doloremque magnam atque rerum beatae obcaecati ratione
                ad, veritatis provident dicta eos nostrum, culpa suscipit quisquam nisi quaerat quod
                nobis aperiam! Lorem ipsum dolor sit amet consectetur adipisicing elit. Pariatur
                doloremque magnam atque rerum beatae obcaecati ratione ad, veritatis provident dicta
                eos nostrum, culpa suscipit quisquam nisi quaerat quod nobis aperiam! Lorem ipsum
                dolor sit amet consectetur adipisicing elit. Pariatur doloremque magnam atque rerum
                beatae obcaecati ratione ad, veritatis provident dicta eos nostrum, culpa suscipit
                quisquam nisi quaerat quod nobis aperiam! Lorem ipsum dolor sit amet consectetur
                adipisicing elit. Pariatur doloremque magnam atque rerum beatae obcaecati ratione
                ad, veritatis provident dicta eos nostrum, culpa suscipit quisquam nisi quaerat quod
                nobis aperiam! Lorem ipsum dolor sit amet consectetur adipisicing elit. Pariatur
                doloremque magnam atque rerum beatae obcaecati ratione ad, veritatis provident dicta
                eos nostrum, culpa suscipit quisquam nisi quaerat quod nobis aperiam! Lorem ipsum
                dolor sit amet consectetur adipisicing elit. Pariatur doloremque magnam atque rerum
                beatae obcaecati ratione ad, veritatis provident dicta eos nostrum, culpa suscipit
                quisquam nisi quaerat quod nobis aperiam!
              </div>
            </div>
          </ShowIf>
        </div>
      </div>
      <CommentProvider storyId={story.id}>
        <Comments story={story} />
      </CommentProvider>

      <ShowIf if={showCreateNewTaskModal}>
        <NewStoryTaskModal
          show={showCreateNewTaskModal}
          onClose={() => {
            setShowCreateNewTaskModal(false);
            getStories();
          }}
        />
      </ShowIf>
    </div>
  );
}

export default StoryEdit;

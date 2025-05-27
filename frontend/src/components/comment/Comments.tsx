import useComment from "../../hooks/comment/useComent";
import { IComment, INewComment, IStory, ITask } from "../../types/story.types";
import TextArea from "../generic/inputs/CustomTextArea";
import Button from "../generic/buttons/Button";
import Comment from "./Comment";
import { useState } from "react";
import { FaPlus } from "react-icons/fa";

interface ICommentsProps {
  task?: ITask;
  story?: IStory;
}

function Comments(props: ICommentsProps) {
  const { comments, addComment } = useComment();
  const [ newComment, setNewComment ] = useState("");

  const handleAddComment = () => {
    let comment: INewComment = {
        taskId: props.task?.id,
        storyId: props.story?.id,
        content: newComment.trim()
    }
    addComment(comment)
      .then(() => {
        setNewComment("");
      })
      .catch((error) => {
        console.error("Failed to add comment:", error);
      });
  };

  return (
    <>
      {comments.map((comment: IComment) => (
        <Comment
          key={comment.id}
          comment={comment}
        />
      ))}
      <TextArea
        value={newComment}
        onChange={(e) => setNewComment(e.target.value)}
        placeholder="Comment on this task..."
        rows={3}
        className="w-full"
      />
      <Button
        text={
          <span className="flex items-center gap-2">
            <FaPlus className="text-ap-lavender-900" />
              Add Comment
          </span>
        }
        className="bg-white px-10 border border-ap-onyx-50"
        onClick={() => handleAddComment()}
        disabled={newComment.trim() === ""}
      />
    </>
  )
}

export default Comments;
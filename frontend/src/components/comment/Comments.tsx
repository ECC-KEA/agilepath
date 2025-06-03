import useComment from "../../hooks/comment/useComent";
import { IComment, INewComment, IStory, ITask } from "../../types/story.types";
import TextArea from "../generic/inputs/CustomTextArea";
import Button from "../generic/buttons/Button";
import Comment from "./Comment";
import { useState } from "react";

interface ICommentsProps {
  task?: ITask;
  story?: IStory;
}

function Comments(props: ICommentsProps) {
  const { comments, addComment } = useComment();
  const [newComment, setNewComment] = useState("");

  const handleAddComment = () => {
    const comment: INewComment = {
      taskId: props.task?.id,
      storyId: props.story?.id,
      content: newComment.trim()
    };
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
        placeholder="Leave a comment..."
        rows={3}
        className="w-full"
      />
      <div className="w-full flex justify-end">
        <Button
          text="Comment"
          className="bg-ap-lavender-900 text-white px-10 border border-ap-onyx-50 w-fit"
          onClick={() => handleAddComment()}
          disabled={newComment.trim() === ""}
        />
      </div>
    </>
  );
}

export default Comments;

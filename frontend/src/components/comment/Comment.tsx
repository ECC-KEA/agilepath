import { IComment } from "../../types/story.types";


function Comment({comment}: { comment: IComment }) {
  return (
    <>
      {comment.content && (
        <div className="p-4 border-b border-ap-onyx-50">
          <div className="text-sm text-ap-lavender-900">{comment.content}</div>
          <div className="text-xs text-ap-lavender-500 mt-1">
            {new Date(comment.createdAt).toLocaleString()}
          </div>
        </div>
      )}
    </>
  );
}

export default Comment;
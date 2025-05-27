import { createContext } from "react";
import { IComment, INewComment } from "../../types/story.types";

interface ICommentContext {
  comments: IComment[];
  addComment: (comment: INewComment) => Promise<void>;
}

const CommentContext = createContext<ICommentContext | undefined>(undefined);
export default CommentContext;
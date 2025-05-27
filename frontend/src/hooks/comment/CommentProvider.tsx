import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import { IComment, INewComment } from "../../types/story.types";
import { useApi } from "../utils/useApi";
import CommentContext from "./CommentContext";
import { useLoading } from "../utils/loading/useLoading";

interface CommentProviderProps extends PropsWithChildren {
  storyId?: string;
  taskId?: string;
}

function CommentProvider({ children, storyId, taskId }: Readonly<CommentProviderProps>) {
  const loader = useLoading();
  const { get, post } = useApi();
  const [comments, setComments] = useState<IComment[]>([]);

  const loadComments = useCallback(async () => {
    loader.add();
    const url = taskId ? `task/${taskId}` : `story/${storyId}`;
    return get(`/comments/${url}`)
      .then(setComments)
      .finally(loader.done);
  }, [get, storyId, taskId]);

  const addComment = useCallback(async (comment: INewComment) => {
    loader.add();
    return post("/comments", comment)
      .then(newComment => setComments(prev => [...prev, newComment]))
      .finally(loader.done);
  }, [post]);

  useEffect(() => {
    loadComments();
  }, [loadComments]);

  const contextValue = useMemo(() => ({
    comments,
    addComment
  }), [comments, addComment]);

  return (
    <CommentContext.Provider value={contextValue}>
      {children}
    </CommentContext.Provider>
  );
}

export default CommentProvider;
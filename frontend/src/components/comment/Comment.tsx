import { useEffect, useState } from "react";
import { IComment } from "../../types/story.types";
import { IUser } from "../../types/user.types";
import useUserApi from "../../hooks/users/useUserApi";
import { Avatar } from "@mui/material";
import { name } from "../../helpers/userHelpers";

function Comment({ comment }: { comment: IComment }) {
  const { getUser } = useUserApi();

  const [user, setUser] = useState<IUser>();

  useEffect(() => {
    getUser(comment.createdBy).then(setUser).catch(console.error);
  }, [comment]);

  return (
    <>
      {comment.content && (
        <div className="p-4 border-b border-ap-onyx-50 flex gap-2">
          <Avatar
            src={user?.avatarUrl}
            className="mt-2"
          />
          <div>
            <div className="text-xs text-ap-lavender-500 flex items-center gap-2">
              <div className="font-semibold text-ap-lavender-900">{user && name(user)}</div>
              {new Date(comment.createdAt).toLocaleString()}
            </div>
            <div className="text-sm">{comment.content}</div>
          </div>
        </div>
      )}
    </>
  );
}

export default Comment;

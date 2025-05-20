import useUserApi from "../hooks/users/useUserApi";
import CustomAsyncSelect from "../components/generic/select/CustomAsyncSelect";
import { IUser } from "../types/user.types";
import { Avatar } from "@mui/material";
import useCurrentProject from "../hooks/projects/useCurrentProject";
import ShowIf from "../components/generic/ShowIf";

function ProjectMembers() {
  const { searchUsers } = useUserApi();
  const { members, project, owner } = useCurrentProject();

  console.log(owner);

  return (
    <div>
      <CustomAsyncSelect
        loadOptions={(search) =>
          searchUsers(search).then((u: IUser[]) =>
            u.map((u) => ({
              label: (
                <span className="flex items-center gap-3">
                  <Avatar src={u.avatarUrl} />
                  {u.fullName ?? u.githubUsername ?? u.email}
                </span>
              ),
              value: u
            }))
          )
        }
        isMulti
        className="w-96"
      />
      {!!owner && (
        <div className="p-2 flex flex-col gap-2">
          <div>Owner</div>
          <div className="flex items-center gap-3">
            <Avatar src={owner.avatarUrl} />
            {owner.fullName ?? owner.githubUsername ?? owner.email}
          </div>
        </div>
      )}
      <ShowIf if={members.length > 0}>
        <div>
          <div>Members ({members.length})</div>
          {members.map((m) => (
            <div key={"member" + m.id}>{m.fullName ?? m.githubUsername ?? m.email}</div>
          ))}
        </div>
      </ShowIf>
    </div>
  );
}

export default ProjectMembers;

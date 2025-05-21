import useUserApi from "../hooks/users/useUserApi";
import CustomAsyncSelect from "../components/generic/select/CustomAsyncSelect";
import { IUser } from "../types/user.types";
import { Avatar } from "@mui/material";
import useCurrentProject from "../hooks/projects/useCurrentProject";
import ShowIf from "../components/generic/ShowIf";
import { MemberRole } from "../types/project.types";
import { notifyError, notifySuccess } from "../helpers/notify";
import Button from "../components/generic/buttons/Button";
import RequireRole from "../components/generic/RequireRole";
import { name } from "../helpers/userHelpers";
import { useState } from "react";
import CustomSelect from "../components/generic/select/CustomSelect";

function ProjectMembers() {
  const { searchUsers } = useUserApi();
  const { members, owner, addMember } = useCurrentProject();

  const onAddMember = (u: IUser) => {
    addMember({ userId: u.id, role: MemberRole.CONTRIBUTOR })
      .then(() => notifySuccess("Member added"))
      .catch(() => notifyError("Something went wrong while adding member"));
  };

  const loadOptions = (search: string) => {
    return searchUsers(search).then((u: IUser[]) =>
      u
        .filter((usr) => !members.some((m) => m.user.id === usr.id))
        .map((u) => ({
          label: (
            <span className="flex items-center gap-3">
              <Avatar src={u.avatarUrl} />
              {u.fullName ?? u.githubUsername ?? u.email}
            </span>
          ),
          value: u
        }))
    );
  };

  return (
    <div>
      {!!owner && (
        <div className="p-2 flex flex-col gap-2">
          <div>Owner</div>
          <div className="flex items-center bg-white p-2 rounded shadow justify-between">
            <div className="flex items-center gap-3">
              <Avatar src={owner.avatarUrl} />
              {name(owner)}
            </div>
          </div>
        </div>
      )}
      <ShowIf if={members.length > 0}>
        <div className="p-2 flex flex-col gap-2 mt-8">
          <div>Contributors ({members.length})</div>
          <RequireRole role={MemberRole.ADMIN}>
            <div className="">
              <CustomAsyncSelect
                loadOptions={loadOptions}
                onChange={(o) => {
                  if (o) {
                    onAddMember(o.value);
                  }
                }}
                value={null}
                className="w-xl"
                placeholder="Add contributor"
                noOptionsMessage={() => "Search by name, username or email"}
              />
            </div>
          </RequireRole>
          {members.map((m) => (
            <MemberCard
              key={"member" + m.user.id}
              role={m.role}
              user={m.user}
            />
          ))}
        </div>
      </ShowIf>
    </div>
  );
}

interface MemberCardProps {
  user: IUser;
  role: MemberRole;
}
function MemberCard(props: Readonly<MemberCardProps>) {
  const { editMemberRole, removeMember } = useCurrentProject();
  const [editMode, setEditMode] = useState<boolean>(false);

  const onKickMember = () => {
    if (props.role === MemberRole.OWNER) return;
    removeMember({ user: props.user, role: props.role })
      .then(() => notifySuccess(`${name(props.user)} removed from project`))
      .catch(() => notifyError(`Failed to kick ${name(props.user)} from project`));
  };

  const onEditRole = (r: MemberRole) => {
    if (props.role === MemberRole.OWNER) return;
    editMemberRole({ user: props.user, role: props.role }, r)
      .then(() => notifySuccess(`${name(props.user)} is now ${r.toLowerCase()}`))
      .catch(() => notifyError(`Failed to update role for ${name(props.user)}`));
    setEditMode(false);
  };

  const roleOptions = [MemberRole.ADMIN, MemberRole.CONTRIBUTOR].map((r) => ({
    label: <span className="capitalize">{r.toLowerCase()}</span>,
    value: r
  }));

  return (
    <div className="flex items-center bg-ap-mint-50/50 p-2 rounded shadow w-xl justify-between">
      <div className="flex items-center gap-3">
        <Avatar src={props.user.avatarUrl} />
        {name(props.user)}
        <ShowIf if={!editMode}>
          <div className="capitalize text-gray-400">{props.role.toLowerCase()}</div>
        </ShowIf>
        <ShowIf if={editMode}>
          <CustomSelect
            options={roleOptions}
            value={roleOptions.find((o) => o.value === props.role)}
            onChange={(o) => {
              if (o) {
                onEditRole(o.value);
              }
            }}
          />
        </ShowIf>
      </div>
      <RequireRole role={MemberRole.ADMIN}>
        <ShowIf if={props.role !== MemberRole.OWNER}>
          <div className="flex items-center gap-3">
            <Button
              text={editMode ? "Cancel" : "Change role"}
              className={`${editMode ? "bg-white text-ap-onyx-700 border border-ap-onyx-100" : "bg-ap-lavender-900 text-white"} px-2 text-xs`}
              onClick={() => setEditMode(!editMode)}
            />
            <Button
              text="Kick member"
              className="bg-ap-coral-900 text-white px-2 text-xs"
              onClick={onKickMember}
            />
          </div>
        </ShowIf>
      </RequireRole>
    </div>
  );
}

export default ProjectMembers;

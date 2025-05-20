import { PropsWithChildren } from "react";
import { getRoleValue, MemberRole } from "../../types/project.types";
import useMe from "../../hooks/me/useMe";
import useCurrentProject from "../../hooks/projects/useCurrentProject";

interface RequireRoleProps extends PropsWithChildren {
  role: MemberRole;
}

function RequireRole({ children, role }: Readonly<RequireRoleProps>) {
  const { members, owner } = useCurrentProject();

  const { me } = useMe();

  const member = members.find((m) => m.user.id === me?.id);

  if (!member && me?.id !== owner?.id) return null;
  if (
    getRoleValue(role) > getRoleValue(member?.role ?? MemberRole.CONTRIBUTOR) &&
    me?.id !== owner?.id
  )
    return null;
  return <>{children}</>;
}

export default RequireRole;

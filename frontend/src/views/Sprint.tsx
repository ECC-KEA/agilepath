import { Outlet, useParams } from "react-router";
import SprintProvider from "../hooks/sprint/SprintProvider";
import ColumnProvider from "../hooks/column/ColumnProvider";
import useCurrentProject from "../hooks/projects/useCurrentProject";

function Sprint() {
  const { sprintId } = useParams();
  const { sprints } = useCurrentProject();

  // if sprint is not part of current project block it
  if (!sprintId || !sprints.some((s) => s.id === sprintId)) {
    return null; // TODO return 404 page
  }

  return (
    <SprintProvider sprintId={sprintId}>
      <ColumnProvider sprintId={sprintId}>
        <Outlet />
      </ColumnProvider>
    </SprintProvider>
  );
}

export default Sprint;

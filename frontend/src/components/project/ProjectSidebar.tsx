import { NavLink, useParams } from "react-router";
import useCurrentProject from "../../hooks/projects/useCurrentProject";
import { ISprint } from "../../types/sprint.types";
import Button from "../generic/buttons/Button";
import { useMemo, useState } from "react";
import ShowIf from "../generic/ShowIf";
import NewSprintModal from "./NewSprintModal";
import { isFuture, isPast } from "../../helpers/timeHelpers";

function ProjectSidebar() {
  const { projectID } = useParams();
  const { sprints } = useCurrentProject();
  const [showNewSprintModal, setShowNewSprintModal] = useState<boolean>(false);

  return (
    <div className="w-58 flex flex-col gap-1 h-full border-r border-ap-onyx-50 flex-shrink-0">
      <NavLink
        to={`/projects/${projectID}/overview`}
        className={({ isActive }) =>
          `${isActive ? "text-ap-lavender-700 underline" : "text-ap-onyx-700 hover:text-ap-lavender-200 hover:underline"} 
          border-b-2 border-ap-lavender-700 p-4 text-center`
        }
      >
        Project Overview
      </NavLink>
      <Button
        text="New Sprint"
        className="bg-ap-lavender-900 text-white m-2"
        onClick={() => setShowNewSprintModal(true)}
      />
      {sprints.map((s) => (
        <SprintLink
          key={s.id + "sprintlink"}
          sprint={s}
        />
      ))}

      <ShowIf if={showNewSprintModal}>
        <NewSprintModal
          show={showNewSprintModal}
          onClose={() => setShowNewSprintModal(false)}
        />
      </ShowIf>
    </div>
  );
}

interface SprintLinkProps {
  sprint: ISprint;
}

function SprintLink(props: Readonly<SprintLinkProps>) {
  const { projectID } = useParams();
  const isCurrent = useMemo(() => {
    return isPast(props.sprint.startDate) && isFuture(props.sprint.endDate);
  }, [props.sprint]);
  return (
    <NavLink
      to={`/projects/${projectID}/sprint/${props.sprint.id}/overview`}
      className={`border-ap-lavender-700 text-center`}
    >
      {({ isActive }) => (
        <div
          className="flex items-center w-full px-4 gap-2"
          title={props.sprint.name}
        >
          <div
            className={`${isActive ? "text-ap-lavender-700 underline" : "text-ap-onyx-700 hover:text-ap-lavender-200 hover:underline"} truncate`}
          >
            {props.sprint.name}
          </div>
          <ShowIf if={isCurrent}>
            <div className="text-xs text-ap-lavender-700">current</div>
          </ShowIf>
        </div>
      )}
    </NavLink>
  );
}
export default ProjectSidebar;

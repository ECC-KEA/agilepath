import { useMemo, useState } from "react";
import useCurrentProject from "../../hooks/projects/useCurrentProject";
import Modal from "../generic/Modal";
import CustomSelect from "../generic/select/CustomSelect";
import ShowIf from "../generic/ShowIf";
import ColumnProvider from "../../hooks/column/ColumnProvider";
import TaskProvider from "../../hooks/task/TaskProvider";
import NewStoryTaskModal from "./NewStoryTaskModal";

interface NavnProps {
  show: boolean;
  onClose: () => void;
}
function Navn(props: Readonly<NavnProps>) {
  const { sprints } = useCurrentProject();
  const [sprintId, setSprintId] = useState<string>("");

  const sprintOptions = useMemo(() => {
    return sprints.map((s) => ({
      label: s.name,
      value: s.id
    }));
  }, [sprints]);


  return (
    <Modal
      title="New Task"
      show={props.show}
      onClose={props.onClose}
      actionText="Create"
      onAction={() => {console.log("Create task")}}
      disableAction={!sprintId}
    >
      <div className="flex flex-col gap-4 p-4">
        <div className="flex flex-col">
          <div className="text-ap-onyx-200">Story</div>
          <CustomSelect
            options={sprintOptions}
            value={sprintOptions.find((o) => o.value === sprintId)}
            onChange={(o) => {
              if (o) {
                setSprintId(o.value);
              }
            }}
          />
        </div>
        <ShowIf if={!!sprintId}>
            <ColumnProvider sprintId={sprintId}>
                <TaskProvider>
                    <NewStoryTaskModal
                        show={props.show}
                        onClose={props.onClose}
                    />
                </TaskProvider>
            </ColumnProvider>
        </ShowIf>
      </div>
    </Modal>
  );
}
export default Navn;

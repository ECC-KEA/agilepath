import { useMemo, useState, useRef } from "react";
import useCurrentProject from "../../hooks/projects/useCurrentProject";
import Modal from "../generic/Modal";
import CustomSelect from "../generic/select/CustomSelect";
import ShowIf from "../generic/ShowIf";
import ColumnProvider from "../../hooks/column/ColumnProvider";
import TaskProvider from "../../hooks/task/TaskProvider";
import TaskHandler, { TaskHandlerHandle } from "./TaskHandler";

interface NewStoryTaskModalProps {
  show: boolean;
  onClose: () => void;
}
function NewStoryTaskModal(props: Readonly<NewStoryTaskModalProps>) {
  const { sprints } = useCurrentProject();
  const [sprintId, setSprintId] = useState<string>("");
  
  const taskModalRef = useRef<TaskHandlerHandle>(null);

  const sprintOptions = useMemo(() => {
    return sprints.map((s) => ({
      label: s.name,
      value: s.id
    }));
  }, [sprints]);

  const handleCreateTask = () => {
    taskModalRef.current?.handleCreateTask();
    props.onClose();
  }

  return (
    <Modal
      title="New Task"
      show={props.show}
      onClose={props.onClose}
      actionText="Create"
      onAction={handleCreateTask}
      disableAction={!sprintId}
    >
      <div className="flex flex-col gap-4 p-4">
        <div className="flex flex-col">
          <div className="text-ap-onyx-200">Sprint</div>
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
                    <TaskHandler
                        show={props.show}
                        ref={taskModalRef}
                    />  
                </TaskProvider>
            </ColumnProvider>
        </ShowIf>
      </div>
    </Modal>
  );
}
export default NewStoryTaskModal;

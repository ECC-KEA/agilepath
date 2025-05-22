import useSprint from "../../hooks/sprint/useSprint";
import useColumn from "../../hooks/column/useColumn";
import { INewColumn } from "../../types/column.types";
import { useMemo, useState } from "react";
import { notifyError, notifySuccess } from "../../helpers/notify";
import Modal from "../generic/Modal";
import CustomSelect from "../generic/select/CustomSelect";
import Input from "../generic/inputs/Input";
import { Status } from "../../types/story.types";

interface CreateColumnModalProps {
  show: boolean;
  onClose: () => void;
}

export default function CreateColumnModal(props: CreateColumnModalProps) {
  const [name, setName] = useState<string>("");
  const [columnStatus, setColumnStatus] = useState<Status>(Status.TODO);
  const { sprintId } = useSprint();
  const { createColumn, columns } = useColumn();

  const disableCreate = useMemo(() => {
    return name === "";
  }, [name]);

  const handleCreateColumn = async () => {
    const endIndex = columns.length === 0 ? 0 : Math.max(...columns.map((c) => c.columnIndex)) + 1;

    const newColumn: INewColumn = {
      sprintId,
      name,
      columnStatus,
      columnIndex: endIndex
    };
    createColumn(newColumn)
      .then(() => notifySuccess(`New Column Created: ${newColumn.name}`))
      .catch(() => notifyError("Something went wrong while creating column"));
  };

  const columnStatusOptions = [
    {
      label: (
        <span className="gap-2 flex">
          To do <span className="italic text-ap-onyx-200">Auto-open tasks</span>
        </span>
      ),
      value: Status.TODO
    },
    { label: <span>In progress</span>, value: Status.IN_PROGRESS },
    {
      label: (
        <span>
          Done <span className="text-ap-onyx-200 italic">Auto-close tasks</span>
        </span>
      ),
      value: Status.DONE
    },
    {
      label: (
        <span>
          Archived <span className="text-ap-onyx-200 italic">Auto archive tasks</span>
        </span>
      ),
      value: Status.ARCHIVED
    }
  ];

  return (
    <Modal
      title="Create column"
      show={props.show}
      onClose={props.onClose}
      onAction={() => {
        handleCreateColumn();
        props.onClose();
      }}
      actionText="Create"
      disableAction={disableCreate}
    >
      <div className="flex flex-col gap-4 p-4">
        <div className="flex flex-col">
          <div className="text-ap-onyx-200">Title</div>
          <Input
            type="text"
            placeholder="Column Name"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </div>
        <div className="flex flex-col">
          <div className="text-ap-onyx-200">Type</div>
          <CustomSelect
            options={columnStatusOptions}
            value={columnStatusOptions.find((o) => o.value === columnStatus)}
            onChange={(o) => {
              if (o) {
                setColumnStatus(o.value);
              }
            }}
          />
        </div>
      </div>
    </Modal>
  );
}

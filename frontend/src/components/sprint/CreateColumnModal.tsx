import useSprint from "../../hooks/sprint/useSprint";
import useColumn from "../../hooks/column/useColumn";
import { ColumnStatus, INewColumn } from "../../types/column.types";
import { useMemo, useState } from "react";
import { notifyError, notifySuccess } from "../../helpers/notify";
import Modal from "../generic/Modal";
import CustomSelect from "../generic/select/CustomSelect";
import Input from "../generic/inputs/Input";

interface CreateColumnModalProps {
  show: boolean;
  onClose: () => void;
}

export default function CreateColumnModal(props: CreateColumnModalProps) {
  const [columnName, setColumnName] = useState<string>("");
  const [columnStatus, setColumnStatus] = useState<ColumnStatus>(ColumnStatus.TODO);
  const { sprintId } = useSprint();
  const { createColumn, columns } = useColumn();

  const disableCreate = useMemo(() => {
    return columnName === "";
  }, [columnName]);

  const handleCreateColumn = async (name: string, columnStatus: ColumnStatus) => {
    const endIndex = columns.length === 0 ? 0 : Math.max(...columns.map((c) => c.columnIndex)) + 1;
    console.log(endIndex);

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
      value: ColumnStatus.TODO
    },
    { label: <span>In progress</span>, value: ColumnStatus.IN_PROGRESS },
    {
      label: (
        <span>
          Done <span className="text-ap-onyx-200 italic">Auto-close tasks</span>
        </span>
      ),
      value: ColumnStatus.DONE
    }
  ];

  return (
    <Modal
      title="Create column"
      show={props.show}
      onClose={props.onClose}
      onAction={() => {
        handleCreateColumn(columnName, columnStatus);
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
            value={columnName}
            onChange={(e) => setColumnName(e.target.value)}
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

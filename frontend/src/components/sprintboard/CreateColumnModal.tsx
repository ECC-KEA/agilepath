import useSprint from "../../hooks/sprint/useSprint";
import useColumn from "../../hooks/column/useColumn";
import { ColumnStatus, INewColumn } from "../../types/column.types";
import { useState } from "react";

export default function CreateColumnModal() {
  const [columnName, setColumnName] = useState<string>("");
  const [columnStatus, setColumnStatus] = useState<ColumnStatus>(ColumnStatus.TODO);
  const { sprintId } = useSprint();
  const { createColumn } = useColumn();

  const handleCreateColumn = async (name: string, columnStatus: ColumnStatus) => {
    try {
      const newColumn: INewColumn = {
        sprintId,
        name,
        columnStatus
      };
      await createColumn(newColumn);
      console.log("New Column Created: ", newColumn);
    } catch (error) {
      console.error("Error creating column: ", error);
    }
  };

  return (
  <div className="flex flex-col gap-4 p-4">
    <div className="text-lg font-bold">Create New Column</div>
    <input type="text" placeholder="Column Name" className="border border-ap-onyx-200 rounded-md p-2" 
      value={columnName}
      onChange={(e) => setColumnName(e.target.value)}
    />
    <select className="border border-ap-onyx-200 rounded-md p-2"
      value={columnStatus}
      onChange={(e) => setColumnStatus(e.target.value as ColumnStatus)}
    >
      <option value={ColumnStatus.TODO}>TODO</option>
      <option value={ColumnStatus.IN_PROGRESS}>IN_PROGRESS</option>
      <option value={ColumnStatus.DONE}>DONE</option>
    </select>

    <div 
      className="m-4 text-xl text-center border-ap-onyx-200 border rounded-md p-2 cursor-pointer"
      onClick={() => {
        handleCreateColumn(columnName, columnStatus);
        setColumnName("");
        setColumnStatus(ColumnStatus.TODO);
      }}
    >
        Create
    </div>
  </div>
  );
}
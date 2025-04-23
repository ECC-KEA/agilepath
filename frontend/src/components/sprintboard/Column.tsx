import {IColumn } from "../../types/column.types";
import useSprint from "../../hooks/sprint/useSprint";

interface IColumnProps {
    column: IColumn;
}

export default function Column({ column }: IColumnProps) {
  const { sprint } = useSprint();
  return (
    <div key={column.id} className=" ">
      <h2>{column.name}</h2>
      {/* Add your column content here */}
    </div>
  );
}
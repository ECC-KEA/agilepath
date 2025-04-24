import {IColumn } from "../../types/column.types";
import { FaPlus } from "react-icons/fa6";
//import useSprint from "../../hooks/sprint/useSprint";

interface IColumnProps {
    column: IColumn;
}

export default function Column({ column }: IColumnProps) {
  return (
    <div className="flex flex-col h-full justify-between pb-2">
      <div className="text-xl">{column.name}</div>
      
      <div className="flex-grow">
        <div>Task1</div>
        <div>Task2</div>
        <div>Task3</div>
      </div>

      <div className="inline-flex justify-center items-center text-xl border border-ap-onyx-200 rounded-md px-4 py-1 max-w-max m-auto cursor-pointer">
        <FaPlus className="text-ap-lavender-800" /> 
        <div className="pl-1">Add</div>
      </div>
    </div>
  );
}
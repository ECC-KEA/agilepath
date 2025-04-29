import useSprint from "../hooks/sprint/useSprint";
import useColumn from "../hooks/column/useColumn";
import SprintProvider from "../hooks/sprint/SprintProvider";
import ColumnProvider from "../hooks/column/ColumnProvider";
import Column from "../components/sprint/Column";
import CreateColumnModal from "../components/sprint/CreateColumnModal";
import { useParams } from "react-router";
import { FaArrowLeft, FaPlus } from "react-icons/fa6";
import { useState } from "react";
import ShowIf from "../components/generic/ShowIf";

function Sprint() {
  const { sprintId } = useParams<{ sprintId: string }>();

  if (!sprintId) {
    return null;
  }

  return (
    <SprintProvider sprintId={sprintId}>
      <ColumnProvider sprintId={sprintId}>
        <SprintContent />
      </ColumnProvider>
    </SprintProvider>
  );
}

function SprintContent() {
  const { sprint } = useSprint();
  const { columns } = useColumn();
  const [showCreateColumnModal, setShowCreateColumnModal] = useState(false);

  return (
    <div className="sprint-board w-full h-[80vh] ">
      {!!sprint && <Header sprintName={sprint.name} />}
      <div className="flex flex-row w-full h-full">
        <Sidebar />
        <div className="flex flex-col w-full h-full p-4 gap-4 border-ap-onyx-200 border">
          <input
            className="w-72 h-8 ml-4 pl-4 border border-ap-onyx-200 rounded-md"
            placeholder="Search..."
          />
          <div className="flex flex-row justify-center gap-6 w-full h-full ">
            {columns.map((column) => (
              <div
                key={column.id}
                className="flex-1 m-4 text-center  border-ap-onyx-200 border rounded-md shadow-sm shadow-ap-onyx-400"
              >
                <Column column={column} />
              </div>
            ))}
            <FaPlus
              className="m-4 text-4xl text-ap-lavender-800 border-ap-onyx-200 border rounded-md p-2 cursor-pointer"
              onClick={() => setShowCreateColumnModal(true)}
            />
          </div>
        </div>
      </div>

      <ShowIf if={showCreateColumnModal}>
        <CreateColumnModal
          show={showCreateColumnModal}
          onClose={() => setShowCreateColumnModal(false)}
        />
      </ShowIf>
    </div>
  );
}

function Header({ sprintName }: { sprintName: string }) {
  return (
    <div className="bg-ap-onyx-50/20 border-b border-ap-onyx-200">
      <div className="flex flex-row items-center justify-start gap-2 p-4 text-ap-onyx-500">
        <FaArrowLeft className="text-2xl" />
        <h1 className="text-2xl">{sprintName}</h1>
      </div>
      <div className="flex flex-row gap-2">
        <div className="w-50" />
        <div className="text-ap-lavender-600 underline cursor-pointer">board</div>
        <div className="text-ap-onyx-700 cursor-pointer">stats</div>
      </div>
    </div>
  );
}

function Sidebar() {
  return (
    <div className="w-58 flex flex-col gap-1">
      <div className="text-ap-onyx-700 border-b-2 border-ap-lavender-700 p-4">Project overview</div>
      <div className="flex text-ap-lavender-600 gap-2 items-center">
        <div className="text-sm pl-4 underline cursor-pointer">Sprint 5</div>
        <div className="text-xs">current</div>
      </div>
      <div className="text-sm pl-4 cursor-pointer">Sprint 4</div>
      <div className="text-sm pl-4 cursor-pointer">Sprint 3</div>
      <div className="text-sm pl-4 cursor-pointer">Sprint 2</div>
      <div className="text-sm pl-4 cursor-pointer">Sprint 1</div>
    </div>
  );
}

export default Sprint;

import { FaPlus } from "react-icons/fa6";
import ShowIf from "../components/generic/ShowIf";
import CreateColumnModal from "../components/sprint/CreateColumnModal";
import Column from "../components/sprint/Column";
import useColumn from "../hooks/column/useColumn";
import { useState } from "react";
import Input from "../components/generic/inputs/Input";

function SprintBoard() {
  const { columns } = useColumn();
  const [showCreateColumnModal, setShowCreateColumnModal] = useState(false);

  return (
    <div className="sprint-board w-full">
      <div className="flex w-full h-full">
        <div className="flex flex-col w-full h-full p-4 gap-4">
          <Input
            className="w-72"
            placeholder="Search..."
          />
          <div className="flex gap-6 w-full h-full ">
            {columns.map((column) => (
              <Column
                column={column}
                key={column.id + "-sprintcol"}
              />
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

export default SprintBoard;

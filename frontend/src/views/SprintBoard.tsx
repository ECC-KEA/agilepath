import { FaPlus } from "react-icons/fa6";
import ShowIf from "../components/generic/ShowIf";
import CreateColumnModal from "../components/sprint/CreateColumnModal";
import Column from "../components/sprint/Column";
import useColumn from "../hooks/column/useColumn";
import { useState } from "react";

function SprintBoard() {
  const { columns } = useColumn();
  const [showCreateColumnModal, setShowCreateColumnModal] = useState(false);

  return (
    <div className="sprint-board w-full">
      <div className="flex flex-row w-full h-full">
        <div className="flex flex-col w-full h-full p-4 gap-4">
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

export default SprintBoard;

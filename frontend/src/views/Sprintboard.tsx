import useSprint from "../hooks/sprint/useSprint";
import SprintProvider from "../hooks/sprint/SprintProvider";
import Column from "../components/sprintboard/Column";
import { useParams } from "react-router-dom";

function SprintBoard() {

  const { sprintId } = useParams<{ sprintId: string }>();

  console.log("SprintBoard", sprintId);

  if (!sprintId) {
    return <div>Invalid sprint ID</div>;
  }

  return (
    <SprintProvider sprintId={sprintId}>
      <SprintBoardContent />
    </SprintProvider>
  );
}

function SprintBoardContent() {
  const { sprint, columns } = useSprint();
  console.log("SprintBoardContent", sprint);
  console.log("Column", columns);

  if (!sprint) {
    return <div>Loading...</div>;
  }
  if (!columns) {
    return <div>Loading columns...</div>;
  }

  return (
    <div className="sprint-board">
      <h1>{sprint.name}</h1>
      <div className="columns">
        {columns.map((column) => (
          <Column key={column.id} column={column} />
        ))}
      </div>
    </div>
  );
}


export default SprintBoard;
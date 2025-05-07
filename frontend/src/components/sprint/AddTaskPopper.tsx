import { Popper } from "@mui/material";

interface AddTaskPopperProps {
  anchorEl: null | HTMLElement;
  onCreateNewClick: () => void;
  onAddExistingClick: () => void;
}
function AddTaskPopper(props: Readonly<AddTaskPopperProps>) {
  return (
    <Popper
      open={Boolean(props.anchorEl)}
      anchorEl={props.anchorEl}
      className="confirm-dialog"
      placement="top"
    >
      <div className="bg-white shadow-xl rounded-md border border-ap-onyx-50 overflow-hidden">
        <div
          className="py-2 px-4 hover:brightness-95 cursor-pointer bg-white"
          onClick={props.onCreateNewClick}
        >
          Create new task
        </div>
        <div
          className="py-2 px-4 hover:brightness-95 cursor-pointer bg-white"
          onClick={props.onAddExistingClick}
        >
          Add existing task
        </div>
      </div>
    </Popper>
  );
}
export default AddTaskPopper;

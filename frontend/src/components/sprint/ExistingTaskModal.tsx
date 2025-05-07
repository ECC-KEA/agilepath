import { IColumn } from "../../types/column.types";
import Modal from "../generic/Modal";

interface ExistingTaskModalProps {
  show: boolean;
  onClose: () => void;
  column: IColumn;
}
function ExistingTaskModal(props: Readonly<ExistingTaskModalProps>) {
  return (
    <Modal
      title="Existing Task"
      show={props.show}
      onClose={props.onClose}
    >
      Yeet
    </Modal>
  );
}
export default ExistingTaskModal;

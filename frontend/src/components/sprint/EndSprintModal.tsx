import Modal from "../generic/Modal";
import useSprint from "../../hooks/sprint/useSprint";
import { notifyError, notifySuccess } from "../../helpers/notify";

interface EndSprintModalProps {
  show: boolean;
  onClose: () => void;
}

export default function EndSprintModal(props: Readonly<EndSprintModalProps>) {
  const {endSprint} = useSprint();

  const handleEndSprint = () => {
    endSprint()
      .then(() => {
        notifySuccess("Sprint ended successfully");
        props.onClose();
      })
      .catch(() => notifyError("Failed to end sprint"));
  }

  return (
    <Modal
      title="End Sprint"
      show={props.show}
      onClose={props.onClose}
      actionText="End Sprint"
      onAction={handleEndSprint}
      disableAction={false}
    >
      <div className="p-4">
        <p>Are you sure you want to end the sprint? This action cannot be undone.</p>
      </div>

    </Modal>
  )
}
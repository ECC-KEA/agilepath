import { Status } from "../../types/story.types";

interface StatusLabelProps {
  status: Status;
  className?: string;
}
function StatusLabel(props: Readonly<StatusLabelProps>) {
  const getStatusText = () => {
    switch (props.status) {
      case Status.DONE:
        return "closed";
      case Status.IN_PROGRESS:
      case Status.TODO:
        return "open";
      case Status.ARCHIVED:
        return "archived";
    }
  };

  const getStatusColorClassName = () => {
    switch (props.status) {
      case Status.DONE:
        return "bg-ap-lavender-500/50 border-ap-lavender-900 text-ap-lavender-900";
      case Status.IN_PROGRESS:
      case Status.TODO:
        return "bg-ap-mint-300/50 border-ap-mint-900 text-ap-mint-900";
      case Status.ARCHIVED:
        return "bg-ap-onyx-300/50 border-ap-onyx-700 text-ap-onyx-700";
      default:
        return "bg-ap-onyx-300/50 border-ap-onyx-700 text-ap-onyx-700";
    }
  };

  return (
    <div
      className={`${props.className ?? ""} ${getStatusColorClassName()} px-2 rounded-xl border flex-shrink-0`}
    >
      {getStatusText()}
    </div>
  );
}
export default StatusLabel;

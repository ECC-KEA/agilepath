import { ReactNode } from "react";
import ShowIf from "../ShowIf";
import LoadingSpinner from "./LoadingSpinner";
import { LoaderPos } from "../../../types/util.types";

interface LoadingOverlayProps {
  position: LoaderPos;
  width?: number;
  text?: ReactNode;
}

export function LoadingOverlay(props: LoadingOverlayProps) {
  const getPositionClassName = () => {
    switch (props.position) {
      case "top-left":
        return "items-start justify-start";
      case "top-right":
        return "items-start justify-end";
      case "bottom-left":
        return "items-end justify-start";
      case "bottom-right":
        return "items-end justify-end";
      case "top":
        return "items-start justify-center";
      case "bottom":
        return "items-end justify-center";
      case "center":
        return "items-center justify-center";
    }
  };
  return (
    <div
      className={`${getPositionClassName()} flex absolute z-[99999999] pointer-events-none w-screen inset-0`}
    >
      <div
        className={`bg-white/90 backdrop-blur-xs rounded px-8 py-4 shadow-xl border border-gray-200 m-1 opacity-100 w-fit flex flex-col items-center`}
      >
        <LoadingSpinner size={props.width ?? 50} />
        <ShowIf if={!!props.text}>
          <div className="truncate text-center text-dark-forest-900">{props.text}</div>
        </ShowIf>
      </div>
    </div>
  );
}

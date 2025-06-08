import {Tooltip as ReactTooltip} from "react-tooltip";
import { HiMiniQuestionMarkCircle } from "react-icons/hi2";

export default function Tooltip({text, id, className}: {text: string, id:string, className?: string}) {
    return (
      <>
      <span
        data-tooltip-id={id}
        data-tooltip-content={text}
        className={`inline-flex items-center justify-center w-5 h-5 text-xs font-bold rounded-full text-black cursor-pointer ${className}`}
      >
        <HiMiniQuestionMarkCircle className="w-6 h-6" />
      </span>
      <ReactTooltip id={id} />
    </>
    );
}
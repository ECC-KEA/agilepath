import {Tooltip as ReactTooltip} from "react-tooltip";
import { HiMiniQuestionMarkCircle } from "react-icons/hi2";

export default function Tooltip({text, id, className}: {text: string, id:string, className?: string}) {
    return (
      <>
        <div
          data-tooltip-id={id}
          data-tooltip-html={text}
          className={`inline-flex items-center justify-center w-5 h-5 text-xs font-bold rounded-full text-black cursor-help ${className}`}
        >
          <HiMiniQuestionMarkCircle className="w-6 h-6" />
        </div>
        <ReactTooltip id={id} />
    </>
    );
}
import { DetailedHTMLProps, TextareaHTMLAttributes } from "react";

interface TextAreaProps
  extends DetailedHTMLProps<TextareaHTMLAttributes<HTMLTextAreaElement>, HTMLTextAreaElement> {
  enableResize?: boolean;
}

function TextArea({ enableResize, ...props }: TextAreaProps) {
  return (
    <textarea
      {...props}
      onKeyDown={(e) => e.stopPropagation()}
      className={`
        ${props.className ?? ""} 
        ${props.disabled ? "opacity-50 pointer-events-none" : ""} 
        ${enableResize ? "resize-y" : "resize-none"}
        p-2 border border-ap-onyx-100 rounded focus:outline-ap-lavender-700 selection:bg-ap-lavender-100 caret-ap-lavender-700
      `}
    />
  );
}

export default TextArea;

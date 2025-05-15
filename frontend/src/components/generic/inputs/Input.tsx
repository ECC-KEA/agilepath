import { DetailedHTMLProps, InputHTMLAttributes } from "react";

export type InputProps = DetailedHTMLProps<InputHTMLAttributes<HTMLInputElement>, HTMLInputElement>;

function Input({ ...props }: InputProps) {
  return (
    <input
      {...props}
      className={`
        ${props.className ?? ""} 
        ${props.disabled ? "opacity-50 pointer-events-none" : ""} 
        p-2 border border-ap-onyx-100 rounded focus:outline-ap-lavender-700 selection:bg-ap-lavender-100 caret-ap-lavender-700
      `}
    />
  );
}

export default Input;

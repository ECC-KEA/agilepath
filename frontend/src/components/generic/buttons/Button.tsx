import { ButtonHTMLAttributes, DetailedHTMLProps, ReactNode } from "react";

export interface ButtonProps
  extends DetailedHTMLProps<ButtonHTMLAttributes<HTMLButtonElement>, HTMLButtonElement> {
  text?: ReactNode;
}

function Button(props: ButtonProps) {
  return (
    <button
      {...props}
      className={`${props.className ?? ""} ${props.disabled ? "opacity-50 pointer-events-none" : ""} p-1 select-none rounded hover:brightness-95 transition-colors cursor-pointer`}
    >
      {props.text ?? null}
    </button>
  );
}

export default Button;

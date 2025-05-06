import { PropsWithChildren } from "react";

export interface ShowIfProps extends PropsWithChildren {
  if: boolean;
}

function ShowIf(props: ShowIfProps) {
  if (props.if) return <>{props.children}</>;
  return null;
}

export default ShowIf;

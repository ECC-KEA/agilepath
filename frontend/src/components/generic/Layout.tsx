import { PropsWithChildren } from "react";
import Nav from "../navigation/Nav";

function Layout({ children }: Readonly<PropsWithChildren>) {
  return (
    <div className="w-screen min-h-screen flex flex-col">
      <Nav />
      {children}
    </div>
  );
}

export default Layout;

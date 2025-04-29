import { PropsWithChildren } from "react";
import Nav from "../navigation/Nav";
import { Toaster } from "react-hot-toast";
import { useLoading } from "../../hooks/utils/loading/useLoading";
import { LoadingOverlay } from "./loading/LoadingOverlay";
import ShowIf from "./ShowIf";

function Layout({ children }: Readonly<PropsWithChildren>) {
  const loader = useLoading();

  return (
    <div className="w-screen min-h-screen flex flex-col">
      <Nav />
      {children}
      <ShowIf if={loader.isLoading}>
        <LoadingOverlay
          position={loader.position}
          text={loader.text}
        />
      </ShowIf>
      <Toaster
        toastOptions={{
          success: {
            iconTheme: {
              primary: "#7145d9",
              secondary: "white"
            }
          },
          error: {
            iconTheme: {
              primary: "#ff4735",
              secondary: "white"
            }
          }
        }}
      />
    </div>
  );
}

export default Layout;

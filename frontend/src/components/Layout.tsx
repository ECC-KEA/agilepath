import {PropsWithChildren} from "react";
import Nav from "./navigation/Nav";
import {Toaster} from "react-hot-toast";
import {useLoading} from "../hooks/utils/loading/useLoading";
import {LoadingOverlay} from "./generic/loading/LoadingOverlay";
import ShowIf from "./generic/ShowIf";
import AgileCeremoniesGuide from "./generic/guide/CeremoniesGuide.tsx";

function Layout({ children }: Readonly<PropsWithChildren>) {
  const loader = useLoading();

  return (
    <div className="w-screen h-screen flex flex-col">
      <Nav />
      <div className="flex-1 overflow-auto relative">{children}</div>
      <ShowIf if={loader.isLoading}>
        <LoadingOverlay
          position={loader.position}
          text={loader.text}
        />
      </ShowIf>
        <AgileCeremoniesGuide/>
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

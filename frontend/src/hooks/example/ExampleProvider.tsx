import { PropsWithChildren, useMemo, useState } from "react";
import ExampleContext from "./ExampleContext";

export default function ExampleProvider({
  children,
}: Readonly<PropsWithChildren>) {
  const [example, setExample] = useState("Hello AgilePath");

  const contextValue = useMemo(() => ({ example, setExample }), [example]);

  return (
    <ExampleContext.Provider value={contextValue}>
      {children}
    </ExampleContext.Provider>
  );
}

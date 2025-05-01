import { PropsWithChildren, useMemo, useState } from "react";
import LoadingContext, { ILoadingContext } from "./LoadingContext";
import { LoaderPos } from "../../../types/util.types";

const DEFAULT_POS = LoaderPos.BOTTOM_LEFT;

export function LoadingProvider({ children }: Readonly<PropsWithChildren>) {
  const [loadingCounter, setLoadingCounter] = useState(0);
  const [text, setText] = useState<string>();
  const [position, setPosition] = useState<LoaderPos>(DEFAULT_POS);

  const addLoading = () => setLoadingCounter((c) => c + 1);
  const doneLoading = () => {
    setLoadingCounter((c) => {
      const newCount = Math.max(0, c - 1);
      if (newCount === 0) {
        setText(undefined);
        setPosition(DEFAULT_POS);
      }
      return newCount;
    });
  };
  const loader: ILoadingContext = useMemo(
    () => ({
      add: addLoading,
      done: doneLoading,
      text: text,
      position: position,
      setPosition: setPosition,
      setText: setText,
      isLoading: loadingCounter > 0
    }),
    [loadingCounter, text, position]
  );

  return <LoadingContext.Provider value={loader}>{children}</LoadingContext.Provider>;
}

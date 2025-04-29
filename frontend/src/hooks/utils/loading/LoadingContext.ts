import { createContext } from "react";
import { LoaderPos } from "../../../types/util.types";

export interface ILoadingContext {
  add: () => void;
  done: () => void;
  text: string | undefined;
  position: LoaderPos;
  setPosition: (pos: LoaderPos) => void;
  setText: (text: string | undefined) => void;
  isLoading: boolean;
}

const LoadingContext = createContext<ILoadingContext | undefined>(undefined);
export default LoadingContext;

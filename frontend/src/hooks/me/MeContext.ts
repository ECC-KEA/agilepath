import { createContext } from "react";
import { IUser } from "../../types/user.types";

interface IMeContext {
  me: IUser | undefined;
}

const MeContext = createContext<IMeContext | undefined>(undefined);
export default MeContext;

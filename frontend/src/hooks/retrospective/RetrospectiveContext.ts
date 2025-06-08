import { createContext } from "react";
import { IRetrospective, INewRetrospective } from "../../types/retrospective.types";

interface IRetrospectiveContext {
  retrospective: IRetrospective | undefined;
  sprintId: string;
  createRetrospective: (retrospective: INewRetrospective) => Promise<void>;
}

const RetrospectiveContext = createContext<IRetrospectiveContext | undefined>(undefined);
export default RetrospectiveContext;
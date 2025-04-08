import { createContext } from "react";

interface IExampleContext {
  example: string;
  setExample: (example: string) => void;
}
const ExampleContext = createContext<IExampleContext | undefined>(undefined);
export default ExampleContext;

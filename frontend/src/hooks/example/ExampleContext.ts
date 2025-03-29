import { createContext } from "react";

interface ExampleContextType {
  example: string;
  setExample: (example: string) => void;
}
const ExampleContext = createContext<ExampleContextType | undefined>(undefined);
export default ExampleContext;

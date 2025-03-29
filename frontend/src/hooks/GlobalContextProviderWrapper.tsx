import { PropsWithChildren } from "react";
import ExampleProvider from "./example/ExampleProvider";

// Add other providers here that are needed across the entire app
export default function GlobalContextProviderWrapper({
  children,
}: Readonly<PropsWithChildren>) {
  return <ExampleProvider>{children}</ExampleProvider>;
}

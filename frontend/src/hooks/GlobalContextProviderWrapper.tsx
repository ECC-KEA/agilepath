import { PropsWithChildren } from "react";
import ExampleProvider from "./example/ExampleProvider";
import { ClerkProvider } from "@clerk/clerk-react";

const PUBLISHABLE_KEY = import.meta.env.VITE_CLERK_PUBLISHABLE_KEY;
if (!PUBLISHABLE_KEY) {
  throw new Error("VITE_CLERK_PUBLISHABLE_KEY is not defined");
}

// Add other providers here that are needed across the entire app
export default function GlobalContextProviderWrapper({ children }: Readonly<PropsWithChildren>) {
  return (
    <ClerkProvider publishableKey={PUBLISHABLE_KEY}>
      <ExampleProvider>{children}</ExampleProvider>
    </ClerkProvider>
  );
}

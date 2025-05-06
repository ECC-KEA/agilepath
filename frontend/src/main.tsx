import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.tsx";
import GlobalContextProviderWrapper from "./hooks/GlobalContextProviderWrapper.tsx";
import { BrowserRouter } from "react-router";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <GlobalContextProviderWrapper>
      <BrowserRouter>
        <App />
      </BrowserRouter>
    </GlobalContextProviderWrapper>
  </StrictMode>
);

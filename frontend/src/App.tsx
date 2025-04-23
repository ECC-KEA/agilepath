import { BrowserRouter, Route, Routes } from "react-router";
import Layout from "./components/generic/Layout";
import { SignedIn, SignedOut, useAuth } from "@clerk/clerk-react";
import Login from "./views/Login";
import SprintBoard from "./views/SprintBoard";

function App() {
  console.log(useAuth());

  return (
    <BrowserRouter>
      <Layout>
        <SignedOut>
          <Routes>
            <Route
              index
              path="*"
              element={<Login />}
            />
          </Routes>
        </SignedOut>
        <SignedIn>
          <Routes>
            <Route
              index
              element={<div></div>}
            />
            <Route
              path="/sprintboard/:sprintId"
              element={<SprintBoard />}
            />
          </Routes>
        </SignedIn>
      </Layout>
    </BrowserRouter>
  );
}

export default App;

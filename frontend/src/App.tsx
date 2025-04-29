import { BrowserRouter, Route, Routes } from "react-router";
import Layout from "./components/generic/Layout";
import { SignedIn, SignedOut } from "@clerk/clerk-react";
import Login from "./views/Login";
import Projects from "./views/Projects";
import Sprint from "./views/Sprint";

function App() {
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
              path="/"
              element={<Projects />}
            />
            <Route
              path="/projects/:projectID"
              element={<div></div>}
            />
            <Route
              path="/sprintboard/:sprintId"
              element={<Sprint />}
            />
          </Routes>
        </SignedIn>
      </Layout>
    </BrowserRouter>
  );
}

export default App;

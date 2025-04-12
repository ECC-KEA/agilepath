import { BrowserRouter, Route, Routes } from "react-router";
import Layout from "./components/generic/Layout";
import { SignedIn, SignedOut } from "@clerk/clerk-react";
import Login from "./views/Login";

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
              element={<div></div>}
            />
          </Routes>
        </SignedIn>
      </Layout>
    </BrowserRouter>
  );
}

export default App;

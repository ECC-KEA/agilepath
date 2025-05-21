import { Route, Routes } from "react-router";
import Layout from "./components/Layout";
import { SignedIn, SignedOut } from "@clerk/clerk-react";
import Login from "./views/Login";
import Projects from "./views/Projects";
import Sprint from "./views/Sprint";
import Project, { ProjectWrapper } from "./views/Project";
import ProjectOverview from "./views/ProjectOverview";
import SprintBoard from "./views/SprintBoard";
import SprintStats from "./views/SprintStats";
import ProjectStats from "./views/ProjectStats";
import ProjectMembers from "./views/ProjectMembers";

function App() {
  return (
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
            element={
              <ProjectWrapper>
                <Project />
              </ProjectWrapper>
            }
          >
            <Route
              path="sprint/:sprintId"
              element={<Sprint />}
            >
              <Route
                index
                path="overview"
                element={<SprintBoard />}
              />
              <Route
                path="stats"
                element={<SprintStats />}
              />
            </Route>
            <Route
              index
              path="overview"
              element={<ProjectOverview />}
            />
            <Route
              path="stats"
              element={<ProjectStats />}
            />
            <Route
              path="contributors"
              element={<ProjectMembers />}
            />
          </Route>
        </Routes>
      </SignedIn>
    </Layout>
  );
}

export default App;

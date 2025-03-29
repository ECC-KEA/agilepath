import Logo from "./components/generic/Logo";

function App() {
  return (
    <div className="flex items-center justify-center h-screen space-x-2 text-2xl">
      <Logo size={50} />
      <div className="text-3xl">AgilePath</div>
    </div>
  );
}

export default App;

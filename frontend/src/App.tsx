import LoadingSpinner from "./components/generic/loading/LoadingSpinner";
import Logo from "./components/generic/Logo";

function App() {
  return (
    <div className="h-screen flex flex-col items-center justify-center space-y-4">
      <div className="flex items-center justify-center space-x-2 text-2xl">
        <Logo size={50} />
        <div className="text-3xl">AgilePath</div>
      </div>
      <LoadingSpinner size={25} />
    </div>
  );
}

export default App;

import { useNavigate } from "react-router";
import Logo from "../generic/Logo";
import { SignedIn, UserButton } from "@clerk/clerk-react";

function Nav() {
  const navigate = useNavigate();

  return (
    <div className="w-full p-2 border-b border-gray-100 flex items-center justify-between">
      <div className="flex items-center gap-2">
        <button
          className="flex items-center space-x-2 cursor-pointer"
          onClick={() => navigate("/")}
        >
          <Logo size={25} />
          <div className="text-xl">AgilePath</div>
        </button>
        <div>{/* Breadcrumbs go here */}</div>
      </div>
      <SignedIn>
        <UserButton />
      </SignedIn>
    </div>
  );
}

export default Nav;

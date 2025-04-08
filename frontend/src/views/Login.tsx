import { SignIn } from "@clerk/clerk-react";

function Login() {
  return (
    <div className="flex items-center justify-center h-100">
      <SignIn />
    </div>
  );
}

export default Login;

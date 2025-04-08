import { LuSquareDashedKanban } from "react-icons/lu";

interface LogoProps {
  size?: number;
  className?: string;
}

function Logo({ className = "text-ap-lavender-800", size = 100 }: Readonly<LogoProps>) {
  return (
    <LuSquareDashedKanban
      size={size}
      className={className}
    />
  );
}

export default Logo;

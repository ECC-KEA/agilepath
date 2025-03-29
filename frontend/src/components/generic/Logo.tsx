import { LuSquareDashedKanban } from "react-icons/lu";

function Logo({
  className = "text-lavender-800",
  size = 100
}: Readonly<{ size?: number; className?: string }>) {
  return (
    <LuSquareDashedKanban
      size={size}
      className={className}
    />
  );
}

export default Logo;

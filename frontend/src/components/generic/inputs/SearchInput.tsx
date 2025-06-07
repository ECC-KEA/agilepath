import { IoSearchOutline } from "react-icons/io5";
import Input, { InputProps } from "./Input";

interface SearchInputProps extends InputProps {
  containerClassName?: string;
}

function SearchInput({ containerClassName, ...props }: SearchInputProps) {
  return (
    <div
      className={`${containerClassName ?? ""} flex items-center border rounded border-ap-onyx-50/50`}
    >
      <Input
        {...props}
        type="search"
        className={`${props.className ?? ""} rounded-r-none border-none outline-0 w-full`}
      />
      <div className="px-3">
        <IoSearchOutline className="text-xl" />
      </div>
    </div>
  );
}

export default SearchInput;

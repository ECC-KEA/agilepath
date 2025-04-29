import { useEffect, useRef } from "react";

export const useOutsideClick = (callback: () => unknown) => {
  const ref = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    const handleClick = (event: MouseEvent) => {
      const target = event.target as HTMLElement;
      // Ignore react select menu clicks, to prevent closing modals when select menu is clicked
      if (
        ref.current &&
        !ref.current.contains(target) &&
        !target.closest("[id^=react-select]") &&
        !target.closest(".confirm-dialog")
      ) {
        callback();
      }
    };

    document.addEventListener("mousedown", handleClick, true);

    return () => {
      document.removeEventListener("mousedown", handleClick, true);
    };
  }, [ref, callback]);

  return ref;
};

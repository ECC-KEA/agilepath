import { useCallback, useEffect } from "react";

export enum KeyboardKey {
  Enter = "Enter",
  Escape = "Escape",
  ArrowDown = "ArrowDown",
  ArrowUp = "ArrowUp"
}

function useKeyboardKey(key: KeyboardKey, handler: (e: KeyboardEvent) => void) {
  const handleKeydown = useCallback(
    (e: KeyboardEvent) => {
      if (e.key === key) {
        handler(e);
      }
    },
    [handler]
  );

  useEffect(() => {
    window.addEventListener("keydown", handleKeydown, false);

    return () => {
      window.removeEventListener("keydown", handleKeydown, false);
    };
  }, [handleKeydown]);
}

export default useKeyboardKey;

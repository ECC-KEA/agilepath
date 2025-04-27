import { PropsWithChildren } from "react";

interface ModalProps extends PropsWithChildren {
  isOpen: boolean;
  onClose: () => void;
}

export default function Modal({ isOpen, onClose, children }: ModalProps) {
  if (!isOpen) return null;

  return (
    <div
      className="fixed inset-0 backdrop-blur-xs flex items-center justify-center z-50"
      onClick={onClose}
    >
      <div
        className="bg-white p-6 rounded-lg shadow-lg min-w-[300px] max-w-[90%]"
        onClick={(e) => e.stopPropagation()}
      >
        {children}
      </div>
    </div>
  );
}

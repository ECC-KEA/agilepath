import { PropsWithChildren, ReactNode } from "react";
import Logo from "./Logo";
import { FaX } from "react-icons/fa6";
import { useOutsideClick } from "../../hooks/utils/useOutsideClick";
import useKeyboardKey, { KeyboardKey } from "../../hooks/utils/useKeyboardKey";
import Button from "./buttons/Button";

export type ModalSize = "xl" | "2xl" | "3xl" | "4xl" | "5xl" | "6xl" | "7xl" | "screen" | "fit";

interface ModalProps extends PropsWithChildren {
  show: boolean;
  title?: ReactNode;
  closeText?: ReactNode;
  onClose: () => void;
  onAction?: () => void;
  onSecondaryAction?: () => void;
  actionText?: ReactNode;
  secondaryActionText?: ReactNode;
  disableAction?: boolean;
  disableSecondaryAction?: boolean;
  actionButtonClassName?: string;
  secondaryActionButtonClassName?: string;
  closeButtonClassName?: string;
  size?: ModalSize;
  isConfirm?: boolean;
  disableEnterSubmit?: boolean;
  disableEscClose?: boolean;
}

function Modal(props: ModalProps) {
  const outsideClickRef = useOutsideClick(props.onClose);

  const getMaxWidthClassName = () => {
    if (!props.size) return "sm:max-w-xl";
    switch (props.size) {
      case "fit":
        return "sm:max-w-fit";
      case "xl":
        return "sm:max-w-xl";
      case "2xl":
        return "sm:max-w-2xl";
      case "3xl":
        return "sm:max-w-3xl";
      case "4xl":
        return "sm:max-w-4xl";
      case "5xl":
        return "sm:max-w-5xl";
      case "6xl":
        return "sm:max-w-6xl";
      case "7xl":
        return "sm:max-w-7xl";
      case "screen":
        return "sm:max-w-[calc(100vw-40px)]";
    }
  };

  function onEnterSubmit() {
    if (!props.disableAction && !!props.onAction && props.show && !props.disableEnterSubmit) {
      props.onAction();
    }
  }

  function onEscClose() {
    if (props.show && !props.disableEscClose) {
      props.onClose();
    }
  }

  useKeyboardKey(KeyboardKey.Escape, onEscClose);

  useKeyboardKey(KeyboardKey.Enter, onEnterSubmit);

  if (!props.show) return null;
  return (
    <div
      className={`relative ${props.isConfirm ? "z-[99999]" : "z-[9999]"}`}
      aria-labelledby="modal-title"
      role="dialog"
      aria-modal="true"
    >
      <div
        className="fixed inset-0 bg-gray-500/75 transition-opacity"
        aria-hidden="true"
      ></div>

      <div className="fixed inset-0 z-10 w-full overflow-y-auto">
        <div className="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
          <div
            ref={outsideClickRef}
            className={`${getMaxWidthClassName()} ${props.isConfirm ? "confirm-dialog" : ""} relative transform overflow-hidden rounded-xs bg-white text-left shadow-xl transition-all sm:my-8 sm:w-full`}
          >
            {!!props.title && (
              <div className="py-1 px-1 border-b border-gray-200 flex items-center gap-1 justify-between">
                <div className="flex items-center gap-1">
                  <Logo size={20} />
                  {props.title}
                </div>
                <FaX
                  onClick={props.onClose}
                  className="text-sm mr-1 cursor-pointer"
                />
              </div>
            )}
            <div className="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">{props.children}</div>
            <div className="bg-gray-50 px-4 py-3 sm:flex sm:flex-row-reverse sm:px-6">
              {!!props.onAction && !!props.actionText && (
                <Button
                  type="button"
                  text={props.actionText}
                  disabled={props.disableAction}
                  onClick={props.onAction}
                  className={`${props.actionButtonClassName ? props.actionButtonClassName : "bg-ap-lavender-800 text-white px-3 w-full justify-center shadow-xs sm:ml-3 sm:w-auto"}`}
                />
              )}
              {!!props.onSecondaryAction && !!props.secondaryActionText && (
                <Button
                  type="button"
                  text={props.secondaryActionText}
                  disabled={props.disableSecondaryAction}
                  onClick={props.onSecondaryAction}
                  className={`${props.secondaryActionButtonClassName ? props.secondaryActionButtonClassName : "bg-ap-lavender-800 text-white px-2 w-full justify-center shadow-xs sm:ml-3 sm:w-auto"}`}
                />
              )}
              <Button
                type="button"
                text={props.closeText ?? "Cancel"}
                onClick={props.onClose}
                className={`${props.closeButtonClassName ? props.closeButtonClassName : "bg-white border border-gray-300 px-2 ml-2"}`}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Modal;

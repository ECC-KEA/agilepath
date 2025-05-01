import toast, { Renderable, ToastOptions, ValueOrFunction } from "react-hot-toast";

const defaultToastConfig: ToastOptions = {
  duration: 3000,
  position: "bottom-left"
};

export function notifySuccess(message: Renderable, config: ToastOptions = defaultToastConfig) {
  return toast.success(message, config);
}
export function notifyError(message: Renderable, config: ToastOptions = defaultToastConfig) {
  return toast.error(message, config);
}
export function notifyCustom(message: Renderable, config: ToastOptions = defaultToastConfig) {
  return toast.custom(message, config);
}

export function notifyPromise(
  promise: Promise<unknown> | (() => Promise<unknown>),
  msgs: {
    loading: Renderable;
    success?: ValueOrFunction<Renderable, unknown>;
    error?: ValueOrFunction<Renderable, unknown>;
  },
  config: ToastOptions = defaultToastConfig
) {
  return toast.promise(promise, msgs, config);
}

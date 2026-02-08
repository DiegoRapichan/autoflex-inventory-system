import clsx from "clsx";

export default function Input({
  label,
  error,
  className,
  required = false,
  ...props
}) {
  return (
    <div className="mb-4">
      {label && (
        <label className="label">
          {label}
          {required && <span className="text-danger ml-1">*</span>}
        </label>
      )}
      <input
        className={clsx("input", error && "input-error", className)}
        {...props}
      />
      {error && <p className="error-message">{error}</p>}
    </div>
  );
}

export default function Badge({ children, variant = "primary" }) {
  const variants = {
    primary: "badge-primary",
    success: "badge-success",
    warning: "badge-warning",
    danger: "badge-danger",
  };

  return <span className={`badge ${variants[variant]}`}>{children}</span>;
}

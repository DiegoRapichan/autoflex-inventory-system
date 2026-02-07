import { Link } from "react-router-dom";

export default function Header() {
  return (
    <header className="bg-white shadow-sm border-b border-gray-200">
      <div className="container-custom">
        <div className="flex items-center justify-between h-16">
          <Link to="/" className="flex items-center gap-2">
            <div className="w-8 h-8 bg-primary-600 rounded flex items-center justify-center">
              <span className="text-white font-bold">A</span>
            </div>
            <h1 className="text-xl font-bold text-gray-900">
              Autoflex Inventory
            </h1>
          </Link>

          <div className="flex items-center gap-4">
            <span className="text-sm text-gray-600">
              Inventory Management System
            </span>
          </div>
        </div>
      </div>
    </header>
  );
}

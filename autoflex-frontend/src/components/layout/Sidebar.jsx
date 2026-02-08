import { NavLink } from "react-router-dom";
import {
  HomeIcon,
  CubeIcon,
  BeakerIcon,
  ChartBarIcon,
} from "@heroicons/react/24/outline";

const navigation = [
  { name: "Dashboard", path: "/", icon: HomeIcon },
  { name: "Products", path: "/products", icon: CubeIcon },
  { name: "Raw Materials", path: "/raw-materials", icon: BeakerIcon },
  { name: "Production", path: "/production", icon: ChartBarIcon },
];

export default function Sidebar() {
  return (
    <aside className="w-64 bg-white border-r border-gray-200 min-h-[calc(100vh-4rem)]">
      <nav className="p-4 space-y-1">
        {navigation.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            end={item.path === "/"}
            className={({ isActive }) =>
              `flex items-center gap-3 px-4 py-3 rounded-lg transition-colors ${
                isActive
                  ? "bg-primary-50 text-primary-700 font-medium"
                  : "text-gray-700 hover:bg-gray-50"
              }`
            }
          >
            <item.icon className="h-5 w-5" />
            {item.name}
          </NavLink>
        ))}
      </nav>
    </aside>
  );
}

import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { fetchProducts } from "../store/slices/productSlice";
import { fetchRawMaterials } from "../store/slices/rawMaterialSlice";
import Card from "../components/common/Card";
import Loading from "../components/common/Loading";
import {
  CubeIcon,
  BeakerIcon,
  ChartBarIcon,
} from "@heroicons/react/24/outline";

export default function Home() {
  const dispatch = useDispatch();
  const { items: products, loading: productsLoading } = useSelector(
    (state) => state.products,
  );
  const { items: rawMaterials, loading: materialsLoading } = useSelector(
    (state) => state.rawMaterials,
  );

  useEffect(() => {
    dispatch(fetchProducts());
    dispatch(fetchRawMaterials());
  }, [dispatch]);

  if (productsLoading || materialsLoading) {
    return <Loading fullScreen />;
  }

  const lowStockMaterials = rawMaterials.filter((m) => m.lowStock).length;

  return (
    <div className="animate-fade-in">
      <div className="page-header">
        <h1 className="page-title">Dashboard</h1>
        <p className="page-subtitle">
          Welcome to Autoflex Inventory Management System
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <Card className="bg-gradient-to-br from-primary-50 to-primary-100 border-primary-200">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-primary-900">
                Total Products
              </p>
              <p className="text-3xl font-bold text-primary-700 mt-2">
                {products.length}
              </p>
            </div>
            <CubeIcon className="h-12 w-12 text-primary-600" />
          </div>
        </Card>

        <Card className="bg-gradient-to-br from-success-light to-green-100 border-success">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-success-dark">
                Raw Materials
              </p>
              <p className="text-3xl font-bold text-success-dark mt-2">
                {rawMaterials.length}
              </p>
            </div>
            <BeakerIcon className="h-12 w-12 text-success-dark" />
          </div>
        </Card>

        <Card className="bg-gradient-to-br from-warning-light to-yellow-100 border-warning">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-warning-dark">
                Low Stock Items
              </p>
              <p className="text-3xl font-bold text-warning-dark mt-2">
                {lowStockMaterials}
              </p>
            </div>
            <ChartBarIcon className="h-12 w-12 text-warning-dark" />
          </div>
        </Card>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <Card title="Quick Actions">
          <div className="space-y-3">
            <Link
              to="/products"
              className="block p-4 rounded-lg border-2 border-gray-200 hover:border-primary-500 hover:bg-primary-50 transition"
            >
              <h3 className="font-medium text-gray-900">Manage Products</h3>
              <p className="text-sm text-gray-600 mt-1">
                Create, edit, and organize products
              </p>
            </Link>

            <Link
              to="/raw-materials"
              className="block p-4 rounded-lg border-2 border-gray-200 hover:border-primary-500 hover:bg-primary-50 transition"
            >
              <h3 className="font-medium text-gray-900">
                Manage Raw Materials
              </h3>
              <p className="text-sm text-gray-600 mt-1">
                Control inventory and stock levels
              </p>
            </Link>

            <Link
              to="/production"
              className="block p-4 rounded-lg border-2 border-gray-200 hover:border-primary-500 hover:bg-primary-50 transition"
            >
              <h3 className="font-medium text-gray-900">
                Production Suggestions
              </h3>
              <p className="text-sm text-gray-600 mt-1">
                Calculate what can be produced
              </p>
            </Link>
          </div>
        </Card>

        <Card title="System Information">
          <dl className="space-y-3">
            <div className="flex justify-between py-2 border-b border-gray-100">
              <dt className="text-gray-600">Version</dt>
              <dd className="font-medium">1.0.0</dd>
            </div>
            <div className="flex justify-between py-2 border-b border-gray-100">
              <dt className="text-gray-600">Last Update</dt>
              <dd className="font-medium">Feb 2026</dd>
            </div>
            <div className="flex justify-between py-2 border-b border-gray-100">
              <dt className="text-gray-600">Status</dt>
              <dd>
                <span className="badge-success">Active</span>
              </dd>
            </div>
          </dl>
        </Card>
      </div>
    </div>
  );
}

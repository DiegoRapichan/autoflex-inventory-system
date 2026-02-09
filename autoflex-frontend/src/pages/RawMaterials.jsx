import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  fetchRawMaterials,
  deleteRawMaterial,
} from "../store/slices/rawMaterialSlice";
import Button from "../components/common/Button";
import Modal from "../components/common/Modal";
import Input from "../components/common/Input";
import Loading from "../components/common/Loading";
import Badge from "../components/common/Badge";
import ConfirmDialog from "../components/common/ConfirmDialog";
import { toast } from "react-toastify";
import api from "../api/rawMaterialApi";

export default function RawMaterials() {
  const dispatch = useDispatch();
  const {
    items: rawMaterials,
    loading,
    error,
  } = useSelector((state) => state.rawMaterials);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingMaterial, setEditingMaterial] = useState(null);
  const [deleteConfirm, setDeleteConfirm] = useState(null);

  const [formData, setFormData] = useState({
    code: "",
    name: "",
    stockQuantity: "",
    unit: "KG",
  });

  useEffect(() => {
    dispatch(fetchRawMaterials());
  }, [dispatch]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingMaterial) {
        await api.update(editingMaterial.id, formData);
        toast.success("Material updated successfully!");
      } else {
        await api.create(formData);
        toast.success("Material created successfully!");
      }
      setIsModalOpen(false);
      resetForm();
      dispatch(fetchRawMaterials());
    } catch (error) {
      toast.error(error.response?.data?.message || "Failed to save material");
    }
  };

  const handleEdit = (material) => {
    setEditingMaterial(material);
    setFormData({
      code: material.code,
      name: material.name,
      stockQuantity: material.stockQuantity,
      unit: material.unit,
    });
    setIsModalOpen(true);
  };

  const handleDelete = async () => {
    try {
      await dispatch(deleteRawMaterial(deleteConfirm.id)).unwrap();
      toast.success("Material deleted successfully!");
      setDeleteConfirm(null);
    } catch (error) {
      toast.error("Failed to delete material");
    }
  };

  const resetForm = () => {
    setFormData({
      code: "",
      name: "",
      stockQuantity: "",
      unit: "KG",
    });
    setEditingMaterial(null);
  };

  const handleModalClose = () => {
    setIsModalOpen(false);
    resetForm();
  };

  const isLowStock = (stockQuantity) => {
    return parseFloat(stockQuantity) < 10;
  };

  if (loading) return <Loading fullScreen />;
  if (error) return <div className="text-red-600 p-4">Error: {error}</div>;

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold text-gray-900">Raw Materials</h1>
        <Button onClick={() => setIsModalOpen(true)}>+ New Material</Button>
      </div>

      <div className="bg-white shadow-md rounded-lg overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gradient-to-r from-green-500 to-green-600">
            <tr>
              <th className="px-6 py-4 text-left text-xs font-semibold text-white uppercase tracking-wider">
                Code
              </th>
              <th className="px-6 py-4 text-left text-xs font-semibold text-white uppercase tracking-wider">
                Name
              </th>
              <th className="px-6 py-4 text-left text-xs font-semibold text-white uppercase tracking-wider">
                Stock
              </th>
              <th className="px-6 py-4 text-left text-xs font-semibold text-white uppercase tracking-wider">
                Unit
              </th>
              <th className="px-6 py-4 text-left text-xs font-semibold text-white uppercase tracking-wider">
                Status
              </th>
              <th className="px-6 py-4 text-right text-xs font-semibold text-white uppercase tracking-wider">
                Actions
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {rawMaterials.length === 0 ? (
              <tr>
                <td
                  colSpan="6"
                  className="px-6 py-12 text-center text-gray-500"
                >
                  No materials yet. Create your first material!
                </td>
              </tr>
            ) : (
              rawMaterials.map((material) => (
                <tr
                  key={material.id}
                  className={`hover:bg-gray-50 transition-colors ${
                    isLowStock(material.stockQuantity) ? "bg-red-50" : ""
                  }`}
                >
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className="px-3 py-1 inline-flex text-sm leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                      {material.code}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-gray-900 font-medium">
                    {material.name}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span
                      className={`text-lg font-bold ${
                        isLowStock(material.stockQuantity)
                          ? "text-red-600"
                          : "text-blue-600"
                      }`}
                    >
                      {parseFloat(material.stockQuantity).toFixed(3)}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <Badge variant="secondary">{material.unit}</Badge>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {isLowStock(material.stockQuantity) ? (
                      <Badge variant="danger">⚠️ Low Stock</Badge>
                    ) : (
                      <Badge variant="success">✓ OK</Badge>
                    )}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium space-x-2">
                    <Button size="sm" onClick={() => handleEdit(material)}>
                      Edit
                    </Button>
                    <Button
                      size="sm"
                      variant="danger"
                      onClick={() => setDeleteConfirm(material)}
                    >
                      Delete
                    </Button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      <Modal
        isOpen={isModalOpen}
        onClose={handleModalClose}
        title={editingMaterial ? "Edit Raw Material" : "New Raw Material"}
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Material Code"
            value={formData.code}
            onChange={(e) => setFormData({ ...formData, code: e.target.value })}
            required
            placeholder="e.g., MAT001"
          />

          <Input
            label="Material Name"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            required
            placeholder="e.g., Stainless Steel"
          />

          <Input
            label="Stock Quantity"
            type="number"
            step="0.001"
            min="0"
            value={formData.stockQuantity}
            onChange={(e) =>
              setFormData({ ...formData, stockQuantity: e.target.value })
            }
            required
            placeholder="0.000"
          />

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Unit
            </label>
            <select
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              value={formData.unit}
              onChange={(e) =>
                setFormData({ ...formData, unit: e.target.value })
              }
              required
            >
              <option value="KG">Kilogram (KG)</option>
              <option value="L">Liter (L)</option>
              <option value="UN">Unit (UN)</option>
              <option value="M">Meter (M)</option>
              <option value="M2">Square Meter (M²)</option>
              <option value="M3">Cubic Meter (M³)</option>
            </select>
          </div>

          <div className="flex gap-3 pt-4">
            <Button type="submit" className="flex-1">
              {editingMaterial ? "Update" : "Create"}
            </Button>
            <Button
              type="button"
              variant="secondary"
              onClick={handleModalClose}
              className="flex-1"
            >
              Cancel
            </Button>
          </div>
        </form>
      </Modal>

      <ConfirmDialog
        isOpen={!!deleteConfirm}
        onClose={() => setDeleteConfirm(null)}
        onConfirm={handleDelete}
        title="Delete Material"
        message={`Are you sure you want to delete "${deleteConfirm?.name}"?`}
      />
    </div>
  );
}

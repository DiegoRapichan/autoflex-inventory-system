import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { fetchProducts } from "../store/slices/productSlice";
import { fetchRawMaterials } from "../store/slices/rawMaterialSlice";
import axios from "../api/axios";
import { toast } from "react-toastify";
import Button from "../components/common/Button";
import Input from "../components/common/Input";
import Modal from "../components/common/Modal";
import Card from "../components/common/Card";
import Loading from "../components/common/Loading";

export default function ProductMaterials() {
  const { id } = useParams();
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const { items: products } = useSelector((state) => state.products);
  const { items: rawMaterials } = useSelector((state) => state.rawMaterials);

  const [product, setProduct] = useState(null);
  const [productMaterials, setProductMaterials] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const [formData, setFormData] = useState({
    rawMaterialId: "",
    requiredQuantity: "",
  });

  useEffect(() => {
    dispatch(fetchProducts());
    dispatch(fetchRawMaterials());
    loadProductMaterials();
  }, [id]);

  useEffect(() => {
    const prod = products.find((p) => p.id === parseInt(id));
    setProduct(prod);
  }, [products, id]);

  const loadProductMaterials = async () => {
    try {
      setLoading(true);
      // ✅ CORRIGIDO: Removido /api do início
      const response = await axios.get(`/products/${id}/materials`);
      setProductMaterials(response.data);
    } catch (error) {
      console.error("Error loading materials:", error);
      toast.error("Failed to load materials");
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      // ✅ CORRIGIDO: Usar a mesma estrutura de URL do backend
      await axios.post(`/products/${id}/materials`, {
        rawMaterialId: parseInt(formData.rawMaterialId),
        requiredQuantity: parseFloat(formData.requiredQuantity),
      });

      toast.success("Material added successfully!");
      setIsModalOpen(false);
      setFormData({ rawMaterialId: "", requiredQuantity: "" });
      loadProductMaterials();
    } catch (error) {
      toast.error(error.response?.data?.message || "Failed to add material");
    }
  };

  const handleDelete = async (materialId) => {
    if (!window.confirm("Remove this material from the product?")) return;

    try {
      // ✅ CORRIGIDO: Usar a estrutura de URL do backend
      await axios.delete(`/products/${id}/materials/${materialId}`);
      toast.success("Material removed!");
      loadProductMaterials();
    } catch (error) {
      toast.error("Failed to remove material");
    }
  };

  if (loading) return <Loading fullScreen />;

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <Button variant="secondary" onClick={() => navigate("/products")}>
            ← Back to Products
          </Button>
          <h1 className="text-3xl font-bold text-gray-900 mt-4">
            Product Materials
          </h1>
          <p className="text-gray-600 mt-1">
            {product?.name} ({product?.code})
          </p>
        </div>
        <Button onClick={() => setIsModalOpen(true)}>+ Add Material</Button>
      </div>

      <Card>
        <h2 className="text-xl font-semibold mb-4">Required Materials</h2>

        {productMaterials.length === 0 ? (
          <div className="text-center py-12 text-gray-500">
            No materials added yet. Add materials to enable production
            suggestions.
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Code
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Material
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Required Quantity
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Current Stock
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {productMaterials.map((pm) => (
                  <tr key={pm.id}>
                    <td className="px-6 py-4 whitespace-nowrap font-mono text-sm">
                      {pm.rawMaterial?.code}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      {pm.rawMaterial?.name}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap font-semibold">
                      {pm.requiredQuantity} {pm.rawMaterial?.unit}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span
                        className={
                          pm.rawMaterial?.stockQuantity < pm.requiredQuantity
                            ? "text-red-600 font-semibold"
                            : "text-green-600"
                        }
                      >
                        {pm.rawMaterial?.stockQuantity} {pm.rawMaterial?.unit}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <Button
                        variant="danger"
                        size="sm"
                        onClick={() => handleDelete(pm.rawMaterialId)}
                      >
                        Remove
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </Card>

      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title="Add Required Material"
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Raw Material
            </label>
            <select
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              value={formData.rawMaterialId}
              onChange={(e) =>
                setFormData({ ...formData, rawMaterialId: e.target.value })
              }
              required
            >
              <option value="">Select a material...</option>
              {rawMaterials.map((material) => (
                <option key={material.id} value={material.id}>
                  {material.name} ({material.code}) - Stock:{" "}
                  {material.stockQuantity} {material.unit}
                </option>
              ))}
            </select>
          </div>

          <Input
            label="Required Quantity per Unit"
            type="number"
            step="0.001"
            min="0.001"
            value={formData.requiredQuantity}
            onChange={(e) =>
              setFormData({ ...formData, requiredQuantity: e.target.value })
            }
            required
            placeholder="How much is needed to produce 1 unit"
          />

          <div className="flex gap-3 pt-4">
            <Button type="submit" className="flex-1">
              Add Material
            </Button>
            <Button
              type="button"
              variant="secondary"
              onClick={() => setIsModalOpen(false)}
              className="flex-1"
            >
              Cancel
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
}

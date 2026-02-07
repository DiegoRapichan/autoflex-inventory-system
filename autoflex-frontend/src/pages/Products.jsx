import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  fetchProducts,
  createProduct,
  updateProduct,
  deleteProduct,
} from "../store/slices/productSlice";
import Button from "../components/common/Button";
import Input from "../components/common/Input";
import Modal from "../components/common/Modal";
import Table from "../components/common/Table";
import Card from "../components/common/Card";
import Loading from "../components/common/Loading";
import ConfirmDialog from "../components/common/ConfirmDialog";
import { formatCurrency } from "../utils/formatters";

export default function Products() {
  const dispatch = useDispatch();
  const {
    items: products,
    loading,
    error,
  } = useSelector((state) => state.products);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [productToDelete, setProductToDelete] = useState(null);

  const [formData, setFormData] = useState({
    code: "",
    name: "",
    value: "",
  });

  const [formErrors, setFormErrors] = useState({});

  useEffect(() => {
    dispatch(fetchProducts());
  }, [dispatch]);

  const handleOpenModal = (product = null) => {
    if (product) {
      setEditingProduct(product);
      setFormData({
        code: product.code,
        name: product.name,
        value: product.value,
      });
    } else {
      setEditingProduct(null);
      setFormData({ code: "", name: "", value: "" });
    }
    setFormErrors({});
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingProduct(null);
    setFormData({ code: "", name: "", value: "" });
    setFormErrors({});
  };

  const validateForm = () => {
    const errors = {};

    if (!formData.code.trim()) {
      errors.code = "Code is required";
    }

    if (!formData.name.trim()) {
      errors.name = "Name is required";
    }

    if (!formData.value || formData.value <= 0) {
      errors.value = "Value must be greater than 0";
    }

    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) return;

    const productData = {
      code: formData.code.trim(),
      name: formData.name.trim(),
      value: parseFloat(formData.value),
    };

    if (editingProduct) {
      await dispatch(
        updateProduct({ id: editingProduct.id, data: productData }),
      );
    } else {
      await dispatch(createProduct(productData));
    }

    handleCloseModal();
  };

  const handleDeleteClick = (product) => {
    setProductToDelete(product);
    setIsDeleteDialogOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (productToDelete) {
      await dispatch(deleteProduct(productToDelete.id));
      setIsDeleteDialogOpen(false);
      setProductToDelete(null);
    }
  };

  const columns = [
    {
      key: "code",
      label: "Code",
      render: (product) => (
        <span className="font-mono font-semibold">{product.code}</span>
      ),
    },
    {
      key: "name",
      label: "Name",
    },
    {
      key: "value",
      label: "Value",
      render: (product) => (
        <span className="font-semibold text-green-600">
          {formatCurrency(product.value)}
        </span>
      ),
    },
    {
      key: "actions",
      label: "Actions",
      render: (product) => (
        <div className="flex gap-2">
          <Button
            variant="secondary"
            size="sm"
            onClick={() => handleOpenModal(product)}
          >
            Edit
          </Button>
          <Button
            variant="danger"
            size="sm"
            onClick={() => handleDeleteClick(product)}
          >
            Delete
          </Button>
        </div>
      ),
    },
  ];

  if (loading && products.length === 0) {
    return <Loading fullScreen />;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Products</h1>
          <p className="text-gray-600 mt-1">Manage your product catalog</p>
        </div>
        <Button onClick={() => handleOpenModal()}>+ New Product</Button>
      </div>

      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
          {error}
        </div>
      )}

      <Card>
        <Table
          columns={columns}
          data={products}
          emptyMessage="No products found. Create your first product!"
        />
      </Card>

      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={editingProduct ? "Edit Product" : "New Product"}
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Code"
            name="code"
            value={formData.code}
            onChange={(e) => setFormData({ ...formData, code: e.target.value })}
            error={formErrors.code}
            required
            disabled={editingProduct !== null}
            placeholder="PROD-001"
          />

          <Input
            label="Name"
            name="name"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            error={formErrors.name}
            required
            placeholder="Product name"
          />

          <Input
            label="Value"
            name="value"
            type="number"
            step="0.01"
            min="0"
            value={formData.value}
            onChange={(e) =>
              setFormData({ ...formData, value: e.target.value })
            }
            error={formErrors.value}
            required
            placeholder="150.00"
          />

          <div className="flex gap-3 pt-4">
            <Button type="submit" className="flex-1">
              {editingProduct ? "Update" : "Create"}
            </Button>
            <Button
              type="button"
              variant="secondary"
              onClick={handleCloseModal}
              className="flex-1"
            >
              Cancel
            </Button>
          </div>
        </form>
      </Modal>

      <ConfirmDialog
        isOpen={isDeleteDialogOpen}
        onClose={() => setIsDeleteDialogOpen(false)}
        onConfirm={handleConfirmDelete}
        title="Delete Product"
        message={`Are you sure you want to delete "${productToDelete?.name}"? This action cannot be undone.`}
      />
    </div>
  );
}

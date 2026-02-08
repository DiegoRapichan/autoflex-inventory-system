import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  fetchRawMaterials,
  createRawMaterial,
  updateRawMaterial,
  deleteRawMaterial,
} from "../store/slices/rawMaterialSlice";
import Button from "../components/common/Button";
import Input from "../components/common/Input";
import Modal from "../components/common/Modal";
import Table from "../components/common/Table";
import Card from "../components/common/Card";
import Loading from "../components/common/Loading";
import Badge from "../components/common/Badge";
import ConfirmDialog from "../components/common/ConfirmDialog";
import {
  formatNumber,
  isLowStock,
  getStockBadgeClass,
} from "../utils/formatters";

export default function RawMaterials() {
  const dispatch = useDispatch();
  const {
    items: materials,
    loading,
    error,
  } = useSelector((state) => state.rawMaterials);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingMaterial, setEditingMaterial] = useState(null);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [materialToDelete, setMaterialToDelete] = useState(null);

  const [formData, setFormData] = useState({
    code: "",
    name: "",
    stockQuantity: "",
    unit: "",
  });

  const [formErrors, setFormErrors] = useState({});

  useEffect(() => {
    dispatch(fetchRawMaterials());
  }, [dispatch]);

  const handleOpenModal = (material = null) => {
    if (material) {
      setEditingMaterial(material);
      setFormData({
        code: material.code,
        name: material.name,
        stockQuantity: material.stockQuantity,
        unit: material.unit,
      });
    } else {
      setEditingMaterial(null);
      setFormData({ code: "", name: "", stockQuantity: "", unit: "" });
    }
    setFormErrors({});
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingMaterial(null);
    setFormData({ code: "", name: "", stockQuantity: "", unit: "" });
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

    if (!formData.stockQuantity || formData.stockQuantity < 0) {
      errors.stockQuantity = "Stock quantity must be 0 or greater";
    }

    if (!formData.unit.trim()) {
      errors.unit = "Unit is required";
    }

    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) return;

    const materialData = {
      code: formData.code.trim(),
      name: formData.name.trim(),
      stockQuantity: parseFloat(formData.stockQuantity),
      unit: formData.unit.trim(),
    };

    if (editingMaterial) {
      await dispatch(
        updateRawMaterial({ id: editingMaterial.id, data: materialData }),
      );
    } else {
      await dispatch(createRawMaterial(materialData));
    }

    handleCloseModal();
  };

  const handleDeleteClick = (material) => {
    setMaterialToDelete(material);
    setIsDeleteDialogOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (materialToDelete) {
      await dispatch(deleteRawMaterial(materialToDelete.id));
      setIsDeleteDialogOpen(false);
      setMaterialToDelete(null);
    }
  };

  const columns = [
    {
      key: "code",
      label: "Code",
      render: (material) => (
        <span className="font-mono font-semibold">{material.code}</span>
      ),
    },
    {
      key: "name",
      label: "Name",
    },
    {
      key: "stock",
      label: "Stock",
      render: (material) => (
        <div className="flex items-center gap-2">
          <span className="font-semibold">
            {formatNumber(material.stockQuantity)} {material.unit}
          </span>
          <Badge className={getStockBadgeClass(material.stockQuantity)}>
            {isLowStock(material.stockQuantity) ? "Low" : "OK"}
          </Badge>
        </div>
      ),
    },
    {
      key: "unit",
      label: "Unit",
      render: (material) => (
        <span className="text-gray-600">{material.unit}</span>
      ),
    },
    {
      key: "actions",
      label: "Actions",
      render: (material) => (
        <div className="flex gap-2">
          <Button
            variant="secondary"
            size="sm"
            onClick={() => handleOpenModal(material)}
          >
            Edit
          </Button>
          <Button
            variant="danger"
            size="sm"
            onClick={() => handleDeleteClick(material)}
          >
            Delete
          </Button>
        </div>
      ),
    },
  ];

  if (loading && materials.length === 0) {
    return <Loading fullScreen />;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Raw Materials</h1>
          <p className="text-gray-600 mt-1">Manage your inventory stock</p>
        </div>
        <Button onClick={() => handleOpenModal()}>+ New Material</Button>
      </div>

      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
          {error}
        </div>
      )}

      <Card>
        <Table
          columns={columns}
          data={materials}
          emptyMessage="No raw materials found. Create your first material!"
        />
      </Card>

      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={editingMaterial ? "Edit Raw Material" : "New Raw Material"}
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Code"
            name="code"
            value={formData.code}
            onChange={(e) => setFormData({ ...formData, code: e.target.value })}
            error={formErrors.code}
            required
            disabled={editingMaterial !== null}
            placeholder="MAT-001"
          />

          <Input
            label="Name"
            name="name"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            error={formErrors.name}
            required
            placeholder="Material name"
          />

          <Input
            label="Stock Quantity"
            name="stockQuantity"
            type="number"
            step="0.001"
            min="0"
            value={formData.stockQuantity}
            onChange={(e) =>
              setFormData({ ...formData, stockQuantity: e.target.value })
            }
            error={formErrors.stockQuantity}
            required
            placeholder="100.000"
          />

          <Input
            label="Unit"
            name="unit"
            value={formData.unit}
            onChange={(e) => setFormData({ ...formData, unit: e.target.value })}
            error={formErrors.unit}
            required
            placeholder="KG, L, UN, M, etc"
          />

          <div className="flex gap-3 pt-4">
            <Button type="submit" className="flex-1">
              {editingMaterial ? "Update" : "Create"}
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
        title="Delete Raw Material"
        message={`Are you sure you want to delete "${materialToDelete?.name}"? This action cannot be undone.`}
      />
    </div>
  );
}

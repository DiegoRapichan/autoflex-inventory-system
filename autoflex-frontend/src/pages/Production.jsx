import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchProductionSuggestions } from "../store/slices/productionSlice";
import Button from "../components/common/Button";
import Card from "../components/common/Card";
import Loading from "../components/common/Loading";
import Badge from "../components/common/Badge";

export default function Production() {
  const dispatch = useDispatch();
  const { suggestions, totalValue, totalUnits, loading, error } = useSelector(
    (state) => state.production,
  );

  useEffect(() => {
    dispatch(fetchProductionSuggestions());
  }, [dispatch]);

  const handleRefresh = () => {
    dispatch(fetchProductionSuggestions()); // ✅ CORRIGIDO
  };

  if (loading) return <Loading fullScreen />;

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold bg-gradient-to-r from-purple-600 to-blue-600 bg-clip-text text-transparent">
            Production Suggestions
          </h1>
          <p className="text-gray-600 mt-1">
            Intelligent recommendations based on current inventory
          </p>
        </div>
        <Button
          onClick={handleRefresh}
          className="bg-gradient-to-r from-blue-500 to-blue-600 hover:from-blue-600 hover:to-blue-700"
        >
          🔄 Refresh
        </Button>
      </div>

      {error && (
        <div className="bg-red-50 border-l-4 border-red-500 p-4 rounded-r-lg">
          <p className="text-red-800">Error: {error}</p>
        </div>
      )}

      {/* Statistics Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card className="bg-gradient-to-br from-blue-500 to-blue-600 text-white shadow-lg hover:shadow-xl transition-shadow">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-blue-100 text-sm font-medium uppercase tracking-wide">
                Total Suggestions
              </p>
              <p className="text-4xl font-bold mt-2">
                {suggestions?.length || 0}
              </p>
            </div>
            <div className="bg-white bg-opacity-20 rounded-full p-4">
              <svg
                className="w-8 h-8"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"
                />
              </svg>
            </div>
          </div>
        </Card>

        <Card className="bg-gradient-to-br from-green-500 to-green-600 text-white shadow-lg hover:shadow-xl transition-shadow">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-green-100 text-sm font-medium uppercase tracking-wide">
                Total Production Value
              </p>
              <p className="text-4xl font-bold mt-2">
                R$ {totalValue?.toFixed(2) || "0.00"}
              </p>
            </div>
            <div className="bg-white bg-opacity-20 rounded-full p-4">
              <svg
                className="w-8 h-8"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                />
              </svg>
            </div>
          </div>
        </Card>

        <Card className="bg-gradient-to-br from-purple-500 to-purple-600 text-white shadow-lg hover:shadow-xl transition-shadow">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-purple-100 text-sm font-medium uppercase tracking-wide">
                Total Units
              </p>
              <p className="text-4xl font-bold mt-2">{totalUnits || 0}</p>
            </div>
            <div className="bg-white bg-opacity-20 rounded-full p-4">
              <svg
                className="w-8 h-8"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"
                />
              </svg>
            </div>
          </div>
        </Card>
      </div>

      {/* Production Suggestions */}
      <div className="space-y-4">
        {!suggestions || suggestions.length === 0 ? (
          <Card className="text-center py-16">
            <div className="text-gray-400 mb-4">
              <svg
                className="w-24 h-24 mx-auto"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={1.5}
                  d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4"
                />
              </svg>
            </div>
            <h3 className="text-xl font-semibold text-gray-700 mb-2">
              No production suggestions available
            </h3>
            <p className="text-gray-500 mb-6">
              Add products and raw materials to see intelligent production
              recommendations
            </p>
            <div className="flex gap-3 justify-center">
              <Button onClick={() => (window.location.href = "/products")}>
                Add Products
              </Button>
              <Button
                variant="secondary"
                onClick={() => (window.location.href = "/raw-materials")}
              >
                Add Materials
              </Button>
            </div>
          </Card>
        ) : (
          suggestions.map((suggestion) => (
            <Card
              key={`suggestion-${suggestion.productId}`} // ✅ Prefixo adicionado
              className="hover:shadow-lg transition-shadow border-l-4 border-blue-500"
            >
              <div className="flex items-start justify-between mb-4">
                <div className="flex-1">
                  <div className="flex items-center gap-3 mb-2">
                    <h3 className="text-2xl font-bold text-gray-900">
                      {suggestion.productName}
                    </h3>
                    <Badge
                      variant={suggestion.canProduce ? "success" : "danger"}
                    >
                      {suggestion.canProduce
                        ? "✓ Can Produce"
                        : "✗ Insufficient Stock"}
                    </Badge>
                  </div>
                  <p className="text-sm text-gray-500 font-mono">
                    Code: {suggestion.productCode}
                  </p>
                </div>
                <div className="text-right">
                  <p className="text-sm text-gray-600 mb-1">Unit Value</p>
                  <p className="text-2xl font-bold text-green-600">
                    R$ {parseFloat(suggestion.unitValue || 0).toFixed(2)}
                  </p>
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6 p-4 bg-gray-50 rounded-lg">
                <div>
                  <p className="text-sm text-gray-600 mb-1">Maximum Quantity</p>
                  <p className="text-3xl font-bold text-blue-600">
                    {suggestion.maxQuantity} units
                  </p>
                </div>
                <div>
                  <p className="text-sm text-gray-600 mb-1">Total Value</p>
                  <p className="text-3xl font-bold text-green-600">
                    R$ {parseFloat(suggestion.totalValue || 0).toFixed(2)}
                  </p>
                </div>
                {suggestion.limitingMaterial && (
                  <div>
                    <p className="text-sm text-gray-600 mb-1">
                      Limiting Material
                    </p>
                    <p className="text-lg font-semibold text-orange-600">
                      {suggestion.limitingMaterial}
                    </p>
                  </div>
                )}
              </div>

              <div>
                <h4 className="font-semibold text-gray-700 mb-3 flex items-center gap-2">
                  <span className="w-1 h-6 bg-blue-500 rounded"></span>
                  Material Requirements
                </h4>
                <div className="overflow-x-auto">
                  <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-100">
                      <tr>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">
                          Material
                        </th>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">
                          Required/Unit
                        </th>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">
                          Total Required
                        </th>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">
                          Available
                        </th>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">
                          Remaining
                        </th>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">
                          Status
                        </th>
                      </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                      {suggestion.materialRequirements?.map((req) => (
                        <tr
                          key={`material-${suggestion.productId}-${req.materialId}`} // ✅ Key composta única
                          className={!req.sufficient ? "bg-red-50" : ""}
                        >
                          <td className="px-4 py-3 whitespace-nowrap font-medium text-gray-900">
                            {req.materialName}
                          </td>
                          <td className="px-4 py-3 whitespace-nowrap text-gray-700">
                            {parseFloat(req.requiredPerUnit).toFixed(3)}{" "}
                            {req.unit}
                          </td>
                          <td className="px-4 py-3 whitespace-nowrap font-semibold text-blue-600">
                            {parseFloat(req.totalRequired).toFixed(3)}{" "}
                            {req.unit}
                          </td>
                          <td className="px-4 py-3 whitespace-nowrap text-gray-700">
                            {parseFloat(req.availableStock).toFixed(3)}{" "}
                            {req.unit}
                          </td>
                          <td className="px-4 py-3 whitespace-nowrap">
                            <span
                              className={
                                req.remainingStock >= 0
                                  ? "text-green-600 font-semibold"
                                  : "text-red-600 font-semibold"
                              }
                            >
                              {parseFloat(req.remainingStock).toFixed(3)}{" "}
                              {req.unit}
                            </span>
                          </td>
                          <td className="px-4 py-3 whitespace-nowrap">
                            <Badge
                              variant={req.sufficient ? "success" : "danger"}
                            >
                              {req.sufficient
                                ? "✓ Sufficient"
                                : "✗ Insufficient"}
                            </Badge>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            </Card>
          ))
        )}
      </div>
    </div>
  );
}

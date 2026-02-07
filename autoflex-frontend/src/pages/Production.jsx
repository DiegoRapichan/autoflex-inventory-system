import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchProductionSuggestions } from "../store/slices/productionSlice";
import Button from "../components/common/Button";
import Card from "../components/common/Card";
import Loading from "../components/common/Loading";
import Badge from "../components/common/Badge";
import { formatCurrency, formatNumber } from "../utils/formatters";

export default function Production() {
  const dispatch = useDispatch();
  const { suggestions, totalValue, totalUnits, warnings, loading, error } =
    useSelector((state) => state.production);

  useEffect(() => {
    dispatch(fetchProductionSuggestions());
  }, [dispatch]);

  const handleRefresh = () => {
    dispatch(fetchProductionSuggestions());
  };

  if (loading && suggestions.length === 0) {
    return <Loading fullScreen />;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">
            Production Suggestions
          </h1>
          <p className="text-gray-600 mt-1">
            Intelligent recommendations based on available stock
          </p>
        </div>
        <Button onClick={handleRefresh} disabled={loading}>
          {loading ? "Loading..." : "🔄 Refresh"}
        </Button>
      </div>

      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
          {error}
        </div>
      )}

      {warnings && warnings.length > 0 && (
        <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
          <h3 className="font-semibold text-yellow-900 mb-2">⚠️ Warnings</h3>
          <ul className="list-disc list-inside space-y-1 text-yellow-800">
            {warnings.map((warning, index) => (
              <li key={index}>{warning}</li>
            ))}
          </ul>
        </div>
      )}

      {suggestions.length === 0 ? (
        <Card>
          <div className="text-center py-12">
            <p className="text-gray-500 text-lg">
              No production suggestions available.
            </p>
            <p className="text-gray-400 mt-2">
              Make sure you have products with materials configured and stock
              available.
            </p>
          </div>
        </Card>
      ) : (
        <>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <Card className="bg-gradient-to-br from-blue-50 to-blue-100">
              <div className="text-center">
                <p className="text-blue-600 font-semibold mb-1">
                  Total Suggestions
                </p>
                <p className="text-3xl font-bold text-blue-900">
                  {suggestions.length}
                </p>
              </div>
            </Card>

            <Card className="bg-gradient-to-br from-green-50 to-green-100">
              <div className="text-center">
                <p className="text-green-600 font-semibold mb-1">Total Value</p>
                <p className="text-3xl font-bold text-green-900">
                  {formatCurrency(totalValue)}
                </p>
              </div>
            </Card>

            <Card className="bg-gradient-to-br from-purple-50 to-purple-100">
              <div className="text-center">
                <p className="text-purple-600 font-semibold mb-1">
                  Total Units
                </p>
                <p className="text-3xl font-bold text-purple-900">
                  {totalUnits}
                </p>
              </div>
            </Card>
          </div>

          <div className="space-y-4">
            {suggestions.map((suggestion, index) => (
              <Card
                key={suggestion.productId}
                className="hover:shadow-lg transition-shadow"
              >
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <div className="flex items-center gap-3 mb-2">
                      <Badge className="bg-blue-100 text-blue-800">
                        #{index + 1}
                      </Badge>
                      <h3 className="text-xl font-bold text-gray-900">
                        {suggestion.productName}
                      </h3>
                      <span className="text-gray-500 font-mono text-sm">
                        {suggestion.productCode}
                      </span>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
                      <div>
                        <p className="text-sm text-gray-600">Max Quantity</p>
                        <p className="text-2xl font-bold text-gray-900">
                          {suggestion.maxQuantity} units
                        </p>
                      </div>
                      <div>
                        <p className="text-sm text-gray-600">Unit Value</p>
                        <p className="text-2xl font-bold text-green-600">
                          {formatCurrency(suggestion.unitValue)}
                        </p>
                      </div>
                      <div>
                        <p className="text-sm text-gray-600">Total Value</p>
                        <p className="text-2xl font-bold text-green-700">
                          {formatCurrency(suggestion.totalValue)}
                        </p>
                      </div>
                    </div>

                    {suggestion.materialRequirements &&
                      suggestion.materialRequirements.length > 0 && (
                        <div>
                          <h4 className="font-semibold text-gray-700 mb-2">
                            Material Requirements:
                          </h4>
                          <div className="overflow-x-auto">
                            <table className="min-w-full divide-y divide-gray-200">
                              <thead className="bg-gray-50">
                                <tr>
                                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">
                                    Material
                                  </th>
                                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">
                                    Required/Unit
                                  </th>
                                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">
                                    Total Required
                                  </th>
                                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">
                                    Available
                                  </th>
                                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">
                                    Remaining
                                  </th>
                                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">
                                    Status
                                  </th>
                                </tr>
                              </thead>
                              <tbody className="bg-white divide-y divide-gray-200">
                                {suggestion.materialRequirements.map((req) => (
                                  <tr key={req.materialId}>
                                    <td className="px-4 py-2 text-sm text-gray-900">
                                      {req.materialName}
                                    </td>
                                    <td className="px-4 py-2 text-sm text-gray-600">
                                      {formatNumber(req.requiredPerUnit)}{" "}
                                      {req.unit}
                                    </td>
                                    <td className="px-4 py-2 text-sm font-semibold text-gray-900">
                                      {formatNumber(req.totalRequired)}{" "}
                                      {req.unit}
                                    </td>
                                    <td className="px-4 py-2 text-sm text-gray-600">
                                      {formatNumber(req.availableStock)}{" "}
                                      {req.unit}
                                    </td>
                                    <td className="px-4 py-2 text-sm text-gray-600">
                                      {formatNumber(req.remainingStock)}{" "}
                                      {req.unit}
                                    </td>
                                    <td className="px-4 py-2 text-sm">
                                      <Badge
                                        className={
                                          req.sufficient
                                            ? "bg-green-100 text-green-800"
                                            : "bg-red-100 text-red-800"
                                        }
                                      >
                                        {req.sufficient
                                          ? "✓ OK"
                                          : "✗ Insufficient"}
                                      </Badge>
                                    </td>
                                  </tr>
                                ))}
                              </tbody>
                            </table>
                          </div>
                        </div>
                      )}

                    {suggestion.limitingMaterial && (
                      <div className="mt-3 p-3 bg-yellow-50 border border-yellow-200 rounded">
                        <p className="text-sm text-yellow-800">
                          <span className="font-semibold">
                            ⚠️ Limiting Material:
                          </span>{" "}
                          {suggestion.limitingMaterial}
                        </p>
                      </div>
                    )}
                  </div>
                </div>
              </Card>
            ))}
          </div>
        </>
      )}
    </div>
  );
}

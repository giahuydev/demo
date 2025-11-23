// src/components/WeatherChart.jsx
import {
  LineChart,
  Line,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";

export default function WeatherChart({ data }) {
  const chartData = data.map((item) => {
    const hour = new Date(item.dt * 1000).getHours();
    return {
      time: `${hour}:00`,
      temp: item.main.temp,
      rain: item.rain ? item.rain["1h"] : 0,
    };
  });

  return (
    <div className="space-y-8">
      {/* Temperature Chart */}
      <div>
        <h3 className="text-xl font-bold text-gray-900 dark:text-white mb-4">
          📈 Nhiệt độ 24 giờ
        </h3>
        <ResponsiveContainer width="100%" height={250}>
          <LineChart data={chartData}>
            <CartesianGrid strokeDasharray="3 3" stroke="#374151" />
            <XAxis dataKey="time" stroke="#9CA3AF" />
            <YAxis stroke="#9CA3AF" />
            <Tooltip
              contentStyle={{
                backgroundColor: "#1F2937",
                border: "1px solid #374151",
                borderRadius: "8px",
              }}
            />
            <Legend />
            <Line
              type="monotone"
              dataKey="temp"
              stroke="#3B82F6"
              strokeWidth={3}
              name="Nhiệt độ (°C)"
              dot={{ fill: "#3B82F6", r: 4 }}
            />
          </LineChart>
        </ResponsiveContainer>
      </div>

      {/* Rain Chart */}
      <div>
        <h3 className="text-xl font-bold text-gray-900 dark:text-white mb-4">
          🌧️ Lượng mưa 24 giờ
        </h3>
        <ResponsiveContainer width="100%" height={250}>
          <BarChart data={chartData}>
            <CartesianGrid strokeDasharray="3 3" stroke="#374151" />
            <XAxis dataKey="time" stroke="#9CA3AF" />
            <YAxis stroke="#9CA3AF" />
            <Tooltip
              contentStyle={{
                backgroundColor: "#1F2937",
                border: "1px solid #374151",
                borderRadius: "8px",
              }}
            />
            <Legend />
            <Bar dataKey="rain" fill="#06B6D4" name="Lượng mưa (mm)" />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}

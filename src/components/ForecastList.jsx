// 6. ForecastList.jsx
const formatDate = (timestamp) =>
  new Date(timestamp * 1000).toLocaleDateString("vi-VN", {
    weekday: "short",
    day: "numeric",
    month: "numeric",
  });

export default function ForecastList({ data }) {
  if (!data?.list) return null;
  const daily = data.list
    .filter((item) => item.dt_txt.includes("12:00:00"))
    .slice(0, 5);

  return (
    <div className="flex gap-6 overflow-x-auto py-4">
      {daily.map((item, i) => (
        <div key={i} className="text-center min-w-24">
          <p className="font-semibold">{formatDate(item.dt)}</p>
          <img
            src={`https://openweathermap.org/img/wn/${item.weather[0].icon}@2x.png`}
            alt=""
            className="w-16 h-16 mx-auto"
          />
          <p className="text-2xl font-bold">{Math.round(item.main.temp)}°</p>
          <p className="text-xs opacity-70 capitalize">
            {item.weather[0].description}
          </p>
        </div>
      ))}
    </div>
  );
}

  export default function HourlyForecast({ data }) {
    const formatHour = (timestamp) =>
      new Date(timestamp * 1000).getHours() + ":00";

    return (
      <div className="flex gap-6 overflow-x-auto pb-4 scrollbar-thin scrollbar-thumb-slate-700">
        {data.map((h, i) => (
          <div
            key={i}
            className="bg-slate-800/60 backdrop-blur rounded-3xl p-6 text-center min-w-36 border border-slate-700"
          >
            <p className="text-lg font-semibold opacity-80">{formatHour(h.dt)}</p>
            <img
              src={`https://openweathermap.org/img/wn/${h.weather[0].icon}@2x.png`}
              className="w-20 mx-auto my-3"
            />
            <p className="text-3xl font-bold text-cyan-400">
              {Math.round(h.main.temp)}°
            </p>
            <p className="text-sm opacity-70 mt-2">{h.weather[0].description}</p>
          </div>
        ))}
      </div>
    );
  }

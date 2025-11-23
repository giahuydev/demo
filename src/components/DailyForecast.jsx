const days = ["CN", "T2", "T3", "T4", "T5", "T6", "T7"];

export default function DailyForecast({ data }) {
  return (
    <div className="grid grid-cols-2 md:grid-cols-5 gap-6">
      {data.map((d, i) => {
        const date = new Date(d.dt * 1000);
        return (
          <div
            key={i}
            className="bg-slate-800/60 backdrop-blur rounded-3xl p-6 text-center border border-slate-700"
          >
            <p className="text-xl font-bold">
              {i === 0 ? "Hôm nay" : days[date.getDay()]}
            </p>
            <p className="text-sm opacity-70">
              {date.getDate()}/{date.getMonth() + 1}
            </p>
            <img
              src={`https://openweathermap.org/img/wn/${d.weather[0].icon}@2x.png`}
              className="w-20 mx-auto my-3"
            />
            <p className="text-4xl font-bold text-cyan-400">
              {Math.round(d.temp.day)}°
            </p>
            <p className="text-sm opacity-70 mt-2">
              {d.weather[0].description}
            </p>
          </div>
        );
      })}
    </div>
  );
}

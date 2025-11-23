export default function WeatherCard({ data }) {
  return (
    <div className="bg-slate-800/60 backdrop-blur-xl rounded-3xl p-10 border border-slate-700 shadow-2xl">
      <h1 className="text-4xl font-bold text-center mb-3">{data.name}</h1>
      <p className="text-2xl text-center opacity-80 mb-8">
        {data.weather[0].description}
      </p>

      <div className="flex justify-center items-center gap-12 my-10">
        <img
          src={`https://openweathermap.org/img/wn/${data.weather[0].icon}@4x.png`}
          alt="weather"
          className="w-40 drop-shadow-2xl"
        />
        <div className="text-8xl font-bold text-cyan-400">
          {Math.round(data.main.temp)}°
        </div>
      </div>

      <div className="grid grid-cols-2 gap-6 text-lg mt-8">
        <div className="flex items-center gap-3">
          <span>Cảm giác:</span>
          <span className="font-semibold text-cyan-400">
            {Math.round(data.main.feels_like)}°C
          </span>
        </div>
        <div className="flex items-center gap-3">
          <span>Độ ẩm:</span>
          <span className="font-semibold text-cyan-400">
            {data.main.humidity}%
          </span>
        </div>
        <div className="flex items-center gap-3">
          <span>Gió:</span>
          <span className="font-semibold text-cyan-400">
            {data.wind.speed.toFixed(1)} m/s
          </span>
        </div>
        <div className="flex items-center gap-3">
          <span>Mây:</span>
          <span className="font-semibold text-cyan-400">
            {data.clouds.all}%
          </span>
        </div>
      </div>
    </div>
  );
}

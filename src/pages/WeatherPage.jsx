// src/pages/WeatherPage.jsx
import { useState, useEffect } from "react";
import Sidebar from "../components/Sidebar";
import LoginModal from "../components/LoginModal";
import RegisterModal from "../components/RegisterModal";
import WeatherChart from "../components/WeatherChart";

// Fake data with rain information
const fakeData = {
  current: {
    name: "Thành phố Hồ Chí Minh",
    main: { temp: 29, feels_like: 33, humidity: 78 },
    wind: { speed: 4.2, deg: 180 },
    weather: [{ description: "Mây rải rác", icon: "02d", main: "Clouds" }],
    clouds: { all: 68 },
    sys: { country: "VN", sunrise: 1704067200, sunset: 1704105600 },
    coord: { lat: 10.7626, lon: 106.6602 },
  },
  hourly: Array.from({ length: 24 }, (_, i) => {
    const hasRain = i % 6 === 0 || i % 7 === 0;
    return {
      dt: Math.floor((Date.now() + i * 3600000) / 1000),
      main: { temp: Math.round(27 + Math.sin((i - 6) / 3) * 5) },
      weather: [{ main: hasRain ? "Rain" : "Clear", icon: "01d" }],
      rain: hasRain
        ? { "1h": Number((Math.random() * 8).toFixed(1)) }
        : undefined,
    };
  }),
};

export default function WeatherPage() {
  const [isDarkMode, setIsDarkMode] = useState(false);
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [isLoginOpen, setIsLoginOpen] = useState(false);
  const [isRegisterOpen, setIsRegisterOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");

  useEffect(() => {
    if (isDarkMode) {
      document.documentElement.classList.add("dark");
    } else {
      document.documentElement.classList.remove("dark");
    }
  }, [isDarkMode]);

  return (
    <>
      <Sidebar
        isOpen={sidebarOpen}
        toggleSidebar={() => setSidebarOpen(!sidebarOpen)}
      />

      <div className="min-h-screen bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
        {/* Header */}
        <header className="sticky top-0 bg-white/95 dark:bg-gray-800/95 backdrop-blur-sm border-b border-gray-200 dark:border-gray-700 z-30 shadow-sm">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex items-center justify-between h-16">
              <button
                onClick={() => setSidebarOpen(true)}
                className="p-2 hover:bg-gray-100 dark:hover:bg-gray-700 rounded-lg transition text-gray-900 dark:text-white"
              >
                <span className="text-2xl">☰</span>
              </button>

              <div className="hidden lg:flex items-center space-x-2">
                <span className="text-3xl">☁️</span>
                <span className="text-xl font-bold text-gray-900 dark:text-white">
                  WeatherApp
                </span>
              </div>

              <nav className="hidden md:flex items-center space-x-2">
                <button className="px-6 py-2 bg-blue-500 text-white rounded-full font-medium hover:bg-blue-600 transition">
                  Thời tiết
                </button>
                <button className="px-6 py-2 text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 rounded-full transition">
                  Lịch hoạt động
                </button>
              </nav>

              <div className="flex items-center space-x-2">
                <button
                  onClick={() => setIsDarkMode(!isDarkMode)}
                  className="p-2 hover:bg-gray-100 dark:hover:bg-gray-700 rounded-lg transition"
                  title={isDarkMode ? "Chế độ sáng" : "Chế độ tối"}
                >
                  <span className="text-2xl">{isDarkMode ? "☀️" : "🌙"}</span>
                </button>
                <button
                  onClick={() => setIsLoginOpen(true)}
                  className="hidden sm:block px-4 py-2 text-blue-600 dark:text-blue-400 hover:bg-blue-50 dark:hover:bg-blue-900/30 rounded-lg font-medium transition"
                >
                  Đăng nhập
                </button>
                <button
                  onClick={() => setIsRegisterOpen(true)}
                  className="hidden sm:block px-4 py-2 bg-blue-500 text-white rounded-lg font-medium hover:bg-blue-600 transition"
                >
                  Đăng ký
                </button>
              </div>
            </div>
          </div>
        </header>

        {/* Main Content */}
        <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          {/* Search Bar */}
          <div className="mb-8">
            <div className="relative max-w-2xl mx-auto">
              <span className="absolute left-4 top-1/2 -translate-y-1/2 text-xl">
                📍
              </span>
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                placeholder="Tìm kiếm thành phố, quận, tỉnh..."
                className="w-full pl-12 pr-4 py-4 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-2xl focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition text-gray-900 dark:text-white placeholder-gray-500"
              />
            </div>
          </div>

          {/* Weather Card & Map Grid */}
          <div className="grid lg:grid-cols-2 gap-6 mb-8">
            {/* Current Weather Card */}
            <div className="bg-gradient-to-br from-blue-500 to-blue-600 rounded-2xl p-8 text-white shadow-lg">
              <div className="flex items-start justify-between mb-6">
                <div>
                  <h2 className="text-2xl font-bold mb-1">
                    {fakeData.current.name}
                  </h2>
                  <p className="text-blue-100">
                    {fakeData.current.weather[0].description}
                  </p>
                </div>
                <span className="text-5xl">☁️</span>
              </div>

              <div className="text-6xl font-bold mb-8">
                {fakeData.current.main.temp}°C
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div className="flex items-center space-x-3">
                  <div className="p-2 bg-white/20 rounded-lg">
                    <span className="text-xl">💨</span>
                  </div>
                  <div>
                    <p className="text-sm text-blue-100">Gió</p>
                    <p className="font-semibold">
                      {fakeData.current.wind.speed} m/s
                    </p>
                  </div>
                </div>
                <div className="flex items-center space-x-3">
                  <div className="p-2 bg-white/20 rounded-lg">
                    <span className="text-xl">💧</span>
                  </div>
                  <div>
                    <p className="text-sm text-blue-100">Độ ẩm</p>
                    <p className="font-semibold">
                      {fakeData.current.main.humidity}%
                    </p>
                  </div>
                </div>
                <div className="flex items-center space-x-3">
                  <div className="p-2 bg-white/20 rounded-lg">
                    <span className="text-xl">👁️</span>
                  </div>
                  <div>
                    <p className="text-sm text-blue-100">Cảm giác</p>
                    <p className="font-semibold">
                      {fakeData.current.main.feels_like}°C
                    </p>
                  </div>
                </div>
                <div className="flex items-center space-x-3">
                  <div className="p-2 bg-white/20 rounded-lg">
                    <span className="text-xl">☁️</span>
                  </div>
                  <div>
                    <p className="text-sm text-blue-100">Mây</p>
                    <p className="font-semibold">
                      {fakeData.current.clouds.all}%
                    </p>
                  </div>
                </div>
              </div>
            </div>

            {/* Map Card */}
            <div className="bg-white dark:bg-gray-800 rounded-2xl overflow-hidden shadow-lg border border-gray-200 dark:border-gray-700">
              <div className="aspect-video bg-gray-200 dark:bg-gray-700 flex items-center justify-center">
                <div className="text-center">
                  <div className="text-6xl mb-4">🗺️</div>
                  <p className="text-gray-600 dark:text-gray-400 font-medium">
                    Bản đồ thời tiết
                  </p>
                  <p className="text-sm text-gray-500 mt-2">
                    📍 {fakeData.current.coord.lat}°,{" "}
                    {fakeData.current.coord.lon}°
                  </p>
                </div>
              </div>
            </div>
          </div>

          {/* Hourly Forecast */}
          <div className="bg-white dark:bg-gray-800 rounded-2xl p-6 shadow-lg border border-gray-200 dark:border-gray-700 mb-8">
            <h3 className="text-xl font-bold text-gray-900 dark:text-white mb-6">
              ⏰ Dự báo 24 giờ tới
            </h3>
            <div className="overflow-x-auto -mx-6 px-6">
              <div className="flex space-x-4 min-w-max pb-2">
                {fakeData.hourly.slice(0, 12).map((hour, i) => {
                  const time = new Date(hour.dt * 1000);
                  return (
                    <div
                      key={i}
                      className="flex flex-col items-center space-y-2 min-w-[80px] p-3 hover:bg-gray-50 dark:hover:bg-gray-700 rounded-xl transition"
                    >
                      <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
                        {time.getHours()}:00
                      </p>
                      <span className="text-3xl">
                        {hour.weather[0].main === "Rain" ? "🌧️" : "☁️"}
                      </span>
                      <p className="text-lg font-bold text-gray-900 dark:text-white">
                        {hour.main.temp}°
                      </p>
                    </div>
                  );
                })}
              </div>
            </div>
          </div>

          {/* Weather Chart - BIỂU ĐỒ */}
          <div className="bg-white dark:bg-gray-800 rounded-2xl p-6 shadow-lg border border-gray-200 dark:border-gray-700 mb-8">
            <h3 className="text-2xl font-bold text-gray-900 dark:text-white mb-6 text-center">
              📊 Biểu đồ thời tiết chi tiết
            </h3>
            <WeatherChart data={fakeData.hourly} />
          </div>

          {/* 7-day Forecast */}
          <div className="bg-white dark:bg-gray-800 rounded-2xl p-6 shadow-lg border border-gray-200 dark:border-gray-700">
            <h3 className="text-xl font-bold text-gray-900 dark:text-white mb-6">
              📅 Dự báo 7 ngày tới
            </h3>
            <div className="space-y-3">
              {Array.from({ length: 7 }, (_, i) => {
                const date = new Date(Date.now() + (i + 1) * 86400000);
                const dayName = date.toLocaleDateString("vi-VN", {
                  weekday: "short",
                });
                const weatherEmoji =
                  i % 3 === 0 ? "🌧️" : i % 2 === 0 ? "⛅" : "☀️";
                return (
                  <div
                    key={i}
                    className="flex items-center justify-between p-4 hover:bg-gray-50 dark:hover:bg-gray-700 rounded-xl transition"
                  >
                    <p className="font-medium text-gray-900 dark:text-white w-24 capitalize">
                      {dayName}
                    </p>
                    <div className="flex items-center space-x-3 flex-1">
                      <span className="text-2xl">{weatherEmoji}</span>
                      <p className="text-sm text-gray-600 dark:text-gray-400">
                        {i % 3 === 0
                          ? "Mưa rào"
                          : i % 2 === 0
                          ? "Nhiều mây"
                          : "Nắng đẹp"}
                      </p>
                    </div>
                    <div className="flex items-center space-x-4">
                      <span className="text-gray-600 dark:text-gray-400">
                        24°
                      </span>
                      <span className="font-bold text-gray-900 dark:text-white">
                        {Math.round(30 + i * 0.5)}°
                      </span>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        </main>
      </div>

      {/* Modals */}
      {isLoginOpen && (
        <LoginModal
          onClose={() => setIsLoginOpen(false)}
          onSwitchToRegister={() => {
            setIsLoginOpen(false);
            setIsRegisterOpen(true);
          }}
        />
      )}
      {isRegisterOpen && (
        <RegisterModal
          onClose={() => setIsRegisterOpen(false)}
          onSwitchToLogin={() => {
            setIsRegisterOpen(false);
            setIsLoginOpen(true);
          }}
        />
      )}
    </>
  );
}

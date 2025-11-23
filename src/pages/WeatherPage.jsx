// src/pages/WeatherPage.jsx
import { useState, useEffect, useMemo } from "react";
import Sidebar from "../components/Sidebar";
import LoginModal from "../components/LoginModal";
import RegisterModal from "../components/RegisterModal";
import WeatherChart from "../components/WeatherChart";
// 💡 IMPORT HOOK VÀ CONSTANTS
import useWeatherApi from "../hooks/useWeatherApi";
import { API_SOURCES } from "../constants";

// XÓA DỮ LIỆU GIẢ CŨ (fakeData)

export default function WeatherPage() {
  const [isDarkMode, setIsDarkMode] = useState(false);
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [isLoginOpen, setIsLoginOpen] = useState(false);
  const [isRegisterOpen, setIsRegisterOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");
  const [currentCity, setCurrentCity] = useState("Thành phố Hồ Chí Minh"); // Vị trí đang được hiển thị

  // State để chọn nguồn API
  const [apiSource, setApiSource] = useState(API_SOURCES.SPRING_BOOT_OPENMETEO);

  // === GỌI HOOK DỮ LIỆU THỰC TẾ ===
  const { current, hourly, daily, loading, error } = useWeatherApi(
    currentCity,
    apiSource
  );

  useEffect(() => {
    if (isDarkMode) {
      document.documentElement.classList.add("dark");
    } else {
      document.documentElement.classList.remove("dark");
    }
  }, [isDarkMode]);

  // Xử lý tìm kiếm khi người dùng nhấn Enter
  const handleSearch = (e) => {
    if ((e.key === "Enter" || e.type === "click") && searchQuery.trim()) {
      setCurrentCity(searchQuery.trim());
      setSearchQuery("");
    }
  };

  // Lọc dữ liệu hàng giờ và hàng ngày để sử dụng
  // Đảm bảo daily.list là mảng và giới hạn 7 ngày
  const forecast7Day = daily?.list?.slice(0, 7) || [];
  const forecastHourly = hourly?.slice(0, 12) || []; // Giới hạn 12 giờ

  // Dùng `current` hoặc object rỗng để tránh lỗi truy cập thuộc tính khi loading/error
  const weatherData = current || {};
  const currentTemp = Math.round(weatherData.main?.temp) || "-";
  const locationName = weatherData.name || currentCity;
  const description = weatherData.weather?.[0]?.description || "Đang tải...";

  // --- HIỂN THỊ TRẠNG THÁI LOADING/ERROR ---
  if (loading && !current) {
    // Hiển thị loading chỉ khi chưa có dữ liệu lần nào
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
        <div className="text-center text-gray-700 dark:text-gray-300">
          <span className="text-4xl animate-pulse">☁️</span>
          <p className="mt-4 text-lg">
            Đang tải dữ liệu thời tiết cho **{currentCity}**...
          </p>
        </div>
      </div>
    );
  }

  if (error && !current) {
    // Hiển thị error chỉ khi không có dữ liệu để hiển thị
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
        <div
          className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative max-w-lg mx-auto"
          role="alert"
        >
          <strong className="font-bold">Lỗi tải dữ liệu!</strong>
          <p className="block sm:inline mt-2">
            Không thể lấy dữ liệu: **{error}**
          </p>
          <p className="mt-1 text-sm">
            Vui lòng kiểm tra tên thành phố, hoặc đảm bảo Backend đang chạy tại
            `http://localhost:8080`.
          </p>
        </div>
      </div>
    );
  }
  // ----------------------------------------

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
                {/* Thêm phần chọn nguồn API */}
                <select
                  value={apiSource}
                  onChange={(e) => setApiSource(e.target.value)}
                  className="px-4 py-2 bg-gray-100 dark:bg-gray-700 text-gray-700 dark:text-gray-300 rounded-lg font-medium transition cursor-pointer"
                >
                  <option value={API_SOURCES.SPRING_BOOT_OPENMETEO}>
                    Nguồn: BE (OpenMeteo)
                  </option>
                </select>
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
                onKeyDown={handleSearch}
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
                  <h2 className="text-2xl font-bold mb-1">{locationName}</h2>
                  <p className="text-blue-100">{description}</p>
                </div>
                {/* HIỂN THỊ ICON THỰC TẾ DÙNG OpenWeatherMap ICON URL */}
                <span className="text-5xl">
                  {weatherData.weather?.[0]?.icon ? (
                    <img
                      src={`https://openweathermap.org/img/wn/${weatherData.weather[0].icon}@2x.png`}
                      alt={description}
                      className="w-16 h-16"
                    />
                  ) : (
                    "☁️"
                  )}
                </span>
              </div>

              <div className="text-6xl font-bold mb-8">{currentTemp}°C</div>

              <div className="grid grid-cols-2 gap-4">
                {/* Gió */}
                <div className="flex items-center space-x-3">
                  <div className="p-2 bg-white/20 rounded-lg">
                    <span className="text-xl">💨</span>
                  </div>
                  <div>
                    <p className="text-sm text-blue-100">Gió</p>
                    <p className="font-semibold">
                      {weatherData.wind?.speed
                        ? `${weatherData.wind.speed.toFixed(1)} m/s`
                        : "-"}
                    </p>
                  </div>
                </div>
                {/* Độ ẩm */}
                <div className="flex items-center space-x-3">
                  <div className="p-2 bg-white/20 rounded-lg">
                    <span className="text-xl">💧</span>
                  </div>
                  <div>
                    <p className="text-sm text-blue-100">Độ ẩm</p>
                    <p className="font-semibold">
                      {weatherData.main?.humidity
                        ? `${weatherData.main.humidity}%`
                        : "-"}
                    </p>
                  </div>
                </div>
                {/* Cảm giác */}
                <div className="flex items-center space-x-3">
                  <div className="p-2 bg-white/20 rounded-lg">
                    <span className="text-xl">👁️</span>
                  </div>
                  <div>
                    <p className="text-sm text-blue-100">Cảm giác</p>
                    <p className="font-semibold">
                      {weatherData.main?.feels_like
                        ? `${Math.round(weatherData.main.feels_like)}°C`
                        : "-"}
                    </p>
                  </div>
                </div>
                {/* Mây */}
                <div className="flex items-center space-x-3">
                  <div className="p-2 bg-white/20 rounded-lg">
                    <span className="text-xl">☁️</span>
                  </div>
                  <div>
                    <p className="text-sm text-blue-100">Mây</p>
                    <p className="font-semibold">
                      {weatherData.clouds?.all
                        ? `${weatherData.clouds.all}%`
                        : "-"}
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
                    📍{" "}
                    {weatherData.coord?.lat
                      ? `${weatherData.coord.lat.toFixed(4)}°`
                      : "-"}
                    ,{" "}
                    {weatherData.coord?.lon
                      ? `${weatherData.coord.lon.toFixed(4)}°`
                      : "-"}
                  </p>
                </div>
              </div>
            </div>
          </div>

          {/* Hourly Forecast */}
          <div className="bg-white dark:bg-gray-800 rounded-2xl p-6 shadow-lg border border-gray-200 dark:border-gray-700 mb-8">
            <h3 className="text-xl font-bold text-gray-900 dark:text-white mb-6">
              ⏰ Dự báo {forecastHourly.length} giờ tới
            </h3>
            <div className="overflow-x-auto -mx-6 px-6">
              <div className="flex space-x-4 min-w-max pb-2">
                {forecastHourly.map((hour, i) => {
                  const time = new Date(hour.dt * 1000);
                  const temp = Math.round(hour.main?.temp) || "-";
                  const descriptionHourly =
                    hour.weather?.[0]?.description || "N/A";
                  const icon = hour.weather?.[0]?.icon || "04d"; // Icon mặc định
                  const rainVolume = hour.rain?.["1h"]; // Lấy lượng mưa

                  return (
                    <div
                      key={i}
                      className="flex flex-col items-center space-y-2 min-w-[80px] p-3 hover:bg-gray-50 dark:hover:bg-gray-700 rounded-xl transition relative"
                    >
                      <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
                        {time.getHours()}:00
                      </p>
                      <span className="text-3xl">
                        <img
                          src={`https://openweathermap.org/img/wn/${icon}.png`}
                          alt={descriptionHourly}
                          className="w-8 h-8"
                        />
                      </span>
                      <p className="text-lg font-bold text-gray-900 dark:text-white">
                        {temp}°
                      </p>
                      {/* Thêm lượng mưa nếu có */}
                      {rainVolume && (
                        <div className="absolute -bottom-1 left-1/2 transform -translate-x-1/2 text-xs text-blue-500 font-semibold bg-blue-100 dark:bg-blue-900 rounded-full px-1.5">
                          {rainVolume}mm
                        </div>
                      )}
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
            {/* Truyền dữ liệu giờ thực tế */}
            <WeatherChart data={forecastHourly} />
          </div>

          {/* 7-day Forecast */}
          <div className="bg-white dark:bg-gray-800 rounded-2xl p-6 shadow-lg border border-gray-200 dark:border-gray-700">
            <h3 className="text-xl font-bold text-gray-900 dark:text-white mb-6">
              📅 Dự báo {forecast7Day.length} ngày tới
            </h3>
            <div className="space-y-3">
              {forecast7Day.map((day, i) => {
                const date = new Date(day.dt * 1000);
                const dayName = date.toLocaleDateString("vi-VN", {
                  weekday: "short",
                  day: "numeric",
                  month: "numeric",
                });
                const descriptionDay =
                  day.weather?.[0]?.description || "Đang cập nhật";
                const tempMin = Math.round(day.main?.temp_min || 0);
                const tempMax = Math.round(day.main?.temp_max || 0);
                const icon = day.weather?.[0]?.icon || "04d";

                return (
                  <div
                    key={i}
                    className="flex items-center justify-between p-4 hover:bg-gray-50 dark:hover:bg-gray-700 rounded-xl transition"
                  >
                    <p className="font-medium text-gray-900 dark:text-white w-32 capitalize">
                      {dayName}
                    </p>
                    <div className="flex items-center space-x-3 flex-1">
                      <span className="text-2xl">
                        <img
                          src={`https://openweathermap.org/img/wn/${icon}.png`}
                          alt={descriptionDay}
                          className="w-8 h-8"
                        />
                      </span>
                      <p className="text-sm text-gray-600 dark:text-gray-400">
                        {descriptionDay}
                      </p>
                    </div>
                    <div className="flex items-center space-x-4">
                      <span className="text-gray-600 dark:text-gray-400">
                        {tempMin}°
                      </span>
                      <span className="font-bold text-gray-900 dark:text-white">
                        {tempMax}°
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

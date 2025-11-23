// src/components/Sidebar.jsx
export default function Sidebar({ isOpen, toggleSidebar }) {
  const menu = [
    { icon: "⚙️", label: "Cài đặt" },
    { icon: "❤️", label: "Lưu vị trí" },
    { icon: "🕐", label: "Lịch sử tìm kiếm" },
    { icon: "⭐", label: "Vị trí yêu thích" },
    { icon: "ℹ️", label: "Thông tin ứng dụng" },
  ];

  return (
    <>
      {isOpen && (
        <div
          className="fixed inset-0 bg-black/60 z-40"
          onClick={toggleSidebar}
        />
      )}
      <aside
        className={`fixed top-0 left-0 h-full w-80 bg-white dark:bg-gray-800 shadow-xl z-50 transition-transform duration-300 ${
          isOpen ? "translate-x-0" : "-translate-x-full"
        }`}
      >
        <div className="p-6 flex flex-col h-full">
          <div className="flex justify-between items-center mb-8">
            <div className="flex items-center gap-4">
              <div className="text-4xl">☁️</div>
              <div>
                <h1 className="text-2xl font-bold text-blue-500 dark:text-cyan-400">
                  WeatherPro
                </h1>
                <p className="text-sm text-gray-500 dark:text-gray-400">v1.0</p>
              </div>
            </div>
            <button
              onClick={toggleSidebar}
              className="text-2xl hover:text-blue-500 dark:hover:text-cyan-400 transition"
            >
              ✕
            </button>
          </div>

          <h3 className="text-lg font-semibold text-blue-600 dark:text-cyan-400 mb-4 px-2">
            Các tính năng
          </h3>

          <nav className="flex-1 space-y-2">
            {menu.map((item, i) => (
              <button
                key={i}
                className="w-full flex items-center gap-5 px-5 py-4 rounded-xl hover:bg-gray-100 dark:hover:bg-gray-700 transition-all text-left text-gray-700 dark:text-gray-200"
              >
                <div className="text-2xl">{item.icon}</div>
                <span className="text-base font-medium">{item.label}</span>
              </button>
            ))}
          </nav>

          <button className="mt-8 py-4 bg-gradient-to-r from-blue-500 to-cyan-500 dark:from-cyan-500 dark:to-blue-600 rounded-xl font-bold text-white text-base shadow-lg hover:scale-105 transition">
            + Thêm vị trí mới
          </button>
        </div>
      </aside>
    </>
  );
}

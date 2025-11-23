// src/components/RegisterModal.jsx
export default function RegisterModal({ onClose, onSwitchToLogin }) {
  const handleGoogleRegister = () => {
    // Chưa có xử lý, chỉ log ra console
    console.log("Google register clicked");
  };

  return (
    <div
      className="fixed inset-0 bg-black/50 flex items-center justify-center z-[9999]"
      onClick={onClose}
    >
      {/* ↑ ĐÃ SỬA: z-50 → z-[9999] */}
      <div
        className="bg-white dark:bg-gray-800 rounded-2xl p-8 w-full max-w-md relative border border-gray-200 dark:border-gray-700"
        onClick={(e) => e.stopPropagation()}
      >
        <button
          onClick={onClose}
          className="absolute top-4 right-4 text-2xl text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200"
        >
          ✕
        </button>

        <h2 className="text-2xl font-bold mb-6 text-gray-900 dark:text-white">
          Đăng ký
        </h2>

        <form className="space-y-4">
          {/* Email */}
          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
              Email
            </label>
            <input
              type="email"
              placeholder="Nhập email của bạn"
              className="w-full px-4 py-3 rounded-lg border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-700 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500 outline-none"
            />
          </div>

          {/* Mật khẩu */}
          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
              Mật khẩu
            </label>
            <input
              type="password"
              placeholder="Ít nhất 6 ký tự"
              className="w-full px-4 py-3 rounded-lg border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-700 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500 outline-none"
            />
          </div>

          {/* Xác nhận mật khẩu */}
          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
              Xác nhận mật khẩu
            </label>
            <input
              type="password"
              placeholder="Nhập lại mật khẩu"
              className="w-full px-4 py-3 rounded-lg border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-700 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500 outline-none"
            />
          </div>

          {/* Nút đăng ký thường */}
          <button
            type="submit"
            className="w-full py-3 bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 text-white font-medium rounded-lg transition"
          >
            Đăng ký
          </button>

          {/* Nút đăng ký với Google */}
          <button
            type="button"
            onClick={handleGoogleRegister}
            className="w-full py-3 mt-2 flex items-center justify-center gap-2 border border-gray-300 dark:border-gray-600 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition"
          >
            <img
              src="https://www.svgrepo.com/show/475656/google-color.svg"
              alt="Google"
              className="w-5 h-5"
            />
            <span className="text-gray-700 dark:text-gray-300">
              Đăng ký với Google
            </span>
          </button>

          <p className="text-center text-gray-600 dark:text-gray-400">
            Đã có tài khoản?{" "}
            <span
              onClick={onSwitchToLogin}
              className="text-blue-500 hover:underline cursor-pointer"
            >
              Đăng nhập
            </span>
          </p>
        </form>
      </div>
    </div>
  );
}

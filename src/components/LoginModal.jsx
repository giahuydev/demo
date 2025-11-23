// src/components/LoginModal.jsx
export default function LoginModal({ onClose, onSwitchToRegister }) {
  const handleGoogleLogin = () => {
    // TODO: Thêm logic đăng nhập Google (Firebase, NextAuth, v.v.)
    console.log("Google login clicked");
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
          Đăng nhập
        </h2>

        <form className="space-y-4">
          {/* Email + Password */}
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

          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
              Mật khẩu
            </label>
            <input
              type="password"
              placeholder="Nhập mật khẩu"
              className="w-full px-4 py-3 rounded-lg border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-700 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500 outline-none"
            />
          </div>

          <button
            type="submit"
            className="w-full py-3 bg-blue-500 hover:bg-blue-600 text-white font-medium rounded-lg transition"
          >
            Đăng nhập
          </button>

          {/* Nút Google */}
          <button
            type="button"
            onClick={handleGoogleLogin}
            className="w-full py-3 mt-2 flex items-center justify-center gap-2 border border-gray-300 dark:border-gray-600 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition"
          >
            <img
              src="https://www.svgrepo.com/show/475656/google-color.svg"
              alt="Google"
              className="w-5 h-5"
            />
            <span className="text-gray-700 dark:text-gray-300">
              Đăng nhập với Google
            </span>
          </button>

          <p className="text-center text-gray-600 dark:text-gray-400">
            Chưa có tài khoản?{" "}
            <span
              onClick={onSwitchToRegister}
              className="text-blue-500 hover:underline cursor-pointer"
            >
              Đăng ký ngay
            </span>
          </p>
        </form>
      </div>
    </div>
  );
}

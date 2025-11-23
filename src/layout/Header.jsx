export default function Header({ onToggleSidebar }) {
  return (
    <header className="app-header">
      <div className="header-left">
        <button
          className="icon-btn sidebar-toggle-btn"
          onClick={onToggleSidebar}
        >
          ☰
        </button>

        <button className="icon-btn theme-toggle-btn">🌙</button>
      </div>

      <div className="header-right">
        <button className="auth-btn">Đăng nhập</button>
        <button className="auth-btn register-btn">Đăng ký</button>
      </div>
    </header>
  );
}

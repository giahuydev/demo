export default function Sidebar({ isOpen, onToggle }) {
  return (
    <aside className={`sidebar ${isOpen ? "open" : ""}`}>
      <button
        className="sidebar-close-btn"
        onClick={onToggle}
        aria-label="Close sidebar"
      >
        ✖
      </button>

      <div className="sidebar-content">
        <h3>Các tính năng</h3>
        <ul>
          <li>Cài đặt</li>
          <li>Lưu vị trí</li>
          <li>Lịch sử tìm kiếm</li>
        </ul>
      </div>
    </aside>
  );
}

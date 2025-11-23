// tailwind.config.js
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  darkMode: "class", // ← QUAN TRỌNG: Bắt buộc phải có để dark mode hoạt động
  theme: {
    extend: {},
  },
  plugins: [],
};

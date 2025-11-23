import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/identity": {
        target: "http://localhost:8080", // URL của Spring Boot
        changeOrigin: true,
        // Nếu Spring Boot không có prefix /identity, bỏ comment dòng này:
        // rewrite: (path) => path.replace(/^\/identity/, '')
      },
    },
  },
});

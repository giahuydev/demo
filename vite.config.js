// vite.config.js

import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      // Khi FE gọi /identity, Vite sẽ chuyển hướng đến http://localhost:8080
      "/identity": {
        target: "http://localhost:8080",
        changeOrigin: true,
      },
    },
  },
});

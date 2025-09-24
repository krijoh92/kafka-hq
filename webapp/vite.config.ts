import tailwindcss from "@tailwindcss/vite";
import react from "@vitejs/plugin-react-swc";
import path from "path";
import { defineConfig } from "vite";

// https://vite.dev/config/
export default defineConfig({
	plugins: [react(), tailwindcss()],
	resolve: {
		alias: {
			"@": path.resolve(__dirname, "./src"),
		},
	},
	server: {
		host: "0.0.0.0",
		port: 3000,
		proxy: {
			"/api": {
				target: "http://ace-backend:8080",
				changeOrigin: true,
				secure: false,
			},
		},
		allowedHosts: [
			"ace-web.wittyshizard.dev"
		]
	},
});

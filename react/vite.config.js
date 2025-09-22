import { defineConfig } from 'vite';  
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/book-api-controller': {
        target: 'http://localhost:8080/BookRestfulWithJSClient',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/book-api-controller/, '/book-api-controller')
      }
    }
  }
});

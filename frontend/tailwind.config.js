/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{vue,js}'],
  theme: {
    extend: {
      boxShadow: {
        glow: '0 24px 80px rgba(56, 189, 248, 0.18)',
        soft: '0 20px 45px rgba(15, 23, 42, 0.22)'
      },
      backdropBlur: {
        xl: '24px'
      }
    }
  },
  plugins: []
}

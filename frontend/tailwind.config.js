const generateColorClass = (variable) => {

  return `var(--${variable})`
}

const textColor = {
  font: generateColorClass('text-font'),
  main: generateColorClass('text-main'),
  back: generateColorClass('text-back'),
}

const backgroundColor = {
  main: generateColorClass('bg-main'),
  back: generateColorClass('bg-back'),
  panel: generateColorClass('bg-panel'),
  side: generateColorClass('bg-side'),
  first: generateColorClass('bg-first'),
  second: generateColorClass('bg-second'),
  third: generateColorClass('bg-third'),
  most: generateColorClass('bg-most'),
}

const borderColor = {
  label: generateColorClass('border-label'),
}

module.exports = {
  purge: ['./src/common/css/main.css', './src/**/*.{vue,js,ts,jsx,tsx}'],
  darkMode: false, // or 'media' or 'class'
  theme: {
    extend: {
      textColor,
      backgroundColor,
      borderColor,
    },
    fontFamily: {
      sans: ['Noto Sans KR','sans-serif'],
      Montserrat: ['Montserrat','sans-serif']
    },
  },
  variants: {
    extend: {},
  },
  plugins: [],
}

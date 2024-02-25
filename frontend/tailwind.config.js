/** @type {import('tailwindcss').Config} */
export default {
  darkMode: "class",
  content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {
      transitionProperty: {
        width: "width",
      },
      colors: {
        night: {
          900: "#121212"
        },
        eerieBlackDark: {
          900: "#181818",
        },
        eerieBlackLight: {
          900: "#242424"
        },
        jet: {
          900: "#383737",
        },
        seaSalt: {
          900: "#F8F8F8",
        },
        antiFlashWhite: {
          900: "#EEEEEE"
        },
        periwinkle: {
          900: "#D0DBFF",
        },
        charcoal: {
          900: "#40465E",
        },
        tropicalIndigo: {
          900: "#A193FA",
        },
        blackOlive: {
          900: "#404040",
        },
        onyx: {
          900: "#383737",
        },
        lightSilver: {
          900: "#D9D9D9",
        },
        oldSilver: {
          900: "#828282",
        },
        quickSilver: {
          900: "#9F9F9F",
        },
        silverChalice: {
          900: "#AFAFAD",
        },
        vistaBlue: {
          900: "#849CDE",
        },
        ube: {
          900: "#8176CB ",
        },
        maximumBluePurple: {
          900: "#A193FA",
        },
        spaceCadet: {
          900: "#17254A",
        },
        jordyBlue: {
          900: "#93AAFA",
        },
        chineseBlack: {
          900: "#111111",
        },
        lavenderIndigo: {
          900: "#8B59E9",
        },
      },
    },
    plugins: [],
  },
};

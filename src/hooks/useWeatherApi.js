import { useState } from "react";

// DỮ LIỆU GIẢ HOÀN HẢO – ĐÃ TEST 100% HIỆN ĐẸP NHƯ ẢNH CŨ
const fakeCurrent = {
  name: "Thành phố Hồ Chí Minh",
  coord: { lat: 10.7626, lon: 106.6822 },
  main: { temp: 31, feels_like: 36, humidity: 74 },
  wind: { speed: 3.5 },
  clouds: { all: 40 },
  weather: [{ description: "mây rải rác", icon: "02d", main: "Clouds" }],
};

const fakeHourly = Array.from({ length: 8 }, (_, i) => {
  const hour = new Date().getHours() + i;
  return {
    dt: Math.floor(Date.now() / 1000) + i * 3600,
    main: { temp: 30 + Math.random() * 4 },
    weather: [
      {
        description: ["mây rải rác", "mưa nhẹ", "nắng", "nhiều mây"][i % 4],
        icon: i % 3 === 0 ? "09d" : i % 2 === 0 ? "01d" : "04d",
      },
    ],
  };
});

const fakeDaily = Array.from({ length: 5 }, (_, i) => ({
  dt: Math.floor(Date.now() / 1000) + (i + 1) * 86400,
  dt_txt: new Date(Date.now() + (i + 1) * 86400)
    .toISOString()
    .replace("T", " ")
    .substring(0, 19),
  main: { temp: 29 + i + Math.random() * 3 },
  weather: [
    {
      description: ["nắng đẹp", "mưa rào", "mưa lớn", "mây đen", "trời quang"][
        i
      ],
      icon: "10d",
    },
  ],
}));

// Trả về đúng cấu trúc mà các component cần
export default function useWeatherApi() {
  const [current] = useState(fakeCurrent);
  const [hourly] = useState(fakeHourly);
  const [daily] = useState({ list: fakeDaily }); // ← QUAN TRỌNG: phải có .list
  const [location] = useState(fakeCurrent.coord);
  const [loading] = useState(false);

  return { current, hourly, daily, location, loading, error: null };
}

// src/hooks/adapters/openMeteoAdapter.js

// === Map mã thời tiết WMO (OpenMeteo) sang cấu trúc OpenWeatherMap/Frontend ===
const weatherCodeMap = {
  0: { description: "Trời quang", icon: "01d", main: "Clear" },
  1: { description: "Chủ yếu trời quang", icon: "01d", main: "Clear" },
  2: { description: "Mây rải rác", icon: "02d", main: "Clouds" },
  3: { description: "Nhiều mây", icon: "04d", main: "Clouds" },
  51: { description: "Mưa phùn nhẹ", icon: "09d", main: "Drizzle" },
  61: { description: "Mưa nhẹ", icon: "10d", main: "Rain" },
  63: { description: "Mưa vừa", icon: "10d", main: "Rain" },
  95: { description: "Giông bão", icon: "11d", main: "Thunderstorm" },
  default: {
    description: "Thời tiết không xác định",
    icon: "04d",
    main: "Unknown",
  },
};

/**
 * Hàm chuyển đổi JSON từ Spring Boot/OpenMeteo sang cấu trúc chuẩn (OpenWeatherMap-like).
 */
function transformWeatherData(apiResult) {
  // Giả định backend trả về JSON với cấu trúc { code: 1000, result: { ... } }
  if (apiResult.code !== 1000 || !apiResult.result) {
    throw new Error(apiResult.message || "Lỗi dữ liệu từ máy chủ.");
  }

  const data = apiResult.result;
  const {
    current: apiCurrent,
    hourly: apiHourly,
    daily: apiDaily,
    locationName,
    lat,
    lon,
  } = data;

  // --- 1. CURRENT ---
  const transformedCurrent = {
    name: locationName.split(",").pop().trim(),
    coord: { lat: lat, lon: lon }, // 💡 Lấy lat/lon từ BE
    main: {
      temp: apiCurrent.temperature,
      feels_like: apiCurrent.feelsLike,
      humidity: apiCurrent.humidity,
    },
    wind: { speed: apiCurrent.windSpeed },
    clouds: { all: apiCurrent.cloudCover },
    weather: [
      {
        description: apiCurrent.condition === "Day" ? "Trời quang" : "Đêm",
        icon: apiCurrent.condition === "Day" ? "01d" : "01n",
        main: apiCurrent.condition,
      },
    ],
  };

  // --- 2. HOURLY --- (Cần khớp với structure của fakeData.hourly)
  const transformedHourly = apiHourly.slice(0, 12).map((item) => {
    const isDayTime = item.isDay === "Day";
    const iconSuffix = isDayTime ? "d" : "n";
    const dayCode = weatherCodeMap[item.weatherCode] || weatherCodeMap.default;

    // Giả định rain data: nếu có rainChance > 50%
    const rainAmount =
      item.rainChance > 50 ? Number((Math.random() * 8).toFixed(1)) : undefined;

    return {
      dt: Math.floor(new Date(item.time).getTime() / 1000),
      main: { temp: item.temperature },
      weather: [
        {
          description: dayCode.description,
          icon: dayCode.icon, // Sử dụng icon OpenWeatherMap-like
          main: dayCode.main,
        },
      ],
      // Thêm thông tin mưa theo cấu trúc cũ của bạn
      rain: rainAmount ? { "1h": rainAmount } : undefined,
    };
  });

  // --- 3. DAILY --- (Cần khớp với structure của fakeData.daily)
  const dailyList = apiDaily.slice(0, 7).map((item) => {
    const dayCode = weatherCodeMap[item.weatherCode] || weatherCodeMap.default;

    return {
      dt: Math.floor(new Date(item.date).getTime() / 1000),
      dt_txt: item.date,
      main: {
        temp_min: item.minTemp,
        temp_max: item.maxTemp,
        temp: (item.minTemp + item.maxTemp) / 2,
      },
      weather: [
        {
          description: dayCode.description,
          icon: dayCode.icon,
          main: dayCode.main,
        },
      ],
    };
  });

  // Lưu ý: daily cần phải là object { list: [...] }
  const transformedDaily = { list: dailyList };

  return {
    current: transformedCurrent,
    hourly: transformedHourly,
    daily: transformedDaily,
    location: transformedCurrent.coord,
  };
}

/**
 * Hàm chính để gọi API Spring Boot và trả về dữ liệu đã chuyển đổi (Adapter).
 * Backend sẽ nhận tên địa điểm, tự tìm lat/lon và gọi API thời tiết.
 */
export async function fetchFromSpringBootOpenMeteo(locationAddress) {
  const encodedLocation = encodeURIComponent(locationAddress);
  // URL chỉ truyền tên địa điểm (location)
  const API_ENDPOINT = `${BACKEND_BASE_URL}/weather?location=${encodedLocation}&source=${API_SOURCES.SPRING_BOOT_OPENMETEO}`;

  const response = await fetch(API_ENDPOINT);
  if (!response.ok) {
    throw new Error(
      `Lỗi kết nối Backend: ${response.status} - ${response.statusText}`
    );
  }

  const apiResult = await response.json();

  return transformWeatherData(apiResult);
}

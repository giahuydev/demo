// src/hooks/useWeatherApi.js

import { useState, useEffect } from "react";
import { API_SOURCES } from "../constants"; // Import từ constants/index.js
import { fetchFromSpringBootOpenMeteo } from "./adapters/openMeteoAdapter"; // Import Adapter

// Map nguồn API đến hàm gọi tương ứng (Adapter)
const API_ADAPTERS = {
  [API_SOURCES.SPRING_BOOT_OPENMETEO]: fetchFromSpringBootOpenMeteo,
  // Thêm các nguồn khác (nếu có)
};

export default function useWeatherApi(
  locationAddress = "Thành phố Hồ Chí Minh",
  source = API_SOURCES.SPRING_BOOT_OPENMETEO
) {
  // Dữ liệu ban đầu là null/empty array, sẽ được set khi gọi API thành công
  const [current, setCurrent] = useState(null);
  const [hourly, setHourly] = useState([]);
  const [daily, setDaily] = useState({ list: [] });
  const [location, setLocation] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!locationAddress || !API_ADAPTERS[source]) {
      setLoading(false);
      setError("Nguồn API không hợp lệ hoặc thiếu địa chỉ.");
      return;
    }

    const selectedFetcher = API_ADAPTERS[source];

    const fetchData = async () => {
      setLoading(true);
      setError(null);

      try {
        // Gọi Adapter đã chọn, truyền tên địa điểm
        const transformedData = await selectedFetcher(locationAddress);

        setCurrent(transformedData.current);
        setHourly(transformedData.hourly);
        setDaily(transformedData.daily);
        setLocation(transformedData.location);
      } catch (err) {
        console.error(`Lỗi từ nguồn ${source}:`, err);
        // Hiển thị lỗi rõ ràng hơn
        setError(
          `Lỗi khi tải dữ liệu cho "${locationAddress}": ${err.message}`
        );
        setCurrent(null);
        setHourly([]);
        setDaily({ list: [] });
        setLocation(null);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [locationAddress, source]); // Re-run khi địa chỉ hoặc nguồn thay đổi

  return { current, hourly, daily, location, loading, error };
}

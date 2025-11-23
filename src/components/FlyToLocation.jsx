// FlyToLocation.jsx
import { useEffect } from "react";
import { useMap } from "react-leaflet";

export default function FlyToLocation({ position }) {
  const map = useMap();
  useEffect(() => {
    if (position)
      map.flyTo([position.lat, position.lon], 12, { duration: 1.5 });
  }, [position, map]);
  return null;
}

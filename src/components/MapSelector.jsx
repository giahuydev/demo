// MapSelector.jsx
import React from "react";
import { MapContainer, TileLayer, Marker, useMapEvents } from "react-leaflet";
import FlyToLocation from "./FlyToLocation";
import "leaflet/dist/leaflet.css";
import L from "leaflet";

delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl:
    "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png",
  iconUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png",
  shadowUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png",
});

export default function MapSelector({ position }) {
  const center = position ? [position.lat, position.lon] : [21.0285, 105.8542];

  return (
    <MapContainer
      center={center}
      zoom={12}
      style={{ height: "100%", borderRadius: "24px" }}
    >
      <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
      {position && <Marker position={[position.lat, position.lon]} />}
      {position && <FlyToLocation position={position} />}
    </MapContainer>
  );
}

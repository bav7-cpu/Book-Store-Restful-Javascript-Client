import React from "react";

// users can pick their desired data format of JSON, XML or text
export default function FormatSelector({ format, setFormat }) {
	// dropdown menu is renders to select the data format
  return (
    <div className="mb-3">
      <label className="form-label">Select Data Format:</label>
      <select
        className="form-select"
        value={format}
        onChange={(e) => setFormat(e.target.value)}
      >
        <option value="application/json">JSON</option>
        <option value="application/xml">XML</option>
        <option value="text/plain">Text</option>
      </select>
    </div>
  );
}

package com.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.graph.util.geom.GeometryUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author wuyf 实体化ESRI Json对象 
 * Utility for ESRI JSON object
 * 
 */
public class EsriJsonUtil {
	/**
	 *  JSON对象转Geometry
	 * @param json
	 *             ESRI JSON geometry string
	 * @return Geometry
	 * @throws Exception 
	 * @throws JSONException
	 */
	static public Geometry json2Geometry(String json) throws Exception  {

		Geometry geo = null;
		try {
			if (json != null && !"".equals(json.trim())) {
				JSONObject obj = new JSONObject(json);

				if (!obj.isNull("geometry")) {
					obj = obj.getJSONObject("geometry");
				}

				// Point
				if (!obj.isNull("x") && !obj.isNull("y")) {
					double x = obj.getDouble("x");
					double y = obj.getDouble("y");
					geo = GeometryUtil.gf().createPoint(new Coordinate(x, y));
				}

				// Polyline
				else if (!obj.isNull("paths")) {
					JSONArray paths = obj.getJSONArray("paths");

					int lsCount = paths.length();
					LineString[] lns = new LineString[lsCount];
					for (int i = 0; i < lsCount; i++) {
						JSONArray path = paths.getJSONArray(i);

						int ptCount = path.length();
						Coordinate[] pts = new Coordinate[ptCount];
						for (int j = 0; j < ptCount; j++) {
							JSONArray pt = path.getJSONArray(j);
							double x = pt.getDouble(0);
							double y = pt.getDouble(1);
							pts[j] = new Coordinate(x, y);
						}
						lns[i] = GeometryUtil.gf().createLineString(pts);
					}

					if (lns.length == 1) {
						geo = GeometryUtil.gf().createLineString(
								lns[0].getCoordinates());
					} else {
						geo = GeometryUtil.gf().createMultiLineString(lns);
					}
				}

				// Polygon
				else if (!obj.isNull("rings")) {
					JSONArray rings = obj.getJSONArray("rings");

					int lrCount = rings.length();
					LinearRing shell = null;
					LinearRing[] holes = new LinearRing[lrCount - 1];
					for (int i = 0; i < lrCount; i++) {
						JSONArray ring = rings.getJSONArray(i);

						int ptCount = ring.length();
						Coordinate[] pts = new Coordinate[ptCount];
						for (int j = 0; j < ptCount; j++) {
							JSONArray pt = ring.getJSONArray(j);
							double x = pt.getDouble(0);
							double y = pt.getDouble(1);
							pts[j] = new Coordinate(x, y);
						}
						LinearRing lr = GeometryUtil.gf().createLinearRing(pts);
						if (i == 0) {
							shell = lr;
						} else {
							holes[i - 1] = lr;
						}
					}
					geo = GeometryUtil.gf().createPolygon(shell, holes);
				}

				// MultiPoint
				else if (!obj.isNull("points")) {
					JSONArray points = obj.getJSONArray("points");

					int ptCount = points.length();
					Coordinate[] pts = new Coordinate[ptCount];
					for (int i = 0; i < ptCount; i++) {
						JSONArray pt = points.getJSONArray(i);
						double x = pt.getDouble(0);
						double y = pt.getDouble(1);
						pts[i] = new Coordinate(x, y);
					}
					geo = GeometryUtil.gf().createMultiPoint(pts);
				}

				// Envelope
				else if (!obj.isNull("xmin") && !obj.isNull("ymin")
						&& !obj.isNull("xmax") && !obj.isNull("ymax")) {
					double xmin = obj.getDouble("xmin");
					double ymin = obj.getDouble("ymin");
					double xmax = obj.getDouble("xmax");
					double ymax = obj.getDouble("ymax");
					Coordinate[] coordinates = new Coordinate[5];
					coordinates[0] = new Coordinate(xmin, ymin);
					coordinates[1] = new Coordinate(xmin, ymax);
					coordinates[2] = new Coordinate(xmax, ymax);
					coordinates[3] = new Coordinate(xmax, ymin);
					coordinates[4] = new Coordinate(xmin, ymin);
					LinearRing shell = GeometryUtil.gf().createLinearRing(
							coordinates);
					geo = GeometryUtil.gf().createPolygon(shell, null);
				}

				// SpatialReference
				int wkid = EsriJsonUtil.getSpatialReference(obj);
				if (wkid > 0) {
					geo.setSRID(wkid);
				}
			}

		} catch (Exception e) {
			throw new Exception("ESRI Geometry JSON Format is not correct", e);
		}

		return geo;
	}

	/**
	 * @param geometries
	 *            JSON对象转为Geometry集合 ESRI JSON geometries string
	 * @return Geometry List
	 * @throws Exception 
	 */
	static public List<Geometry> json2Geometries(String geometries)
			throws Exception {
		List<Geometry> geometriesList =null;
		try {
			if (geometries == null) {
				return null;
			}

			geometriesList = new ArrayList<Geometry>();
			GeometryFactory geoFactory = new GeometryFactory();
			Geometry geo = null;

			if (!geometries.startsWith("{")) {

				if (geometries.startsWith("[")) {
					// Simple array like "[......]"
					JSONArray geos = new JSONArray(geometries);
					for (int i = 0; i < geos.length(); i++) {
						geo = json2Geometry(geos.getString(i));
						geometriesList.add(geo);
					}
				} else {
					// Simple points like "-104.53, 34.74, -63.53, 10.23"
					String[] strs = geometries.split(",");
					for (int i = 0; i < strs.length / 2; i++) {
						double x = Double.parseDouble(strs[2 * i]);
						double y = Double.parseDouble(strs[2 * i + 1]);
						geo = geoFactory.createPoint(new Coordinate(x, y));
						geometriesList.add(geo);
					}
				}
			} else {
				JSONObject obj = new JSONObject(geometries);
				JSONArray geos = null;
				if (!obj.isNull("url")) {
					// TO DO
					// URL based geometries like
					// ""http://myserver/mygeometries/afile.txt""
				}
				if (!obj.isNull("geometries")) {
					// Normal ESRI JSON geometries
					geos = obj.getJSONArray("geometries");
					for (int i = 0; i < geos.length(); i++) {
						geo = json2Geometry(geos.getString(i));
						geometriesList.add(geo);
					}
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}

		return geometriesList;
	}

	/**
	 * Geometry转为Json
	 * 
	 * @param geometry
	 * @return
	 * @throws Exception 
	 */
	static public JSONObject geometry2JSON(Geometry geometry)
			throws Exception {
		JSONObject obj = new JSONObject();
		try {
			if (geometry == null) {
				return null;
			}
			JSONArray arrayTemp = null;
			JSONArray arrayTemp2 = null;

			// Point
			if (geometry.getClass().equals(Point.class)) {
				Point pt = (Point) geometry;
				obj.put("x", pt.getX());
				obj.put("y", pt.getY());
			}

			// LineString
			else if (geometry.getClass().equals(LineString.class)) {
				LineString ls = (LineString) geometry;
				Coordinate[] coords = ls.getCoordinates();
				arrayTemp = new JSONArray();
				for (int i = 0, count = coords.length; i < count; i++) {
					Coordinate coord = coords[i];
					arrayTemp2 = new JSONArray();
					arrayTemp2.put(coord.x);
					arrayTemp2.put(coord.y);
					arrayTemp.put(arrayTemp2);
				}
				arrayTemp2 = new JSONArray();
				arrayTemp2.put(arrayTemp);
				obj.put("paths", arrayTemp2);
			}

			// Polygon
			else if (geometry.getClass().equals(Polygon.class)) {
				Polygon pg = (Polygon) geometry;
				Coordinate[] coords = pg.getExteriorRing().getCoordinates();
				arrayTemp = new JSONArray();
				for (int i = 0, count = coords.length; i < count; i++) {
					Coordinate coord = coords[i];
					arrayTemp2 = new JSONArray();
					arrayTemp2.put(coord.x);
					arrayTemp2.put(coord.y);
					arrayTemp.put(arrayTemp2);
				}
				arrayTemp2 = new JSONArray();
				arrayTemp2.put(arrayTemp);
				obj.put("rings", arrayTemp2);
			}

			// MultiPoint
			else if (geometry.getClass().equals(MultiPoint.class)) {
				MultiPoint mpt = (MultiPoint) geometry;
				arrayTemp = new JSONArray();
				for (int i = 0, count = mpt.getNumGeometries(); i < count; i++) {
					Geometry geo = mpt.getGeometryN(i);
					if (geo.getClass().equals(Point.class)) {
						Point pt = (Point) geo;
						arrayTemp2 = new JSONArray();
						arrayTemp2.put(pt.getX());
						arrayTemp2.put(pt.getY());
						arrayTemp.put(arrayTemp2);
					}
				}
				obj.put("points", arrayTemp);
			}

			// MultiLineString
			else if (geometry.getClass().equals(MultiLineString.class)) {
				MultiLineString mls = (MultiLineString) geometry;
				arrayTemp2 = new JSONArray();
				for (int i = 0, count = mls.getNumGeometries(); i < count; i++) {
					LineString ls = (LineString) mls.getGeometryN(i);
					Coordinate[] coords = ls.getCoordinates();
					arrayTemp = new JSONArray();
					for (int j = 0, count2 = coords.length; j < count2; j++) {
						Coordinate coord = coords[j];
						JSONArray arrayPt = new JSONArray();
						arrayPt.put(coord.x);
						arrayPt.put(coord.y);
						arrayTemp.put(arrayPt);
					}
					arrayTemp2.put(arrayTemp);
				}
				obj.put("paths", arrayTemp2);
			}

			// MultiPolygon
			else if (geometry.getClass().equals(MultiPolygon.class)) {
				MultiPolygon mpg = (MultiPolygon) geometry;
				for (int i = 0, count = mpg.getNumGeometries(); i < count; i++) {
					Geometry geo = mpg.getGeometryN(i);
					if (geo.getClass().equals(Polygon.class)) {
						Polygon pg = (Polygon) geo;
						// TODO
						// If ESRI support multipolygon with hole?
						// ExteriorRing
						Coordinate[] coords = pg.getExteriorRing()
								.getCoordinates();
						arrayTemp = new JSONArray();
						for (int j = 0; j < coords.length; j++) {
							Coordinate coord = coords[j];
							JSONArray arrayPt = new JSONArray();
							arrayPt.put(coord.x);
							arrayPt.put(coord.y);
							arrayTemp.put(arrayPt);
						}
						obj.append("rings", arrayTemp);

						// InerRing
						for (int n = 0; n < pg.getNumInteriorRing(); n++) {
							coords = pg.getInteriorRingN(n).getCoordinates();
							arrayTemp = new JSONArray();
							for (int j = 0; j < coords.length; j++) {
								Coordinate coord = coords[j];
								JSONArray arrayPt = new JSONArray();
								arrayPt.put(coord.x);
								arrayPt.put(coord.y);
								arrayTemp.put(arrayPt);
							}
							obj.append("rings", arrayTemp);
						}
					}
				}
			}

			// SpatialReference
			if (geometry.getSRID() > 0) {
				EsriJsonUtil.appendSpatialReference(obj, geometry.getSRID());
			}

		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}

		return obj;
	}

	/**
	 * 几何类型转为Arcgis集合类型字符串
	 * 
	 * @param type
	 * @return "esriGeometryPoint"/"esriGeometryMultipoint"/...
	 * @throws JSONException
	 */
	static public String geometryType2String(Class<?> type)
			throws JSONException {
		String result = null;

		if (type.equals(Point.class)) {
			result = "esriGeometryPoint";
		} else if (type.equals(MultiPoint.class)) {
			result = "esriGeometryMultipoint";
		} else if (type.equals(LineString.class)
				|| type.equals(MultiLineString.class)) {
			result = "esriGeometryPolyline";
		} else if (type.equals(Polygon.class)
				|| type.equals(MultiPolygon.class)) {
			result = "esriGeometryPolygon";
		} else if (type.equals(Envelope.class)) {
			result = "esriGeometryEnvelope";
		}

		return result;
	}

	/**
	 * JSON对象中获取空间参考wkid
	 * 
	 * @param obj
	 *            JSON对象
	 * @return wkid
	 * @throws JSONException
	 */
	public static int getSpatialReference(JSONObject obj) throws JSONException {
		int wkid = 0;

		if (!obj.isNull("spatialReference")) {
			wkid = obj.getJSONObject("spatialReference").getInt("wkid");
		}

		return wkid;
	}

	/**
	 * JSON对象附加空间参考wkid
	 * 
	 * @param obj
	 *            要附加的JSON对象
	 * @param wkid
	 *            要附加的空间参考wkid
	 * @throws JSONException
	 */
	public static void appendSpatialReference(JSONObject obj, int wkid)
			throws JSONException {
		JSONObject objSR = new JSONObject();
		objSR.append("wkid", wkid);
		obj.append("spatialReference", objSR);
	}

	/**
	 * Feature 转为JSON对象
	 * 
	 * @param feature
	 * @param isReturnGeometry
	 *            如果为true，JSON对象包括“geometry”与“geometryType”项
	 * @return
	 * @throws Exception 
	 */
	public static JSONObject feature2JSON(Feature feature,
			boolean isReturnGeometry) throws Exception {
		JSONObject result = new JSONObject();
		
		try {
			// feature是SimpleFeature
			if (isReturnGeometry && feature instanceof SimpleFeature) {
				Geometry geo = (Geometry) feature.getDefaultGeometryProperty()
						.getValue();
				result.put("geometry", geometry2JSON(geo));
				result.put("geometryType", geometryType2String(geo.getClass()));
			}

			JSONObject objAttributes = new JSONObject();
			// 几何字段
			String geometryFieldName = feature.getDefaultGeometryProperty()
					.getName().getLocalPart();
			// 所有字段
			for (Iterator<Property> itr = feature.getProperties().iterator(); itr
					.hasNext();) {
				Property property = itr.next();
				String name = property.getDescriptor().getName().getLocalPart();
				// 非几何字段属性
				if (!geometryFieldName.equals(name)) {
					Object value = property.getValue();
					objAttributes.put(name, value);
				}
			}
			result.put("attributes", objAttributes);
			result.put("id", feature.getIdentifier().getID().substring(feature.getIdentifier().getID().lastIndexOf(".") + 1));
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
		
		return result;
	}

	/**
	 * JSONObject转为SimpleFeature
	 * 
	 * @param objFeature
	 * @param featureBuilder
	 * @return
	 * @throws Exception 
	 */
	private static SimpleFeature jsonObject2Feature(JSONObject objFeature,
			SimpleFeatureBuilder featureBuilder,String fID) throws Exception {
		
		SimpleFeature featureResult =null;
		try {
			featureResult = featureBuilder.buildFeature(null);

			int fid = -1;
			if (objFeature.has("attributes")) {
				if (objFeature.getJSONObject("attributes").has("objectid")) {
					fid = Integer.parseInt(objFeature
							.getJSONObject("attributes").getString("objectid")
							.toString());
				} else if (objFeature.getJSONObject("attributes").has(
						"OBJECTID")) {
					fid = Integer.parseInt(objFeature
							.getJSONObject("attributes").getString("OBJECTID")
							.toString());
				}
			}
			if (fid != -1) {
				featureResult = featureBuilder.buildFeature(fID + "." + fid);
			}
			// Feature的 Geometry值
			String strGeometry = objFeature.getString("geometry");
			Geometry geometry = json2Geometry(strGeometry);
			featureResult.setDefaultGeometry(geometry);

			// Feature的 Attributes值
			if (objFeature.has("attributes")) {
				JSONObject objAttributes = objFeature
						.getJSONObject("attributes");
				Iterator<?> itKeys = objAttributes.keys();
				while (itKeys.hasNext()) {
					String key = itKeys.next().toString();
					if (key.equalsIgnoreCase("objectid")) {
						continue;
					}
					Object attribute = objAttributes.get(key);
					featureResult.setAttribute(key, attribute);
				}
			}

		} catch (Exception e) {
			throw new Exception("JSON object builder feature error", e);
		}

		return featureResult;
	}

	/**
	 * JSON转feature
	 * 
	 * @param feature
	 * @param featureType
	 *            指定输出feature类型
	 * @return
	 * @throws Exception 
	 */
	public static SimpleFeature json2Feature(String feature,
			SimpleFeatureType featureType) throws Exception {
		SimpleFeature result = null;
		try {
			JSONObject objFeature = new JSONObject(feature);
			SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(
					featureType);

			result = jsonObject2Feature(objFeature, featureBuilder, null);

		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}

		return result;
	}

	/**
	 * JSON转FeatureCollection
	 * 
	 * @param features
	 * @param featureType
	 *            指定feature类型
	 * @return
	 * @throws Exception 
	 */
	public static FeatureCollection<SimpleFeatureType, SimpleFeature> json2FeatureCollection(
			String features, SimpleFeatureType featureType,String fID)
			throws Exception {
		DefaultFeatureCollection result = null;
		try {
			if (features != null && !"".equals(features)) {
				result = new DefaultFeatureCollection();
				JSONArray arrayFeatures = new JSONArray(features);

				SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(
						featureType);

				for (int i = 0, count = arrayFeatures.length(); i < count; i++) {
					JSONObject objFeature = arrayFeatures.getJSONObject(i);
					SimpleFeature featureResult = jsonObject2Feature(
							objFeature, featureBuilder, fID);
					result.add(featureResult);
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
		return result;
	}

}

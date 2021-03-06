package com.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.ogr;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.PrjFileReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author Jason Wong
 * @title: Util
 * @projectName covsx
 * @description: TODO
 * @date 2020/2/22 15:43
 */
@Component
public class GeojsonUtil {

    public static CoordinateReferenceSystem crsTarget;

    static {
        try {
            crsTarget = CRS.decode("EPSG:4490", true);
        } catch (FactoryException e) {
            e.printStackTrace();
        }
    }


    public static String[] generateObjAttr(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] title = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            title[i] = fields[i].getName();
        }
        return title;
    }

    private final static String nameFrefix = "set";

    public static Object mapConvertBean(Map<String, Object> map, Object obj) {
        /*
         * Class????????????????????? ??????????????????Class????????????????????? 1.?????????????????????String.class
         * 2.?????????getClass??????????????????new String().getClass().
         * 3.forName??????????????????Class.forName("java.lang.String") ?????????????????? ??????????????????
         * ?????????????????????Object??????,?????????????????????2???
         */
        Class classz = obj.getClass();
        // ????????????????????????????????????(getXxx setXxx ...)
        // Method[] declaredMethods = classz.getDeclaredMethods();

        // ??????map?????????????????????null
        if (!map.isEmpty()) {
            for (Map.Entry<String, Object> keyValue : map.entrySet()) {
                // ??????map??????
                String propertyName = keyValue.getKey();
                // ??????map-value???
                Object value = keyValue.getValue();
                // ??????????????????
                Field field = getClassField(classz, propertyName);

                if (field != null) {
                    // ??????????????????
                    Class<?> fieldType = field.getType();
                    if (value != null)
                        value = convertValType(value, fieldType);
                    Method method = null;
                    try {
                        // ????????????set?????????
                        String setMethodName = convertKey(propertyName);
                        //????????????
                        method = classz.getMethod(setMethodName, field.getType());
                        //????????????????????????????????????????????????
                        if (!method.isAccessible()) {
                            method.setAccessible(true);
                        }
                        method.invoke(obj, value);
                    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }

        }
        return obj;
    }

    /**
     * ???????????????map?????????key ?????? ????????? xXxx(tNode)?????? Eclipse????????????get set???????????????????????????????????????
     *
     * @return
     */
    public static String convertKey(String propertyName) {
        // ???????????????????????????????????????????????????
        String setMethodName = nameFrefix.concat(propertyName.substring(0, 1).toUpperCase().concat(propertyName.substring(1)));
        return setMethodName;
    }

    /**
     * ???????????????
     *
     * @param clazz     ???
     * @param fieldName ?????????
     * @return
     */
    private static Field getClassField(Class<?> clazz, String fieldName) {
        // ????????????Object???????????????null??????return
        if (clazz == null || Object.class.getName().equals(clazz.getName())) {
            return null;
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {// ?????????????????????
            return getClassField(superClass, fieldName);
        }
        return null;
    }

    /**
     * ???Object????????????????????????bean?????????????????????????????????
     *
     * @param value     Object?????????
     * @param fieldType ???????????????
     * @return ???????????????
     */
    private static Object convertValType(Object value, Class<?> fieldType) {
        Object retVal = null;
        if (Long.class.getName().equals(fieldType.getName()) || long.class.getName().equals(fieldType.getName())) {
            retVal = Long.parseLong(value.toString());
        } else if (Integer.class.getName().equals(fieldType.getName())
                || int.class.getName().equals(fieldType.getName())) {
            retVal = Integer.parseInt(value.toString());
        } else if (Float.class.getName().equals(fieldType.getName())
                || float.class.getName().equals(fieldType.getName())) {
            retVal = Float.parseFloat(value.toString());
        } else if (Double.class.getName().equals(fieldType.getName())
                || double.class.getName().equals(fieldType.getName())) {
            retVal = Double.parseDouble(value.toString());
        } else if (Boolean.class.getName().equals(fieldType.getName())
                || boolean.class.getName().equals(fieldType.getName())) {
            retVal = Boolean.parseBoolean(value.toString());
        } else if (Character.class.getName().equals(fieldType.getName())
                || char.class.getName().equals(fieldType.getName())) {
            retVal = value;
        } else if (Date.class.getName().equals(fieldType.getName())) {
            retVal = strConvertDate(value.toString(), "yyyy-MM-dd");
        } else if (String.class.getName().equals(fieldType.getName())) {
            retVal = value;
        } else if (Timestamp.class.getName().equals(fieldType.getName())) {
            retVal = strConvertDate(value.toString(), "yyyy-MM-dd HH:mm:ss");
        }
        return retVal;
    }


    /**
     * String?????????Date
     *
     * @param dateStr
     * @return
     */
    public static Date strConvertDate(String dateStr, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date parse = null;
        try {
            parse = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    public static void main(String[] args) throws NoSuchMethodException, SecurityException {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ttInteger", 123456);
        hashMap.put("ttDouble", 1.1);
        hashMap.put("ttFloat", 1.2);
        hashMap.put("ttChar", new Character('???'));
        hashMap.put("ttBoolean", true);
        hashMap.put("ttLong", 1221121221221L);
        hashMap.put("ttString", "Hello World");
        hashMap.put("ttDate", "2018-08-27");

        if (!hashMap.isEmpty()) {
//            Info info = new Info();
//            Object mapConvertBean = mapConvertBean(hashMap,info);
//            System.out.println(mapConvertBean.toString());
        }
    }

    /**
     * Map????????????
     *
     * @param map    ???????????????????????????key??????????????????????????????????????????????????????????????????
     * @param entity ???????????????????????????
     * @return
     */
    public static <T> T mapToEntity(Map<String, Object> map, Class<T> entity) {
        T t = null;
        try {
            t = entity.newInstance();
            for (Field field : entity.getDeclaredFields()) {
                if (map.containsKey(field.getName())) {
                    boolean flag = field.isAccessible();
                    field.setAccessible(true);
                    Object object = map.get(field.getName());
                    if (object != null && field.getType().isAssignableFrom(object.getClass())) {
                        field.set(t, object);
                    }
                    field.setAccessible(flag);
                }
            }
            return t;
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return t;
    }

    /**
     * ????????????
     *
     * @param file      ????????????
     * @param targetDir ???????????????????????????
     * @throws IOException
     */
    public static void unPacket(Path file, Path targetDir) throws IOException {
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }
        // ??????zip??????
        ZipFile zipFile = new ZipFile(file.toFile());
        try {
            // ??????zip???
            try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(file))) {
                ZipEntry zipEntry = null;
                // ???????????????zip???
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    // ??????zip????????????
                    String entryName = zipEntry.getName();
                    // ??????????????????
                    Path entryFile = targetDir.resolve(entryName);
                    if (zipEntry.isDirectory()) {    // ?????????
                        if (!Files.isDirectory(entryFile)) {
                            Files.createDirectories(entryFile);
                        }
                    } else {                            // ??????
                        // ??????zip????????????
                        try (InputStream zipEntryInputStream = zipFile.getInputStream(zipEntry)) {
                            try (OutputStream fileOutputStream = Files.newOutputStream(entryFile, StandardOpenOption.CREATE_NEW)) {
                                byte[] buffer = new byte[4096];
                                int length = 0;
                                while ((length = zipEntryInputStream.read(buffer)) != -1) {
                                    fileOutputStream.write(buffer, 0, length);
                                }
                                fileOutputStream.flush();
                            }
                        }
                    }
                }
            }
        } finally {
            zipFile.close();
        }
    }

    /**
     * ?????????????????????
     *
     * @param files   ????????????
     * @param zipFile ?????????????????????
     * @throws IOException
     */
    public static void packet(Path[] files, Path zipFile) throws IOException {
        OutputStream outputStream = Files.newOutputStream(zipFile, StandardOpenOption.CREATE_NEW);
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        try {
            for (Path file : files) {
                if (Files.isDirectory(file)) {
                    continue;
                }
                try (InputStream inputStream = Files.newInputStream(file)) {
                    // ????????????????????????????????????
                    ZipEntry zipEntry = new ZipEntry(file.getFileName().toString());
                    // ??????????????????
                    zipOutputStream.putNextEntry(zipEntry);
                    // ????????????
                    int len = 0;
                    byte[] buffer = new byte[1024 * 10];
                    while ((len = inputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, len);
                    }
                    zipOutputStream.flush();
                }
            }
            // ??????????????????????????????
            zipOutputStream.closeEntry();
        } finally {
            zipOutputStream.close();
            outputStream.close();
        }
    }

    /**
     * ?????????????????????
     *
     * @param folder
     * @param zipFile
     * @throws IOException
     */
    public static void packet(Path folder, Path zipFile) throws IOException {
        if (!Files.isDirectory(folder)) {
            throw new IllegalArgumentException(folder.toString() + " ????????????????????????");
        }
        OutputStream outputStream = Files.newOutputStream(zipFile, StandardOpenOption.CREATE_NEW);
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

        LinkedList<String> path = new LinkedList<>();

        try {
            Files.walkFileTree(folder, new FileVisitor<Path>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {

                    if (!dir.equals(folder)) {
                        // ??????????????????
                        String folder = dir.getFileName().toString();
                        path.addLast(folder);
                        // ????????????
                        ZipEntry zipEntry = new ZipEntry(path.stream().collect(Collectors.joining("/", "", "/")));
                        try {
                            zipOutputStream.putNextEntry(zipEntry);
                            zipOutputStream.flush();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    // ??????????????????
                    try (InputStream inputStream = Files.newInputStream(file)) {

                        // ????????????????????????????????????
                        String fileName = path.size() > 0
                                ? path.stream().collect(Collectors.joining("/", "", "")) + "/" + file.getFileName().toString()
                                : file.getFileName().toString();

                        ZipEntry zipEntry = new ZipEntry(fileName);

                        // ??????????????????
                        zipOutputStream.putNextEntry(zipEntry);
                        // ????????????
                        int len = 0;
                        byte[] buffer = new byte[1024 * 10];
                        while ((len = inputStream.read(buffer)) > 0) {
                            zipOutputStream.write(buffer, 0, len);
                        }

                        zipOutputStream.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    // ??????????????????
                    if (!path.isEmpty()) {
                        path.removeLast();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
            zipOutputStream.closeEntry();
        } finally {
            zipOutputStream.close();
            outputStream.close();
        }
    }

    /**
     * @param wkt
     * @param epsg
     * @return
     */
    public static String wktToGeoJson(String wkt, String epsg,boolean longitudeFirst) {
        String json = null;
        try {

            WKTReader reader = new WKTReader();
            Geometry geometry = reader.read(wkt);
            if (!"4490".equals(epsg)&&!"4326".equals(epsg)) {
                geometry = projectTransform(geometry, "EPSG:" + epsg, "EPSG:4490",longitudeFirst);
            }
            StringWriter writer = new StringWriter();
            GeometryJSON g = new GeometryJSON(15);
            g.write(geometry, writer);
            json = writer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static Geometry projectTransform(Geometry geometry, String epsgSource, String epsgTarget,
                                            boolean longitudeFirst) throws FactoryException, TransformException {
        // ????????????????????????????????????????????????ESPG??????wkt
        // "PROJCS[\"Xian_1980_3_Degree_GK_CM_111E\",GEOGCS[\"GCS_Xian_1980\",DATUM[\"D_Xian_1980\",SPHEROID[\"Xian_1980\",6378140.0,298.257]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Gauss_Kruger\"],PARAMETER[\"False_Easting\",500000.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",111.0],PARAMETER[\"Scale_Factor\",1.0],PARAMETER[\"Latitude_Of_Origin\",0.0],UNIT[\"Meter\",1.0]]";
        // CoordinateReferenceSystem mercatroCRS = CRS.parseWKT(strWKTMercator);
        CoordinateReferenceSystem crsSource = CRS.decode(epsgSource, longitudeFirst);
        CoordinateReferenceSystem crsTarget = CRS.decode(epsgTarget, true);
        // ????????????
        MathTransform transform = CRS.findMathTransform(crsSource, crsTarget);
        Geometry geometry1 = JTS.transform(geometry, transform);
        return geometry1;
    }

    public static String geoJsonToWkt(String geoJson) {
        String wkt = null;
        GeometryJSON gjson = new GeometryJSON();
        Reader reader = new StringReader(geoJson);
        try {
            Geometry geometry = gjson.read(reader);

            wkt = geometry.toText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wkt;
    }

    public static List<String> geojsonToWktList(String featureCollectionStr) throws IOException {
        JSONObject jsonObject = JSONObject.parseObject(featureCollectionStr);
        if (jsonObject.containsKey("crs")) {
            jsonObject.remove("crs");
        }
        //??????????????????
        List<String> wktList = new ArrayList<>();
        //??????FeatureJSON?????????GeometryJSON??????15?????????
        FeatureJSON featureJSON = new FeatureJSON(new GeometryJSON(15));
        FeatureCollection featureCollection = featureJSON.readFeatureCollection(jsonObject.toJSONString());
        SimpleFeatureType simpleFeatureType = (SimpleFeatureType) featureCollection.getSchema();
        System.out.println(simpleFeatureType.getGeometryDescriptor().getLocalName());
        SimpleFeatureIterator iterator = (SimpleFeatureIterator) featureCollection.features();
        while (iterator.hasNext()) {
            SimpleFeature simpleFeature = iterator.next();
            Geometry geom = (Geometry) simpleFeature.getDefaultGeometry();
            wktList.add(geom.toText());
        }
        iterator.close();
        return wktList;
    }

    public static String geojsonProject(String featureCollectionStr, String srEpsg,
                                        boolean longitudeFirst,String... tarEpsg) throws IOException,
            FactoryException
            , TransformException {
        if ("4490".equals(srEpsg) || "4326".equals(srEpsg)) {
            return featureCollectionStr;
        }
        JSONObject jsonObject = JSONObject.parseObject(featureCollectionStr);
        if (jsonObject.containsKey("crs")) {
            jsonObject.remove("crs");
        }
       JSONArray featuresArray= jsonObject.getJSONArray("features");
        JSONArray newfeaturesArray=new JSONArray();
        for (int a=0;a<featuresArray.size();a++){
         JSONObject featureObj=   featuresArray.getJSONObject(a);
           String geoType= featureObj.getJSONObject("geometry").getString("type");
           if ("Point".equalsIgnoreCase(geoType)){
               continue;
           }else {
               newfeaturesArray.add(featureObj);
           }
        }
        jsonObject.remove("features");
        jsonObject.put("features",newfeaturesArray);
        //??????????????????
        List<String> wktList = new ArrayList<>();
        //??????FeatureJSON?????????GeometryJSON??????15?????????
        FeatureJSON featureJSON = new FeatureJSON(new GeometryJSON(15));
        FeatureCollection featureCollection = featureJSON.readFeatureCollection(jsonObject.toJSONString());
        SimpleFeatureType simpleFeatureType = (SimpleFeatureType) featureCollection.getSchema();
        System.out.println(simpleFeatureType.getGeometryDescriptor().getLocalName());
        SimpleFeatureIterator iterator = (SimpleFeatureIterator) featureCollection.features();
        while (iterator.hasNext()) {
            SimpleFeature simpleFeature = iterator.next();
            Geometry geometry = (Geometry) simpleFeature.getDefaultGeometry();

            if (!geometry.getGeometryType().toLowerCase().contains("polygon")) {
                continue;
            }

            if (!"4490".equals(srEpsg) && !"4326".equals(srEpsg)) {
                geometry = projectTransform(geometry, "EPSG:" + srEpsg, "EPSG:4490",longitudeFirst);
            }
            wktList.add(geometry.toText());
        }
        iterator.close();
        JSONObject featurCollectionObj = new JSONObject();
        featurCollectionObj.put("type", "FeatureCollection");
        JSONArray features = new JSONArray();

        for (String wkt : wktList) {
            String geojson = wktToGeoJson(wkt, "4490",longitudeFirst);
            JSONObject geometry = JSONObject.parseObject(geojson);
            JSONObject featureobj=new  JSONObject();
            featureobj.put("geometry",geometry);
            featureobj.put("type","Feature");
            featureobj.put("properties",new JSONObject());
            features.add(featureobj);
        }
        featurCollectionObj.put("features", features);
        return featurCollectionObj.toString();
    }

    /**
     * ??????????????????
     *
     * @param size
     * @return
     */
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

    public static String getDataDir(Class c) {
        File dir = new File(System.getProperty("user.dir"));
        dir = new File(dir, "src");
        dir = new File(dir, "main");
        dir = new File(dir, "resources");

        return dir.toString() + File.separator;
    }

    public static String parseShpToGeojson(String shapePath) throws FactoryException, TransformException, IOException {
        int epsg=4490;
        String tarEpsg="4490";
        ogr.RegisterAll();
        // ??????????????????????????????????????????????????????
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
        // ????????????????????????????????????????????????????????????
        gdal.SetConfigOption("SHAPE_ENCODING", "");

        //????????????
        DataSource ds = ogr.Open(shapePath, 0);
        FileDataStore SHPdataStore = FileDataStoreFinder.getDataStore(new File(shapePath));
        SimpleFeatureSource featureSource = SHPdataStore.getFeatureSource();

      CoordinateReferenceSystem coordinateReferenceSystem=
              featureSource.getFeatures().getSchema().getCoordinateReferenceSystem();
       if (coordinateReferenceSystem.getName().getCode().equals("GCS_WGS_1984")){
epsg=4326;
        }
       else if (coordinateReferenceSystem.getName().getCode().equals("GCS_China_Geodetic_Coordinate_System_2000")){
           epsg=4490;
       }else if (coordinateReferenceSystem.getName().getCode().equals("CGCS2000_3_degree_Gauss_Kruger_zone_37")){
           epsg=4525;
       }
       else if (coordinateReferenceSystem.getName().getCode().equals("CGCS2000_3_degree_Gauss_Kruger_zone_36")){
           epsg=4524;
       }
       else if (coordinateReferenceSystem.getName().getCode().equals("CGCS2000_3_degree_Gauss_Kruger_CM_108E")){
           epsg=4545;
       }else if (coordinateReferenceSystem.getName().getCode().equals("CGCS2000_3_degree_Gauss_Kruger_CM_105E")){
           epsg=4544;
       }
//        Layer layer = ds.GetLayer(0);
//        SpatialReference spatRef = layer.GetSpatialRef();
//        int EPSG =  spatRef.AutoIdentifyEPSG();
        if (ds == null) {
            System.out.println("?????????????????????");
            return null;
        }
        System.out.println("?????????????????????");
        // GeoJSON shp???json?????????
        org.gdal.ogr.Driver dv = ogr.GetDriverByName("GeoJSON");
        if (dv == null) {
            System.out.println("?????????????????????");
            return null;
        }
        System.out.println("??????GeoJson???????????????");
        dv.CopyDataSource(ds, shapePath.substring(0, shapePath.length() - 4) + ".json");
        String json = FileUtil.readFileByLines(shapePath.substring(0, shapePath.length() - 4) + ".json");
        File delfile = new File(shapePath.substring(0, shapePath.length() - 4) + ".json");
        delfile.delete();
        json=  json+"]}";
        json=  GeojsonUtil.geojsonProject(json,String.valueOf(epsg),true,tarEpsg);
        return json;
    }

    public static List<String> readShpTogeojson(String absolutePath) throws Exception {
        //????????????
        File file = new File(absolutePath);
        if (file == null) {
            return null;
        }
        String wkt = "";
//        //map??????shapefile key-value??????
        List<String> list = new ArrayList<String>();
        //??????store??????featurecollection
        FileDataStore SHPdataStore = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = SHPdataStore.getFeatureSource();
        SimpleFeatureCollection simpleFeatureCollection = featureSource.getFeatures();
        SimpleFeatureIterator itertor = simpleFeatureCollection.features();
//        CoordinateReferenceSystem crsSource = parsePrjFile(absolutePath.substring(0,
//                absolutePath.length() - 4) + ".prj");
//        if (crsSource == null) {
//            throw new Exception("?????????????????????????????????");
//        }
//        // ????????????
//        MathTransform transform = CRS.findMathTransform(crsSource, crsTarget);
        GeometryJSON g = new GeometryJSON(15);
        while (itertor.hasNext()) {
            //Map<String, Object> data = new HashMap<String, Object>();
            SimpleFeature feature = itertor.next();
            Geometry geometry = (Geometry) feature.getDefaultGeometry();
            //Geometry geometry1 = JTS.transform(geometry, transform);
            StringWriter writer = new StringWriter();
            g.write(geometry, writer);
            String json = writer.toString();
//            Collection<Property> p = feature.getProperties();
//            Iterator<Property> it = p.iterator();
            //??????feature???properties
//            while (it.hasNext()) {
//                Property pro = it.next();
//                String field = pro.getName().toString();
//                String value = pro.getValue().toString();
//                field = field.equals("the_geom") ? "wkt" : field;
//                byte[] bytes = value.getBytes("iso8859-1");
//                value = new String(bytes, "gbk");
//                data.put(field, value);
//            }
            list.add(json);
        }
        itertor.close();

        return list;
    }

    /**
     * ????????????????????????
     *
     * @param prjPath
     * @return
     */
    public static CoordinateReferenceSystem parsePrjFile(String prjPath) {
        CoordinateReferenceSystem crs = null;
        // read the prj serviceInfo from the file
        PrjFileReader projReader = null;

        FileInputStream inStream = null;
        FileChannel channel = null;
        try {
            final File prj = new File(prjPath);
            if (prj.exists() && prj.canRead()) {

                inStream = new FileInputStream(prj);
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                inStream.close();
                bufferedReader.close();
                crs = CRS.parseWKT(sb.toString());
            }

            // If some exception occurs, warn about the error but proceed
            // using a default CRS
        } catch (FactoryException | IOException e) {

        } finally {
            if (projReader != null) {
                try {
                    projReader.close();
                } catch (IOException e) {

                }
            }
        }
        if (inStream != null) {
            try {
                inStream.close();
            } catch (Throwable e) {
            }
        }

        if (channel != null) {
            try {
                channel.close();
            } catch (Throwable e) {
            }
        }

        return crs;
    }
}

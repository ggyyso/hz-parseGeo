package com.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.service.TxtJsonService;
import com.common.exception.DataConverterException;
import com.common.exception.FileOptionException;
import com.common.utils.FileUtil;
import com.common.utils.GeojsonUtil;
import com.common.utils.ObjUtils;
import com.common.utils.RecursiveZipUtils;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;
import org.apache.commons.lang3.StringUtils;
import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Driver;
import org.gdal.ogr.ogr;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: TxtJsonServiceImpl <br>
 * date: 2021/2/23 18:33 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
@Service("txtJsonService")
public class TxtJsonServiceImpl implements TxtJsonService {

    @Value("${teighaFileConverter.path}")
    private String teighaFileConverterPath;


//    [属性描述]
//    坐标系=2000国家大地坐标系
//            几度分带=3
//    投影类型=高斯克吕格
//            计量单位=米
//    带号=36
//    精度=0.01
//    转换参数=,,,,,,
//            [地块坐标]
//    34,0.7493,1,地块1,,,,,@
//    J1,1,3665080.547,36376092.199
//    J2,1,3664991.425,36376166.376
//    J3,1,3664968.040,36376104.216
//    J4,1,3664941.465,36376038.517
//    J1,1,3665080.547,36376092.199
//    21,0.0566,1,地块2,,,,,@
//    J1,1,3665019.773,36375940.108
//    J2,1,3665014.930,36375957.707
//    J3,1,3664983.209,36375951.381
//    J4,1,3664991.205,36375934.069
//    J1,1,3665019.773,36375940.108
//    26,0.8247,1,地块3,,,,,@
//    J1,1,3654802.775,36377699.818
//    J2,1,3654834.229,36377836.232
//    J3,1,3654772.444,36377848.309
//    J4,1,3654768.366,36377830.631
//    J5,1,3654750.042,36377710.117
//    J1,1,3654802.775,36377699.818

    /**
     * 解析txt为geoJson
     *
     * @param txtFilePath
     * @return String
     */
    @Override
    public String txt2GeoJson(String txtFilePath,String epsg) {
        String datas = "";
        // 读取文本文件返回geojson格式的polygons
        List<List<String>> multiXYs = new ArrayList<>(16);
        List<String> tempXYs = null;
        List<Integer> tempIndexes = new ArrayList<>();
        Map<String,List<String>> diKuaiMap=new HashMap<>();
        //标识是否是普通文本
        boolean isNormalTxt=false;
        try {
            FileInputStream fileStream = new FileInputStream(txtFilePath);
            InputStreamReader readStream = new InputStreamReader(fileStream, "GBK");
            BufferedReader bufferReader = new BufferedReader(readStream);
            String str;
            // 按行读取字符串
            // 先循环一遍，取出@符号后边的，放到list中，同时记录@行的索引，从0开始
            int index = 0;
            int rowNo=0;
            String diKuaiName=null;
            while ((str = bufferReader.readLine()) != null) {
                rowNo++;
                System.out.println(str);
                if (StringUtils.isEmpty(str)) {
                    continue;
                }
                if (str.contains(",")&&rowNo==1){
                    isNormalTxt=true;
                    tempXYs=new ArrayList<>();
                }
                if (isNormalTxt){
                    if ("4490".equals(epsg)||"4326".equals(epsg)){
                        String[] xy= str.split(",");
                        str=xy[1]+","+xy[0];
                    }
                    tempXYs.add(str);
                    continue;
                }
                if (str.contains("@")) {
                    index++;
                    if (!StringUtils.isEmpty(diKuaiName)){
                        diKuaiMap.put(diKuaiName,tempXYs);
                        diKuaiName=null;
                    }
                    tempXYs = new ArrayList<>();
                    String[] props=str.split(",");
                    if (props.length>=4){
                        diKuaiName=props[3];
                        if (!StringUtils.isEmpty(diKuaiName)){
                            diKuaiName="地块"+index;
                            diKuaiName= ObjUtils.NumberToString(diKuaiName);
                        }
                    }
                    continue;
                }
                if (tempXYs != null) {

                    tempXYs.add(str);

                }
            }
            if (!StringUtils.isEmpty(diKuaiName)){
                diKuaiMap.put(diKuaiName,tempXYs);
                diKuaiName=null;
            }
            fileStream.close();
            readStream.close();
            bufferReader.close();
        } catch (IOException ex) {
            throw new FileOptionException("解析txt文件，转换为坐标串失败！", ex);
        }
        //读取普通文本
        if (isNormalTxt) {

                multiXYs.add(tempXYs);
        }

        String wkt =null;
        String fetureCollectionStr=null;
        if (isNormalTxt){
            wkt= createNormalWKT(multiXYs);
            try {
                datas = GeojsonUtil.wktToGeoJson(wkt,epsg,false);

            } catch (Exception ex) {
                throw new DataConverterException("将Jts Geometry转换为EsriJson失败！", ex);
            }
            //组装为FeatureCollection
            fetureCollectionStr="{\n" +
                    "    \"type\": \"FeatureCollection\",\n" +
                    "    \"features\": [{\n" +
                    "            \"type\": \"Feature\",\n" +
                    "           \"properties\": {},"+
                    "            \"geometry\": \n" +datas+
                    "        }\n" +
                    "    ]\n" +
                    "}\n";
        }else {
            fetureCollectionStr=createZrzyGeoJson(diKuaiMap,epsg);
        }

        return fetureCollectionStr;
    }

    private String createZrzyGeoJson(Map<String, List<String>> diKuaiMap,String epsg) {
        if (null==diKuaiMap||diKuaiMap.size()==0)
        {
            throw new DataConverterException("不含有空间数据！");
        }
        JSONObject feaureColObj=new JSONObject();
        feaureColObj.put("type","FeatureCollection");
        JSONArray featuresArray=new JSONArray();
        Map<String, List<String>> diKuaiMapNew=new HashMap<>();
        List<String> dikuaiList=new ArrayList<>();
        for(String key : diKuaiMap.keySet()){
            dikuaiList.add(key);
        }
        for(int p=dikuaiList.size()-1;p>-1;p-- ){
            String dikuaiName=dikuaiList.get(p);
            List<String> coorString = diKuaiMap.get(dikuaiName);
            JSONObject featureObj=new JSONObject();
            JSONObject propertiesObj=new JSONObject();
            propertiesObj.put("name",dikuaiName);
            featureObj.put("properties",propertiesObj);
            featureObj.put("type","Feature");

            StringBuilder sb = new StringBuilder("POLYGON((");
            for (String coord:coorString){
                String[] strs = coord.split(",");
                if (strs.length != 4) {
                    throw new DataConverterException("txt文件坐标串行格式错误！行内容为："
                            + coord);
                }
                sb.append(strs[2].trim());
                sb.append(" ");
                sb.append(strs[3].trim());
                sb.append(",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append("))");
            String geojsonGeometry = GeojsonUtil.wktToGeoJson(sb.toString(), epsg, false);
            JSONObject geometryObj = JSONObject.parseObject(geojsonGeometry);
            featureObj.put("geometry", geometryObj);
            featuresArray.add(featureObj);
            System.out.println(dikuaiName+":"+sb);
        }
        feaureColObj.put("features",featuresArray);
        return  feaureColObj.toJSONString();
    }

    /**
     * 自然资源部文本创建Wkt multipolygon
     *
     * @param multiXYs
     * @return
     */
    private String createZrzyWKT(List<List<String>> multiXYs) {
        // MULTIPOLYGON(((40 10, 30 0, 40 10, 30 20, 40 10), (30 10, 30 0, 40 10, 30 20, 30 10)))
        StringBuilder sb = new StringBuilder("MULTIPOLYGON((");
        for (List<String> item : multiXYs) {
            sb.append("(");
            for (String strPt : item) {
                String[] strs = strPt.split(",");
                if (strs.length != 4) {
                    throw new DataConverterException("txt文件坐标串行格式错误！行内容为："
                            + strPt);
                }
                sb.append(strs[2].trim());
                sb.append(" ");
                sb.append(strs[3].trim());
                sb.append(",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append("),");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("))");

        return sb.toString();
    }
    private String createNormalWKT(List<List<String>> multiXYs) {

        // MULTIPOLYGON(((40 10, 30 0, 40 10, 30 20, 40 10), (30 10, 30 0, 40 10, 30 20, 30 10)))
        // MPOLYGON((40 10, 30 0, 40 10, 30 20, 40 10))
        String Type="MULTIPOLYGON((";
        if (multiXYs.size()==1){
            Type="POLYGON(";
        }
        StringBuilder sb = new StringBuilder(Type);
        for (List<String> item : multiXYs) {
            if (!item.get(0).equals(item.get(item.size()-1))){
                item.add(item.get(0));
            }
            sb.append("(");
            for (String strPt : item) {
                String[] strs = strPt.split(",");
                if (strs.length != 2) {
                    throw new DataConverterException("txt文件坐标串行格式错误！每行内容为y,x格式组织："
                            + strPt);
                }
                sb.append(strs[0].trim());
                sb.append(" ");
                sb.append(strs[1].trim());
                sb.append(",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append("),");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("))");
        if (multiXYs.size()==1){
            sb.append("))");
        }else {
            sb.append(")");
        }

        return sb.toString();
    }
    /**
     * 验证上传文件是否包含shp文件
     *
     * @param filePath
     * @param destiPath 必须以路径分隔符结尾
     * @return boolean
     */
    @Override
    public String validateUploadZip(String filePath, String destiPath) {

        try {
            RecursiveZipUtils.unZipFiles(filePath, destiPath);
        } catch (IOException ex) {
            throw new FileOptionException("解压zip文件出错！", ex);
        }

        File destiFile = new File(destiPath);
        File[] files = destiFile.listFiles();
        for (File tempFile : files) {
            if (tempFile.getName().endsWith(".shp")) {
                return destiPath + tempFile.getName();
            }
        }

        return null;
    }

    @Override
    public String parseDwg(String filePath, String dwgPath, String dxfPath,String epsg) throws FactoryException, TransformException, IOException {

        FileUtil.copyFiles(filePath, dwgPath);
        File dwgFile=new File(dwgPath);
        File dxfFile=new File(dxfPath);
        if (!dxfFile.exists()){
            dxfFile.mkdirs();
        }
        //dwg 转dxf
        try {
            String CmdStr=
                    "\""+ teighaFileConverterPath+"\" \""+FileUtil.getFileParentPath(filePath)+File.separator+dwgFile.getParentFile().getName()+"\" \""+dxfPath+"\" " +
                    "ACAD2018 " +
                    "DXF 0 0";
            CmdStr= CmdStr.replace("\\","/");
            CmdStr= CmdStr.replace("/","//");

            Process pro =
                    Runtime.getRuntime().exec(CmdStr);
            String line;
            InputStreamReader inputStreamReader=  new InputStreamReader(pro.getInputStream());
            BufferedReader buf = new BufferedReader(inputStreamReader);
            while ((line = buf.readLine()) != null)
                System.out.println(line);
            if (buf!=null){
                buf.close();
            }
            if (inputStreamReader!=null){
                inputStreamReader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ogr.RegisterAll();
//		Driver dr=ogr.GetDriverByName("CAD");
//
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
        gdal.SetConfigOption("SHAPE_ENCODING", "");
        gdal.SetConfigOption("DXF_ENCODING", "ASCII"); //设置DXF缺省编码
//		String strVectorFile = "D:\\SXGIS\\2021\\汉中国土空间平台\\dwgR2000.DWG";
//        DataSource ds= dr.Open(strVectorFile);
        //String strVectorFile = "D:\\sxgis\\2021\\示例CAD文件_高斯大地坐标系_中国2000_36带3_北2.dxf";
        String fileNameNoExt=dwgFile.getName().substring(0,dwgFile.getName().length()-4)+".dxf";
        DataSource ds = ogr.Open(dxfPath+File.separator+fileNameNoExt, 0);
        if (ds == null) {
            System.out.println("打开文件失败！");
            return null;
        }
        System.out.println("打开文件成功！");

        Driver dv = ogr.GetDriverByName("GeoJSON");
        if (dv == null) {
            System.out.println("打开驱动失败！");
            return null;
        }
        System.out.println("打开驱动成功！");
        dv.CopyDataSource(ds, filePath.substring(0, filePath.length() - 4) + ".json");

        String json = FileUtil.readFileByLines(filePath.substring(0, filePath.length() - 4) + ".json");
        //转换之后的bug
        json+="]}";
        //gdal 将dwg转为geojson 后，面要素会转为线要素，判断，如果Coordinates的第一个数组和最后一个数组相同，则将该要素类型设置为polygon
        JSONObject featureCollectionObject=JSONObject.parseObject(json);
        JSONArray featuresArray=featureCollectionObject.getJSONArray("features");
        JSONArray newFeaturesArray=  new JSONArray();
        JSONArray NewfeatureArray=new JSONArray();
        for (int i=0;i<featuresArray.size();i++){
            JSONObject featureObj=featuresArray.getJSONObject(i);
          String type=  featureObj.getJSONObject("geometry").getString("type");
          if (type.equalsIgnoreCase("LineString")){
              JSONArray coorArray=  featureObj.getJSONObject("geometry").getJSONArray("coordinates");
              JSONArray coordStartArray=coorArray.getJSONArray(0);
              JSONArray coordEndArray=coorArray.getJSONArray(coorArray.size()-1);
              //如果首尾坐标相等，则将feature类型改为面
              if (String.valueOf(coordStartArray.getDoubleValue(0)).equals(String.valueOf(coordEndArray.getDouble(0)))&&String.valueOf(coordStartArray.getDoubleValue(1)).equals(String.valueOf(coordEndArray.getDouble(1)))){
                  featureObj.getJSONObject("geometry").put("type","Polygon");
                  JSONArray newCoordArray=  new JSONArray();
                  newCoordArray.add(coorArray);
                  featureObj.getJSONObject("geometry").put("coordinates",newCoordArray);
              }
          }
            newFeaturesArray.add(featureObj);
        }
        featureCollectionObject.put("features",newFeaturesArray);

        //进行投影转换
        json=featureCollectionObject.toJSONString();
        json=GeojsonUtil.geojsonProject(json,epsg,true);
        return json;
    }

    /**
     * create a polygon(多边形) by WKT
     *
     * @return
     * @throws ParseException
     */
    private Polygon createPolygonByWKT(String wkt) throws ParseException {
        WKTReader reader = new WKTReader();
        Polygon polygon = null;
        try {
            polygon = (Polygon) reader.read("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))");
        } catch (com.vividsolutions.jts.io.ParseException ex) {
            throw new FileOptionException("解析txt文件，转换为MultiPolygon失败！", ex);
        }
        return polygon;
    }

    /**
     * create multi polygon by wkt
     *
     * @return
     * @throws ParseException
     */
    public MultiPolygon createMulPolygonByWKT(String wkt) {
        WKTReader reader = new WKTReader();
        MultiPolygon mpolygon = null;
        try {
//            mpolygon = (MultiPolygon) reader.read(
//                    "MULTIPOLYGON(((40 10, 30 0, 40 10, 30 20, 40 10),(30 10, 30 0, 40 10, 30 20, 30 10)))");
            mpolygon = (MultiPolygon) reader.read(wkt);
        } catch (com.vividsolutions.jts.io.ParseException ex) {
            throw new DataConverterException("将坐标串转换为MultiPolygon失败！", ex);
        }
        return mpolygon;
    }

}

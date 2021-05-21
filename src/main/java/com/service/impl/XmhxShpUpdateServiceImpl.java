package com.service.impl;

import com.dto.AddFeatureeData;
import com.service.XmhxShpUpdateService;
import com.common.utils.GeojsonUtil;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jason Wong
 * @title: QypgShpUpdateServiceImpl
 * @projectName hz-mis
 * @description: TODO
 * @date 2021/3/13 11:09
 */
@Service
public class XmhxShpUpdateServiceImpl implements XmhxShpUpdateService {

    @Value("${YouMapServerData.yongDiHongXian}")
    String shapfilePath;


    @Override
    public void addFeature(AddFeatureeData info) throws Exception {

        int lsh = info.getLsh();
        String xmdm = info.getXmdm();
        String xmmc =info.getXmmc();
        String xmfwGeo =info.getXmfw();
//        List<XmfwgeoEntity> xmfwgeoEntityList = xmfwgeoRepository.findByXmdm(xmdm);
//        if(xmfwgeoEntityList.size()==0){
//            throw  new Exception("不含有该项目的空间范围数据");
//        }
        //shapfile要素更新

//读取shp文件
        File file = new File(shapfilePath);
        ShapefileDataStore ds = null;
        //设置编码
        Charset charset = Charset.forName("utf-8");
        if (file.exists()) {

            ds = new ShapefileDataStore(file.toURI().toURL());

            ds.setCharset(charset);
            String typeName = ds.getTypeNames()[0];
            SimpleFeatureSource featureSource = null;
            featureSource = ds.getFeatureSource(typeName);
            SimpleFeatureCollection simpleFeatureCollection = featureSource.getFeatures();
            SimpleFeatureIterator itertor = simpleFeatureCollection.features();
            boolean isExistProject = false;
            //查询项目代码对应的空间数据是否已经存在
            while (itertor.hasNext()) {
                SimpleFeature feature = itertor.next();
                int lshTmp = (int) feature.getAttribute("LSH");
                if (lsh==lshTmp) {
                    isExistProject = true;
                }
            }
            itertor.close();
            ds.dispose();
            if (isExistProject) {
                throw new Exception("该流水号的用地红线已经存在");
            }

        } else {
            //创建shape文件对象
            File fileBuf = new File(shapfilePath);
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put(ShapefileDataStoreFactory.URLP.key, fileBuf.toURI().toURL());
            params.put("create spatial index", Boolean.TRUE);
            ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
            //获取原shp文件字段名
//            SimpleFeatureType sft = featureSource.getSchema();
//            List<AttributeDescriptor> attrs = sft.getAttributeDescriptors();

            //定义图形信息和属性信息
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            //tb.setCRS(DefaultGeographicCRS.WGS84);
            tb.setName("shapefile");
            tb.add("the_geom", Polygon.class, 4490);
            tb.add("lsh", Integer.class);
            tb.add("XMDM", String.class);
            tb.add("XMMC", String.class);
            ds.createSchema(tb.buildFeatureType());
            //设置编码
            ds.setCharset(charset);
            ds.dispose();
        }
        WKTReader reader = new WKTReader();
        ds = new ShapefileDataStore(file.toURI().toURL());
        ds.setCharset(charset);

        //获取类型
        SimpleFeatureType TYPE = ds.getFeatureSource().getSchema();
        FeatureStore<SimpleFeatureType, SimpleFeature> featureStore = (FeatureStore<SimpleFeatureType, SimpleFeature>) ds.getFeatureSource();
        Transaction transaction = new DefaultTransaction(null);
        featureStore.setTransaction(transaction);
        //创建要素模板
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
        List<SimpleFeature> simpleFeatureList=new ArrayList<>();
        //for (XmfwgeoEntity xmfwgeoEntity : xmfwgeoEntityList) {
            //SimpleFeature featureBuf = writer.next();
           // String xmfwGeo=xmfwgeoEntity.getXmfw();
            List<String>WktList= GeojsonUtil.geojsonToWktList(xmfwGeo);
            for (String wkt:WktList) {
                Geometry geometry = reader.read(wkt);
                //geometry.setSRID(4490);
                //构建要素
                SimpleFeature feature = featureBuilder.buildFeature(null);
               // feature.setAttribute("the_geom", geometry);
                feature.setAttribute("XMDM", xmdm);
                feature.setAttribute("XMMC", xmmc);
                feature.setAttribute("LSH", lsh);
                feature.setDefaultGeometry(geometry);
                simpleFeatureList.add(feature);
            }

        SimpleFeatureCollection SimpleFeaturecollection = new ListFeatureCollection(TYPE, simpleFeatureList);
        try
        {
            featureStore.addFeatures(SimpleFeaturecollection);
            transaction.commit();
        }
        catch (Exception problem)
        {
            problem.printStackTrace();
            transaction.rollback();
        }
        finally
        {
            transaction.close();
        }

    }

    @Override
    public String dijiInfoCheck(Object info) throws Exception {
        Map<String, Object> infoMap = (Map<String, Object>) info;

        if (!infoMap.containsKey("xmfw")) {
            throw new Exception("不包含xmfw参数");
        }

        String xmfw = infoMap.get("xmfw").toString();

        return  null;
    }
}

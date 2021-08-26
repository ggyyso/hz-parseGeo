# hz-parseGeo
解析dwg、shapefile为geojson或wkt

Dwg格式文件转json 分为两步，第一步将dwg文件转为dxf文件，第二步将dxf文件转换为json文件。

第一步将dwg文件转为dxf文件：

第一种 使用Teigha File  converter软件。
--------------------------------------------------------------------------------
安装Teigha File  converter软件
Teigha File Converter是一款简便实用的CAD文件转换工具，可以进行不同格式的CAD文件转换，包括dwg和dxf格式，接受源目录、输入文件筛选器、递归标志、审计标志等
【功能特点】
通过在程序中处理DWG和DXF格式的多个版本的文档，以便在它们之间进行快速转换，而不会丢失数据。Teigha File Converter 具有一个过滤器，用于快速搜索和识别驱动器和文件夹中兼容的文件。
该工具实际上是一个CAD绘图转换器，可以在不同版本的DXF文件和DWG文件之间转换。最显著特点是，它支持从R12 DWG到2013DWG的输出文件的各种版本，以及ASCII和二进制DXF文件的各种版本。
这个小工具的另一个重要好处是它直观、直观、使用简单。即使初学者可以处理它没有任何问题。还可以指定输入文件过滤器以提高处理速度。通过指定整个文件夹并配置文件过滤器，可以使用此实用程序同时批处理多个文件。命令行接口也是可用的，特别适用于更高级的用户。还提供了对输入文件执行审计和修复任务的选项。
【输入格式】
源目录
目标目录
输入文件筛选器，如*的.dwg（默认为“*的.dwg; *。DXF”）
输出版本/类型
递归标志
审计标志
【使用说明】
1、打开需要进行转换的CAD文件；
2、设置输出文件目录；
3、设置CAD版本；
4、点击【Start】开始转换

调用Teigha File  converter 将dwg 转为dxf,
为防止中文乱码，在输出版本中选择高版本的dxf格式（2018 AsCII DXF）
或使用命令行进行代码调用，调用格式如下。

执行命令行代码为：
```
"C:\Program Files (x86)\ODA\Teigha File Converter 4.3.2\TeighaFileConverter.exe" "D://GIS//2021//测试" "D://SXGIS//2021" ACAD2018 DXF 0 0
```
第二种 使用apose.cad库
--------------------------------------------------------------------------------
```
  //apose.cad 将dwg转dxf  中文会存在乱码，考虑能否设置输出编码
  String inputFile = "D:\\SXGIS\\2021\\汉中国土空间平台\\kcd.dwg";
  String outFile = "D:\\Line.dxf";
  CadImage cadImage = (CadImage) Image.load(inputFile);
  int i=  cadImage.getFileEncoding();
  cadImage.save(outFile);
```
第二步，将dxf文件转为geojson 采用gdal库
使用gdal将dxf 解析为geojson，部署java gdal环境，见gdal部署环境。
--------------------------------------------------------------------------------


```
/**
     * dwg转geojson
     * @param args
     */
    public static void main(String[] args) {

 

        ogr.RegisterAll();
//    Driver dr=ogr.GetDriverByName("CAD");
//        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
        gdal.SetConfigOption("SHAPE_ENCODING", "");
        gdal.SetConfigOption("DXF_ENCODING", "ASCII"); //设置DXF缺省编码
//    String strVectorFile = "D:\\GIS\\2021\\测试\\dwgR2000.DWG";
//        DataSource ds= dr.Open(strVectorFile);
        String strVectorFile = "D:\\sxgis\\2021\\示例CAD文件_高斯大地坐标系_中国2000_36带3_北2.dxf";
        DataSource ds = ogr.Open(strVectorFile, 0);
        if (ds == null) {
            System.out.println("打开文件失败！");
            return;
        }
        System.out.println("打开文件成功！");

        Driver dv = ogr.GetDriverByName("GeoJSON");
        if (dv == null) {
            System.out.println("打开驱动失败！");
            return;
        }
        System.out.println("打开驱动成功！");
        dv.CopyDataSource(ds, "D:\\Dgn.geojson");
        System.out.println("转换成功！");
    }
```



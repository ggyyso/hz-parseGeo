import com.common.SQLHelper;
import com.common.utils.FileUtil;
import com.vividsolutions.jts.geom.Envelope;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Jason Wong
 * @title: testSqlite
 * @projectName hz-parseGeo
 * @description: TODO
 * @date 2021/5/11 9:30
 */
public class testSqlite {
    public static void main(String[] args) throws Exception {
write();

        File tileFile = new File("src/main/resources/ip.db");
        Connection connection = SQLHelper.establishConnection(tileFile);
//        String tableName = "2";
//        Statement SQliteSt = connection.createStatement();
//        SQliteSt.execute("PRAGMA key=''");
//        SQliteSt.execute("PRAGMA rekey='pwd'");
//        SQliteSt.execute("CREATE TABLE '" + tableName + "' (" +
//                "'tile_Id'  text(20) ,'tile_data' blob );");
//
//        PreparedStatement stmt =
//                connection.prepareStatement(
//                        "INSERT INTO \"" + tableName +"\" (tile_Id," +
//                                "tile_data) " +
//                                "VALUES(?," +
//                                "?) ");
//
//        // 关闭事务自动提交 ,这一行必须加上，否则每插入一条数据会向log插入一条日志
//        connection.setAutoCommit(false);
//        stmt.setString(1,  zoom+ "_" + x + "_" + y);
//        stmt.setBytes(2,tile);
//        stmt.addBatch();
//
//            stmt.executeBatch();
//        Envelope env = new Envelope(77.7107,134.44,
//                18.4899,53.9936);
//
//        //执行批量处理语句；
//        stmt.executeBatch();
//        //// 提交事务
//        connection.commit();
//        stmt.close();
        connection.close();
    }

    public static void write() throws IOException {
        List<String> uid=new ArrayList<>();
        for (int i = 0;i<1217;i++){
            uid.add(UUID.randomUUID().toString());
        }
        System.out.println(uid);
        BufferedWriter out = new
                BufferedWriter(new FileWriter("D://b.txt"));
        out.write(uid.toString());
        out.close();
    }
}

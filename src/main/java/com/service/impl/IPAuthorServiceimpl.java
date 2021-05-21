package com.service.impl;

import com.common.SQLHelper;
import com.service.IPAuthorService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Jason Wong
 * @title: IPAuthorServiceimpl
 * @projectName hz-parseGeo
 * @description: TODO
 * @date 2021/5/12 16:27
 */
@Service
public class IPAuthorServiceimpl implements IPAuthorService {

    @Override
    public void add(String ip) throws Exception {
        File tileFile = new File("src/main/resources/ip.db");
        Connection connection = SQLHelper.establishConnection(tileFile);
        PreparedStatement stmt =
                connection.prepareStatement(
                        "INSERT INTO ipT (id, ip) " +
                                "VALUES(?,?) ");

        // 关闭事务自动提交 ,这一行必须加上，否则每插入一条数据会向log插入一条日志
        connection.setAutoCommit(false);
        stmt.setInt(1,  0);
        stmt.setString(2,ip);
       // stmt.setDate(4,new Date());
        stmt.addBatch();
        int[] out=stmt.executeBatch();
    }

    @Override
    public boolean author(String ip) throws Exception {
        boolean result=false;
        File tileFile = new File("src/main/resources/ip.db");
        Connection connection = SQLHelper.establishConnection(tileFile);
        PreparedStatement stmt =
                connection.prepareStatement(
                        "select  ip from " +
                                "ipT where ip=?");
        stmt.setString(1,  ip);
       try (ResultSet resultSet = stmt.executeQuery()) {
         while (resultSet.next()){
             result=true;
             break;
         }
         resultSet.close();
         stmt.close();
         connection.close();
       }
        return result;
    }
}

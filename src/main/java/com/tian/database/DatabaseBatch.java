package com.tian.database;

import sun.security.jca.GetInstance;

import java.sql.*;

public class DatabaseBatch {

    /**
     * 数据库的迁移  从一个数据库查询添加到另一个数据库
     *  本页面中像一些数据库的连接 还有关闭资源等  在实际工作中基本用不到
     *  采用JDBC的批处理  效率也是最高的
     *  JDBC的批量处理
     *  分批次  10W此提交一次
     * 时间：2020-9-20
     */

    public void main(String[] args) {
        System.out.println("开始处理数据库");
        Connection itcoon = getIteconntest();
        try {
            Statement statement = itcoon.createStatement();
            ResultSet iters = statement.executeQuery("select * from test1");
            //从结果集中获取长度   字段长度
            int size = iters.getMetaData().getColumnCount();

            //拼接SQL语句   insert into lagou values (?,?,?,?,?)
            StringBuffer sbf = new StringBuffer();
            sbf.append("insert into  test2 values(");
            String link = "";
            String  asd="";
            for (int i = 0; i < size; i++) {
                sbf.append(link).append("?");
                link = ",";
                sbf.append(")");
            }
            //拼接结束

            //连接数据库  采用预编译PreparedStatement
            Connection mysqlconn = getIteconntest1();
            PreparedStatement preparedStatement = mysqlconn.prepareStatement(sbf.toString());

            //取出结果并向数据库中插入数据   要插入的数据库test1  使用批处理

            //记录条数
            int count = 0;
            int num = 0;
            //取消事务
            mysqlconn.setAutoCommit(false);
            //记录时间  可以不用
            long start = System.currentTimeMillis();
            while (iters.next()) {
                //采用先加进行记录   后续到达10W 进入一次提交
                ++count;
                for (int i = 1; i < size; i++) {
                    preparedStatement.setObject(i, iters.getObject(i));
                }
                //将语句储存起来，这里没有向数据库中插入
                preparedStatement.addBatch();
                //当数据达到自己预定的条数的时候  提交  我的是10W
                if (count % 100000 == 0) {
                    //执行处理
                    preparedStatement.executeBatch();
                    System.out.println("第" + num + "次提交,耗时:" + (System.currentTimeMillis() - start) / 1000.0 + "s");

                }
            }
            //防止有数据未提交
            preparedStatement.executeBatch();
            //提交
            mysqlconn.commit();
            System.out.println("完成 " + count + " 条数据,耗时:" + (System.currentTimeMillis() - start) / 1000.0 + "s");
            //恢复事务
            mysqlconn.setAutoCommit(true);
            //关闭资源
            close(itcoon,statement,null);
            close(mysqlconn,preparedStatement,iters);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 以下为常规的数据库连接
     * @return
     */
    public Connection getIteconntest()  {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            //连接的数据库
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/test","用户名","密码");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public Connection getIteconntest1(){
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/test1","用户名","密码");
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //关闭数据的连接
    public  void  close(Connection conn,Statement stmt,ResultSet rs){

        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(stmt!=null){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}


package org.example;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Employee {
    public static Connection connect() throws Exception
    {
        Properties prop = new Properties();
        FileInputStream fs = new FileInputStream("D:\\Work\\Projects\\Employee-Management-System\\src\\main\\resources\\application.properties");
        prop.load(fs);
        Class.forName(prop.getProperty("registerClass"));
        Connection con = DriverManager.getConnection(prop.getProperty("url")+prop.getProperty("port")+"/"+prop.getProperty("databaseName"),prop.getProperty("userName"),prop.getProperty("userPass"));
        return con;
    }
    public static void disconnect(Connection con) throws Exception
    {
        if(!con.isClosed())
            con.close();
    }
    public static void main(String[] args)
    {
        MyFrame f = new MyFrame();
    }
}
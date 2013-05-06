package com.zomwi.simidic.db;

import java.sql.*;

public class DataBase {

    private Connection conexion;
    public static final String CREATE_TABLE_INFO = "create table info (name text, author text, description text, language_begin text, language_end text);";
    public static final String CREATE_TABLE_WORDS = "create table words (_id integer primary key autoincrement, word text, meaning text, summary text);";
    //public static final String CREATE_TABLE_MEANINGS = "create table meanings (_id integer primary key autoincrement, id_word integer, value text);";

    public DataBase(String name) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        conexion = DriverManager.getConnection("jdbc:sqlite:" + name + ".db");
    }

    public void create() throws SQLException {
        Statement statement = conexion.createStatement();
        statement.executeUpdate(CREATE_TABLE_INFO);
        statement.executeUpdate(CREATE_TABLE_WORDS);
        //statement.executeUpdate(CREATE_TABLE_MEANINGS);
        statement.close();
    }

    public void insertInfo(String name, String author, String description, String languageBegin, String languageEnd) throws SQLException {
        PreparedStatement statement = conexion.prepareStatement("insert into info (name, author, description, language_begin, language_end) values (?,?,?,?,?)");
        statement.setString(1, name);
        statement.setString(2, author);
        statement.setString(3, description);
        statement.setString(4, languageBegin);
        statement.setString(5, languageEnd);
        statement.executeUpdate();
    }

    public void insertWord2(String word, String meaning) throws SQLException {
        PreparedStatement statement = conexion.prepareStatement("insert into words (value) values (?)");
        statement.setString(1, word);
        statement.executeUpdate();


        Statement statementExtra = conexion.createStatement();
        ResultSet result = statementExtra.executeQuery("SELECT last_insert_rowid()");
        int x = result.getInt(1);
        PreparedStatement statement2 = conexion.prepareStatement("insert into meanings (id_word, value) values (?, ?)");
        statement2.setInt(1, x);
        statement2.setString(2, meaning);
        statement2.executeUpdate();

        result.close();
        statement.close();
        statement2.close();
    }


    public void insertWord(String word, String meaning, String summary) throws SQLException {
        PreparedStatement statement = conexion.prepareStatement("insert into words (word, meaning, summary) values (?, ?, ?)");
        statement.setString(1, word);
        statement.setString(2, meaning);
        statement.setString(3, summary);
        statement.executeUpdate();
        statement.close();
    }

    public void close() throws SQLException {
        conexion.close();
    }

}
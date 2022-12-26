/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.extension.web.login;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.extension.web.chat.Plugin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AccountDatabase {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected final String tableName;
    protected final String uuidColumnName;
    protected final String nameColumnName;
    protected final String codeColumnName;
    protected final String dateColumnName;
    private final String user;
    private final String password;
    protected String name;
    protected Connection connection;
    protected String url;

    public AccountDatabase(String name, String url, String user, String password, String tableName,
                           String uuidColumnName, String nameColumnName, String codeColumnName, String dateColumnName) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.name = name;
        this.tableName = tableName;
        this.uuidColumnName = uuidColumnName;
        this.nameColumnName = nameColumnName;
        this.codeColumnName = codeColumnName;
        this.dateColumnName = dateColumnName;
        Network.runTaskAsync(this::connect);
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public void connect() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            DriverManager.registerDriver(new org.mariadb.jdbc.Driver());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(this.getUrl(), this.getUser(), this.getPassword());
        } catch (SQLNonTransientConnectionException e) {
            Network.printWarning(Plugin.WEB, "Can not connect to web login database");
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Network.printText(Plugin.WEB, "Connected to web database");
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ignored) {
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isConnected() {
        try {
            return this.connection != null && !this.connection.isClosed();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public boolean addEntry(UUID uuid, String name, String code) {
        try {
            PreparedStatement ps =
                    this.connection.prepareStatement("INSERT INTO " + this.tableName + " (" + this.uuidColumnName +
                            ", " + this.nameColumnName + ", " + this.codeColumnName + ", " + this.dateColumnName + ")" +
                            "VALUES (\"" + uuid.toString().replace("-", "") +
                            "\", \"" + name + "\", \"" + code + "\", \"" + DATE_FORMAT.format(new Date()) + "\");");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean isEntryExisting(UUID uuid) {
        try {
            ResultSet rs =
                    this.connection.prepareStatement("SELECT " + this.uuidColumnName + " FROM " + this.tableName + " " +
                            "WHERE " + this.uuidColumnName + "=\"" + uuid.toString().replace("-", "") + "\";").executeQuery();
            if (rs.next()) {
                return rs.getString(this.uuidColumnName) != null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateEntry(UUID uuid, String name, String code) {
        try {
            PreparedStatement ps =
                    this.connection.prepareStatement("DELETE FROM " + tableName + " WHERE " + this.uuidColumnName +
                            "=\"" + uuid.toString().replace("-", "") + "\";");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this.addEntry(uuid, name, code);
    }

}

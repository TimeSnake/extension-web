/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.web.login;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.database.core.Column;
import de.timesnake.database.core.ColumnType;
import de.timesnake.database.core.DatabaseManager;
import de.timesnake.database.util.object.DatabaseConnector;
import de.timesnake.library.basic.util.Loggers;

import java.sql.SQLException;
import java.util.UUID;

public class AccountDatabase extends DatabaseConnector {

  protected final String tableName;
  protected final VerificationTable verificationTable;

  public AccountDatabase(String name, String url, String user, String password, String tableName,
                         String uuidColumnName, String nameColumnName, String codeColumnName) {
    super(name, url, "", user, password);
    this.tableName = tableName;

    this.verificationTable = new VerificationTable(this, tableName,
        new Column<>(uuidColumnName, ColumnType.UUID),
        new Column<>(nameColumnName, ColumnType.VARCHAR(16)),
        new Column<>(codeColumnName, ColumnType.VARCHAR(255))
    );

    Network.runTaskAsync(this::connect);
  }

  @Override
  public void connect() {
    DatabaseManager.loadDrivers();

    try {
      super.connect();
    } catch (SQLException e) {
      Loggers.WEB.warning("Can not connect to web login database: " + e.getMessage());
      return;
    }

    this.verificationTable.loadFunctions();

    Loggers.WEB.info("Connected to web database");
  }

  public void addUser(UUID uuid, String name, String code) {
    this.verificationTable.addUser(uuid, name, code);
  }

  public boolean containsUser(UUID uuid) {
    return this.verificationTable.containsUser(uuid);
  }

}

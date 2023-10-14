/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.web.login;

import de.timesnake.database.core.Column;
import de.timesnake.database.core.Entry;
import de.timesnake.database.core.table.TableDDL;
import de.timesnake.database.util.object.DatabaseConnector;

import java.util.Set;
import java.util.UUID;

public class VerificationTable extends TableDDL {

  private final Column<UUID> uuidColumn;
  private final Column<String> nameColumn;
  private final Column<String> codeColumn;

  protected VerificationTable(DatabaseConnector databaseConnector, String tableName, Column<UUID> uuidColumn,
                              Column<String> nameColumn, Column<String> codeColumn) {
    super(databaseConnector, tableName, false, uuidColumn);

    this.uuidColumn = uuidColumn;
    this.nameColumn = nameColumn;
    this.codeColumn = codeColumn;

    this.addColumn(nameColumn);
    this.addColumn(codeColumn);
    this.setUpdatePolicy(UpdatePolicy.INSERT_IF_NOT_EXISTS);
  }

  @Override
  public void loadFunctions() {
    super.loadFunctions();
  }

  public void addUser(UUID uuid, String name, String code) {
    super.set(Set.of(new Entry<>(name, this.nameColumn), new Entry<>(code, this.codeColumn)),
        new Entry<>(uuid, this.uuidColumn));
  }

  public boolean containsUser(UUID uuid) {
    return super.getFirst(this.nameColumn, new Entry<>(uuid, this.uuidColumn)) != null;
  }
}

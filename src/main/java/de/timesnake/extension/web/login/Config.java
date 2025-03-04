/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.web.login;

import de.timesnake.basic.proxy.util.file.ExFile;

public class Config extends ExFile {

  public Config() {
    super("extension-web", System.getenv("config.toml"));
  }

  public String getDatabaseName() {
    return this.getString("database.name", System.getenv("TS_WEB_DATABASE_NAME"));
  }

  public String getDatabaseUrl() {
    return this.getString("database.url", System.getenv("TS_WEB_DATABASE_URL"));
  }

  public String getDatabaseUser() {
    return this.getString("database.user", System.getenv("TS_WEB_DATABASE_USER"));
  }

  public String getDatabasePassword() {
    return this.getString("database.password", System.getenv("TS_WEB_DATABASE_PASSWORD"));
  }

  public String getDatabaseTableName() {
    return this.getString("database.tables.verification", System.getenv("TS_WEB_DATABASE_VERIFICATION_TABLE"));
  }

  public String getDatabaseUuidColumnName() {
    return super.getString("database.column.uuid", "uuid");
  }

  public String getDatabaseNameColumnName() {
    return super.getString("database.column.name", "username");
  }

  public String getDatabaseCodeColumnName() {
    return super.getString("database.column.code", "code");
  }

  public Integer getVerificationCodeLength() {
    return super.getLong("verification_code.length", 32L).intValue();
  }

  public boolean isEnabled() {
    return super.getBoolean("enabled", Boolean.valueOf(System.getenv("TS_WEB_ENABLED")));
  }
}

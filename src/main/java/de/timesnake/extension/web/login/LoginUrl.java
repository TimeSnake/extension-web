/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.web.login;

import java.util.UUID;

public class LoginUrl {

  public static final String URL = "https://timesnake.de/signup?";
  public static final String VERIFICATION = "code=";
  public static final String UUID = "uuid=";
  public static final String NAME = "name=";
  public static final String SPLITTER = "&";

  private final String code;
  private final UUID uuid;
  private final String name;
  private final boolean oldCode;

  public LoginUrl(String code, UUID uuid, String name, boolean oldCode) {
    this.code = code;
    this.uuid = uuid;
    this.name = name;
    this.oldCode = oldCode;
  }

  public String getCode() {
    return code;
  }

  public java.util.UUID getUuid() {
    return uuid;
  }

  public String getName() {
    return name;
  }

  public boolean expiredOldCode() {
    return oldCode;
  }

  public String getUrl() {
    return String.join(SPLITTER,
        URL + VERIFICATION + this.code,
        UUID + this.uuid.toString().replace("-", ""),
        NAME + this.name);
  }
}

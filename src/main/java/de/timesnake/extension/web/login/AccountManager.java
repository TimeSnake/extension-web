/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.web.login;


import de.timesnake.basic.proxy.util.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountManager {

  public static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
      'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
      't', 'u', 'v', 'w', 'x', 'y', 'z',
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
      'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

  private final Logger logger = LogManager.getLogger("web.manager");

  private final AccountDatabase database;

  private final Integer verificationCodeLength;

  public AccountManager() {
    Config config = new Config();
    this.logger.info("Loaded account config");

    if (config.isEnabled()) {
      this.database = new AccountDatabase(config.getDatabaseName(),
          config.getDatabaseUrl(),
          config.getDatabaseUser(), config.getDatabasePassword(),
          config.getDatabaseTableName(), config.getDatabaseUuidColumnName(),
          config.getDatabaseNameColumnName(),
          config.getDatabaseCodeColumnName());
    } else {
      this.database = null;
    }

    this.verificationCodeLength = config.getVerificationCodeLength();
  }

  public LoginUrl registerUser(User user) {
    this.logger.info("User {} requested a code", user.getName());
    String code = this.generateCode();
    boolean oldCode = false;

    if (this.database == null) {
      return null;
    }

    if (!this.database.isConnected()) {
      return null;
    }

    if (this.database.containsUser(user.getUniqueId())) {
      oldCode = true;
    }

    this.database.addUser(user.getUniqueId(), user.getName(), code);


    this.logger.info("User {} register code: {}", user.getName(), code);
    return new LoginUrl(code, user.getUniqueId(), user.getName(), oldCode);
  }

  private String generateCode() {
    StringBuilder code = new StringBuilder();
    for (int i = 0; i < verificationCodeLength; i++) {
      code.append(CHARS[((int) (Math.random() * CHARS.length))]);
    }
    return code.toString();
  }
}

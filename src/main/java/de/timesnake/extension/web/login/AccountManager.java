/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.web.login;


import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.extension.web.chat.Plugin;

public class AccountManager {

    public static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
            'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    private final AccountDatabase database;
    private final Config config;

    private final Integer verificationCodeLength;

    public AccountManager() {
        this.config = new Config();
        Network.printText(Plugin.WEB, "Loaded account config");

        if (this.config.isEnabled()) {
            this.database = new AccountDatabase(this.config.getDatabaseName(),
                    this.config.getDatabaseUrl(),
                    this.config.getDatabaseUser(), this.config.getDatabasePassword(),
                    this.config.getDatabaseTableName(), this.config.getDatabaseUuidColumnName(),
                    this.config.getDatabaseNameColumnName(),
                    this.config.getDatabaseCodeColumnName());
        } else {
            this.database = null;
        }

        this.verificationCodeLength = this.config.getVerifcationCodeLength();
    }

    public LoginUrl registerUser(User user) {
        Network.printText(Plugin.WEB, "User " + user.getName() + " requested a code", "Register");
        String code = this.generateCode();
        boolean successfully;
        boolean oldCode = false;

        if (this.database == null) {
            return null;
        }

        if (!this.database.isConnected()) {
            return null;
        }

        if (this.database.isEntryExisting(user.getUniqueId())) {
            oldCode = true;
        }
        successfully = this.database.addEntry(user.getUniqueId(), user.getName(), code);

        if (!successfully) {
            Network.printWarning(Plugin.WEB,
                    "Failed to set code in database, user " + user.getName(), "Register");
            return null;
        }
        Network.printText(Plugin.WEB, "User " + user.getName() + " register code: " + code,
                "Register");
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

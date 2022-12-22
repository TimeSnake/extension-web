/*
 * de.timesnake.workspace.extension-web.main
 * Copyright (C) 2022 timesnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
 */

package de.timesnake.extension.web.login;

import de.timesnake.basic.proxy.util.file.ExFile;

public class Config extends ExFile {

    private static final String DATABASE_NAME = "database.name";
    private static final String DATABASE_URL = "database.url";
    private static final String DATABASE_USER = "database.user";
    private static final String DATABASE_PASSWORD = "database.password";
    private static final String DATABASE_TABLE_NAME = "database.table";
    private static final String DATABASE_UUID_COLUMN_NAME = "database.column.uuid";
    private static final String DATABASE_NAME_COLUMN_NAME = "database.column.name";
    private static final String DATABASE_CODE_COLUMN_NAME = "database.column.code";
    private static final String DATABASE_DATE_COLUMN_NAME = "database.column.date";

    private static final String ENABLED = "enabled";

    private static final String VERIFICATION_CODE_LENGTH = "verification_code.length";

    public Config() {
        super("extension-web", "config.toml");
    }

    public String getDatabaseName() {
        return super.getString(DATABASE_NAME);
    }

    public String getDatabaseUrl() {
        return super.getString(DATABASE_URL);
    }

    public String getDatabaseUser() {
        return super.getString(DATABASE_USER);
    }

    public String getDatabasePassword() {
        return super.getString(DATABASE_PASSWORD);
    }

    public String getDatabaseTableName() {
        return super.getString(DATABASE_TABLE_NAME);
    }

    public String getDatabaseUuidColumnName() {
        return super.getString(DATABASE_UUID_COLUMN_NAME);
    }

    public String getDatabaseNameColumnName() {
        return super.getString(DATABASE_NAME_COLUMN_NAME);
    }

    public String getDatabaseCodeColumnName() {
        return super.getString(DATABASE_CODE_COLUMN_NAME);
    }

    public Integer getVerifcationCodeLength() {
        return super.getLong(VERIFICATION_CODE_LENGTH).intValue();
    }

    public String getDatabaseDateColumnName() {
        return super.getString(DATABASE_DATE_COLUMN_NAME);
    }

    public boolean isEnabled() {
        return super.getBoolean(ENABLED);
    }
}

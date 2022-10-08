/*
 * extension-web.main
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

import java.util.UUID;

public class LoginUrl {

    public static final String URL = "http://timesnake.de/signup?";
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
        return URL + VERIFICATION + this.code + SPLITTER + UUID + this.uuid.toString().replace("-", "") + SPLITTER + NAME + this.name;
    }
}

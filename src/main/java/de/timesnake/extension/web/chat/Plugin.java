/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.extension.web.chat;

import de.timesnake.library.basic.util.LogHelper;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Plugin extends de.timesnake.basic.proxy.util.chat.Plugin {

    public static final Plugin WEB = new Plugin("Web", "XWB", LogHelper.getLogger("Web", Level.INFO));

    protected Plugin(String name, String code, Logger logger) {
        super(name, code, logger);
    }
}

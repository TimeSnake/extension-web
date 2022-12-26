/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.extension.web.main;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.timesnake.basic.proxy.util.Network;
import de.timesnake.extension.web.login.AccountManager;
import de.timesnake.extension.web.login.LoginCmd;
import java.util.logging.Logger;

@Plugin(id = "extension-web", name = "ExWeb", version = "1.0-SNAPSHOT",
        url = "https://git.timesnake.de", authors = {"MarkusNils"},
        dependencies = {
                @Dependency(id = "basic-proxy")
        })
public class ExWeb {

    public static AccountManager getAccountManager() {
        return accountManager;
    }

    public static ExWeb getPlugin() {
        return plugin;
    }

    private static ExWeb plugin;
    private static AccountManager accountManager;
    private static ProxyServer server;
    private static Logger logger;

    @Inject
    public ExWeb(ProxyServer server, Logger logger) {
        ExWeb.server = server;
        ExWeb.logger = logger;
        plugin = this;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        accountManager = new AccountManager();

        Network.getCommandHandler().addCommand(this, "web", new LoginCmd(), de.timesnake.extension.web.chat.Plugin.WEB);

    }
}

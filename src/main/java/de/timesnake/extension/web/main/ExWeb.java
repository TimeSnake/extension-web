package de.timesnake.extension.web.main;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.extension.web.login.AccountManager;
import de.timesnake.extension.web.login.LoginCmd;
import net.md_5.bungee.api.plugin.Plugin;

public class ExWeb extends Plugin {

    private static ExWeb plugin;

    private static AccountManager accountManager;

    @Override
    public void onEnable() {

        plugin = this;

        accountManager = new AccountManager();

        Network.getCommandHandler().addCommand(this, this.getProxy().getPluginManager(), "web", new LoginCmd(),
                de.timesnake.extension.web.chat.Plugin.WEB);

    }

    public static AccountManager getAccountManager() {
        return accountManager;
    }

    public static ExWeb getPlugin() {
        return plugin;
    }
}

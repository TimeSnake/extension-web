package de.timesnake.extension.web.login;

import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.extension.web.chat.Plugin;
import de.timesnake.extension.web.main.ExWeb;
import de.timesnake.library.basic.util.chat.ChatColor;
import de.timesnake.library.extension.util.chat.Chat;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

import java.util.List;

public class LoginCmd implements CommandListener<Sender, Argument> {

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (!sender.isPlayer(true)) {
            return;
        }

        if (!args.isLengthHigherEquals(1, true)) {
            return;
        }

        if (!sender.hasPermission("exweb.login", 0)) {
            return;
        }

        User user = sender.getUser();

        if ("login".equalsIgnoreCase(args.getString(0))) {
            LoginUrl url = ExWeb.getAccountManager().registerUser(user);

            if (url != null) {
                if (url.expiredOldCode()) {
                    sender.sendPluginMessage(ChatColor.PERSONAL + "Your old link is now expired");
                }
                TextComponent link = Component.text(Chat.getSenderPlugin(Plugin.WEB) + ChatColor.PERSONAL +
                        ChatColor.UNDERLINE + "Click here" + ChatColor.PERSONAL + " to open your personal login link");
                link.hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                        Component.text("Click to open the link, " + ChatColor.WARNING + "do NOT share with others")));
                link.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, url.getUrl()));
                sender.getPlayer().sendMessage(link);
            } else {
                sender.sendPluginMessage(ChatColor.WARNING + "Error during login. Please contact an admin!");
            }
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.getLength() == 1) {
            return List.of("login");
        }
        return null;
    }
}

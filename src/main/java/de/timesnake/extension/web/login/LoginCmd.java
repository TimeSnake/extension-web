package de.timesnake.extension.web.login;

import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.extension.web.chat.Plugin;
import de.timesnake.extension.web.main.ExWeb;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Chat;
import de.timesnake.library.extension.util.chat.Code;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.List;

public class LoginCmd implements CommandListener<Sender, Argument> {

    private Code.Permission perm;

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (!sender.isPlayer(true)) {
            return;
        }

        if (!args.isLengthHigherEquals(1, true)) {
            return;
        }

        if (!sender.hasPermission(this.perm)) {
            return;
        }

        User user = sender.getUser();

        if ("login".equalsIgnoreCase(args.getString(0))) {
            LoginUrl url = ExWeb.getAccountManager().registerUser(user);

            if (url != null) {
                if (url.expiredOldCode()) {
                    sender.sendPluginMessage(Component.text("Your old link is now expired", ExTextColor.WARNING));
                }
                sender.getPlayer().sendMessage(Chat.getSenderPlugin(Plugin.WEB)
                        .append(Component.text("Click here", ExTextColor.PERSONAL, TextDecoration.UNDERLINED))
                        .append(Component.text(" to open your personal login link", ExTextColor.PERSONAL))
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.text("Click to open the link, do NOT share with others", ExTextColor.WARNING)))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, url.getUrl())));
            } else {
                sender.sendPluginMessage(Component.text("Error during login. Please contact an admin!", ExTextColor.WARNING));
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

    @Override
    public void loadCodes(de.timesnake.library.extension.util.chat.Plugin plugin) {
        this.perm = plugin.createPermssionCode("web", "exweb.login");
    }
}

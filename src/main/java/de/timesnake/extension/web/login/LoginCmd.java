package de.timesnake.extension.web.login;

import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.ChatColor;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.extension.web.chat.Plugin;
import de.timesnake.extension.web.main.ExWeb;
import de.timesnake.library.extension.util.chat.Chat;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

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

        if ("login".equals(args.getString(0).toLowerCase())) {
            LoginUrl url = ExWeb.getAccountManager().registerUser(user);

            if (url != null) {
                if (url.expiredOldCode()) {
                    sender.sendPluginMessage(ChatColor.PERSONAL + "Your old code is now expired");
                }
                TextComponent link = new TextComponent();
                link.setText(Chat.getSenderPlugin(Plugin.WEB) + ChatColor.PERSONAL + ChatColor.UNDERLINE + "Click here" + ChatColor.PERSONAL + " to open your personal login link");
                link.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to open the link, " + ChatColor.WARNING + "do NOT share with others")));
                link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url.getUrl()));
                sender.getPlayer().sendMessage(link);

                TextComponent code = new TextComponent();
                code.setText(Chat.getSenderPlugin(Plugin.WEB) + ChatColor.PERSONAL + "Code: " + ChatColor.VALUE + url.getCode());
                code.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to copy to clipboard, " + ChatColor.WARNING + "do NOT share with others")));
                code.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, url.getCode()));
                sender.getPlayer().sendMessage(code);
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

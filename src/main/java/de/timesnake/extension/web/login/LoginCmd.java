/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.web.login;

import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.CommandListener;
import de.timesnake.basic.proxy.util.chat.Completion;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.extension.web.main.ExWeb;
import de.timesnake.library.chat.Chat;
import de.timesnake.library.chat.Code;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.chat.Plugin;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;

public class LoginCmd implements CommandListener {

  private final Code perm = Plugin.SERVER.createPermssionCode("exweb.login");

  @Override
  public void onCommand(Sender sender, PluginCommand cmd, Arguments<Argument> args) {
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
          sender.sendPluginMessage(
              Component.text("Your old link is now expired", ExTextColor.WARNING));
        }
        sender.getPlayer().sendMessage(Chat.getSenderPlugin(Plugin.SERVER)
            .append(Component.text("Click here", ExTextColor.PERSONAL,
                TextDecoration.UNDERLINED))
            .append(Component.text(" to open your personal login link",
                ExTextColor.PERSONAL))
            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                Component.text("Click to open the link, do NOT share with others",
                    ExTextColor.WARNING)))
            .clickEvent(
                ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, url.getUrl())));
      } else {
        sender.sendPluginMessage(
            Component.text("Error during login. Please contact an admin!",
                ExTextColor.WARNING));
      }
    }
  }

  @Override
  public Completion getTabCompletion() {
    return new Completion(this.perm)
        .addArgument(new Completion("login"));
  }

  @Override
  public String getPermission() {
    return this.perm.getPermission();
  }
}

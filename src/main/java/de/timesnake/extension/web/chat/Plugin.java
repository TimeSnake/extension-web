/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.web.chat;

public class Plugin extends de.timesnake.basic.proxy.util.chat.Plugin {

  public static final Plugin WEB = new Plugin("Web", "XWB");

  protected Plugin(String name, String code) {
    super(name, code);
  }
}

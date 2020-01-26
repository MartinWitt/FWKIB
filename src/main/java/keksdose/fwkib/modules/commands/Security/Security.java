package keksdose.fwkib.modules.commands.security;

import keksdose.fwkib.modules.Command;

public class Security implements Command {

  private String securityFeatures = "use:uuid rsagen-pub rsagen-pri pwgen and hash";

  @Override
  public String apply(String message) {
    return securityFeatures;
  }

  @Override
  public String help(String message) {
    return "zeigt dir welche tollen Cryptofeature fwkib bugfrei/bugvoll implementiert hat.";
  }
}

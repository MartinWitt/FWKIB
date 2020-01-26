package keksdose.fwkib.bot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * ConfigReader
 */
public enum ConfigReader {

  CONFIG("config.properties");

  private ConfigReader(String configName) {
    this.prop = new Properties();
    this.configName = configName;
    loadConfig();
  }

  private void loadConfig() {
    try {
      prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(configName));
    } catch (IOException e) {
      Logger.getLogger("fwkib").warning(e.getLocalizedMessage());
    }
  }

  private final Properties prop;
  private final String configName;

  public static Collection<String> getAdmins() {
    return Arrays.asList(CONFIG.prop.getProperty("admin", "").split(","));
  }

  public static String convertHostnameToString(String hostname) {
    return CONFIG.prop.getProperty(hostname, "");
  }

  public static Collection<String> getBots() {
    return Arrays.asList(CONFIG.prop.getProperty("botNicks", "").toLowerCase().split(","));
  }

  public static void reloadAll() {
    Arrays.stream(ConfigReader.values()).forEach(ConfigReader::loadConfig);;
  }
}

package oh_heaven.game;

import utility.PropertiesLoader;

import java.util.Properties;

public class GameInitializer {
    public static void main(String[] args) {
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Properties properties = null;
        if (args == null || args.length == 0) {
            properties = PropertiesLoader.loadPropertiesFile(null);
        }
        else {
            properties = PropertiesLoader.loadPropertiesFile(args[0]);
        }
        new Oh_Heaven(properties);
    }
}

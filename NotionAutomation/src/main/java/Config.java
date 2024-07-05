import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Properties properties = new Properties();
    private static boolean isLoaded = false;

    static {
        try {
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

            // Cargar el archivo de propiedades
            try (InputStream input = Config.class.getClassLoader().getResourceAsStream("application.properties")) {
                if (input == null) {
                    System.out.println("Unable to find application.properties");
                } else {
                    properties.load(input);
                    isLoaded = true;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            dotenv.entries().forEach(entry -> {
                String key = entry.getKey().toLowerCase().replace('_', '.');
                properties.setProperty(key, entry.getValue());
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        if (isLoaded) {
            return properties.getProperty(key);
        } else {
            throw new IllegalStateException("Properties file not loaded");
        }
    }
}

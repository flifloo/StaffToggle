package fr.flifloo.StaffToggle.Configurations;

import fr.flifloo.StaffToggle.Main;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Language extends Configuration{

    public Language (Main main) {
        super(main, "language", "languages/" + main.getConfig().getString("language") + ".yml");

        if (!main.getConfig().getString("language").equalsIgnoreCase(getConfig().getString("language"))) {
            try {
                Files.copy(getResourceFile(), getConfigFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
                getConfig().load(getConfigFile());
            } catch (IOException | InvalidConfigurationException  e) {
                e.printStackTrace();
            }
        }

    }
}

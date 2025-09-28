package de.example.homesystem;

import com.earth2me.essentials.Essentials;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class HomeSystemPlugin extends JavaPlugin {

    private Essentials essentials;
    private HomeViewManager viewManager;

    @Override
    public void onEnable() {
        Plugin essentialsPlugin = getServer().getPluginManager().getPlugin("Essentials");
        if (!(essentialsPlugin instanceof Essentials)) {
            getLogger().log(Level.SEVERE, "EssentialsX konnte nicht gefunden werden. Bitte installiere EssentialsX.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.essentials = (Essentials) essentialsPlugin;

        this.viewManager = new HomeViewManager(essentials);

        HomeCommand homeCommand = new HomeCommand(viewManager);
        registerCommand("home", homeCommand);
        registerCommand("homes", homeCommand);

        Bukkit.getPluginManager().registerEvents(new HomeMenuListener(viewManager), this);

        getLogger().info("HomeSystem wurde erfolgreich aktiviert.");
    }

    private void registerCommand(String name, HomeCommand executor) {
        PluginCommand command = getCommand(name);
        if (command == null) {
            getLogger().log(Level.SEVERE, "Befehl /" + name + " konnte nicht registriert werden.");
            return;
        }
        command.setExecutor(executor);
        command.setTabCompleter(executor);
    }

    public Essentials getEssentials() {
        return essentials;
    }

    public HomeViewManager getViewManager() {
        return viewManager;
    }
}

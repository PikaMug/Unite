package me.pikamug.unite;

import me.pikamug.unite.api.objects.PartyProvider;
import me.pikamug.unite.api.objects.plugins.PartyProvider_BetterTeams;
import me.pikamug.unite.api.objects.plugins.PartyProvider_DungeonsXL;
import me.pikamug.unite.api.objects.plugins.PartyProvider_PAF;
import me.pikamug.unite.api.objects.plugins.PartyProvider_PAFGUI;
import me.pikamug.unite.api.objects.plugins.PartyProvider_Parties;
import me.pikamug.unite.api.objects.plugins.PartyProvider_SimpleClans;
import me.pikamug.unite.api.objects.plugins.PartyProvider_mcMMO;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.logging.Logger;

public class Unite extends JavaPlugin {
    private static Unite instance;
    private static Logger log;
    private ServicesManager servicesManager;

    @Override
    public void onEnable() {
        instance = this;
        log = this.getLogger();
        servicesManager = getServer().getServicesManager();

        hookProviders();

        Objects.requireNonNull(getCommand("unite")).setExecutor(this);
    }

    @Override
    public void onDisable() {
        getServer().getServicesManager().unregisterAll(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String commandLabel, String[] args) {
        if (!sender.hasPermission("unite.admin")) {
            sender.sendMessage("You do not have permission to use that command!");
            return true;
        }

        sender.sendMessage("Unite v" + this.getDescription().getVersion() + " Commands:");
        sender.sendMessage("  /unite - Displays information about Unite");
        return true;
    }

    public static Unite getInstance() {
        return instance;
    }

    public void hookProviders() {
        hookProvider("Parties", PartyProvider_Parties.class, ServicePriority.Highest, "com.alessiodp.parties.api.interfaces.PartiesAPI");
        hookProvider("BetterTeams", PartyProvider_BetterTeams.class, ServicePriority.Normal, "com.booksaw.betterTeams.Main");
        hookProvider("DungeonsXL", PartyProvider_DungeonsXL.class, ServicePriority.Normal, "de.erethon.dungeonsxl.DungeonsXL");
        hookProvider("mcMMO", PartyProvider_mcMMO.class, ServicePriority.High, "com.gmail.nossr50.party.PartyManager");
        hookProvider("PartyAndFriends", PartyProvider_PAF.class, ServicePriority.Normal, "de.simonsator.partyandfriends.main.PAFPlugin");
        hookProvider("PartyAndFriendsGUI",  PartyProvider_PAFGUI.class, ServicePriority.High, "de.simonsator.partyandfriendsgui.PAFGUIPlugin");
        hookProvider("SimpleClans", PartyProvider_SimpleClans.class, ServicePriority.Normal, "net.sacredlabyrinth.phaed.simpleclans.SimpleClans");
    }

    private void hookProvider(String name, Class<? extends PartyProvider> hookClass, ServicePriority priority, String...packages) {
        try {
            if (packagesExists(packages)) {
                PartyProvider partyProvider = hookClass.getConstructor(Plugin.class).newInstance(this);
                servicesManager.register(PartyProvider.class, partyProvider, this, priority);
                log.info(String.format("[Party] %s found: %s", name, partyProvider.isPluginEnabled() ? "Loaded" : "Waiting"));
            }
        } catch (Exception e) {
            log.severe(String.format("[Party] There was an error hooking %s - check to make sure you're using a compatible version!", name));
            e.printStackTrace();
        }
    }

    /**
     * Determines if all packages in a String array are within the classpath.
     * @param packages String array of package names to check
     * @return true if successful
     */
    private static boolean packagesExists(String...packages) {
        try {
            for (String pkg : packages) {
                Class.forName(pkg);
            }
            return true;
        } catch (NoClassDefFoundError | ClassNotFoundException e) {
            return false;
        }
    }
}

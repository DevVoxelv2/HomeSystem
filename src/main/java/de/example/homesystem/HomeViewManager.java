package de.example.homesystem;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class HomeViewManager {

    private final Essentials essentials;
    private final Map<UUID, Inventory> openMenus = new HashMap<>();

    public HomeViewManager(Essentials essentials) {
        this.essentials = essentials;
    }

    public void openHomeMenu(Player player) {
        User user = essentials.getUser(player);
        if (user == null) {
            player.sendMessage("§cDeine Daten konnten nicht aus Essentials geladen werden.");
            return;
        }

        Set<String> homes = user.getHomes();
        if (homes.isEmpty()) {
            player.sendMessage("§eDu hast noch keine Homes gesetzt.");
            return;
        }

        List<String> sortedHomes = new ArrayList<>(homes);
        Collections.sort(sortedHomes, String.CASE_INSENSITIVE_ORDER);

        int size = ((sortedHomes.size() - 1) / 9 + 1) * 9;
        size = Math.min(Math.max(size, 9), 54);
        Inventory inventory = Bukkit.createInventory(player, size, "§8§lDeine Homes");

        for (int i = 0; i < sortedHomes.size() && i < size; i++) {
            String homeName = sortedHomes.get(i);
            ItemStack item = createHomeItem(homeName, user);
            inventory.setItem(i, item);
        }

        openMenus.put(player.getUniqueId(), inventory);
        player.openInventory(inventory);
    }

    private ItemStack createHomeItem(String homeName, User user) {
        ItemStack item = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§b" + homeName);
            List<String> lore = new ArrayList<>();
            try {
                Location location = user.getHome(homeName);
                lore.add(String.format("§7Welt: §f%s", location.getWorld() != null ? location.getWorld().getName() : "Unbekannt"));
                lore.add(String.format("§7X: §f%.1f §7Y: §f%.1f §7Z: §f%.1f", location.getX(), location.getY(), location.getZ()));
            } catch (Exception ignored) {
                lore.add("§cOrt konnte nicht geladen werden.");
            }
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.values());
            item.setItemMeta(meta);
        }
        return item;
    }

    public void handleMenuClick(Player player, int slot) {
        Inventory inventory = openMenus.get(player.getUniqueId());
        if (inventory == null) {
            return;
        }

        if (slot < 0 || slot >= inventory.getSize()) {
            return;
        }

        ItemStack item = inventory.getItem(slot);
        if (item == null || !item.hasItemMeta() || item.getItemMeta() == null || !item.getItemMeta().hasDisplayName()) {
            return;
        }

        String displayName = item.getItemMeta().getDisplayName();
        String homeName = displayName.replace("§b", "").trim();

        User user = essentials.getUser(player);
        if (user == null) {
            player.sendMessage("§cEssentials Daten nicht gefunden.");
            return;
        }

        try {
            Location homeLocation = user.getHome(homeName);
            if (homeLocation == null) {
                player.sendMessage("§cDieses Home existiert nicht mehr.");
                return;
            }
            player.closeInventory();
            player.teleport(homeLocation);
            player.sendMessage("§aTeleportiere zu §f" + homeName + "§a.");
        } catch (InvalidWorldException e) {
            player.sendMessage("§cDie Welt für dieses Home existiert nicht.");
        } catch (Exception e) {
            player.sendMessage("§cTeleport fehlgeschlagen: " + e.getMessage());
        }
    }

    public boolean isHomeMenu(Player player, Inventory inventory) {
        if (inventory == null) {
            return false;
        }
        Inventory openMenu = openMenus.get(player.getUniqueId());
        return openMenu != null && openMenu.equals(inventory);
    }

    public void closeMenu(Player player) {
        openMenus.remove(player.getUniqueId());
    }
}

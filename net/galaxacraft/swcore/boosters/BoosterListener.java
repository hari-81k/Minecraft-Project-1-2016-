package net.galaxacraft.swcore.boosters;

import org.bukkit.Material;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
/*    */ import org.bukkit.event.inventory.InventoryType.SlotType;
/*    */ import org.bukkit.inventory.Inventory;
/*    */ import org.bukkit.inventory.ItemStack;

import net.galaxacraft.swcore.boosters.BoosterInventory;
import net.galaxacraft.swcore.boosters.BoosterType;
import net.galaxacraft.swcore.boosters.BoosterUtil;

public class BoosterListener implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if ((event.getWhoClicked() instanceof Player)) {
            Player player = (Player) event.getWhoClicked();
            if (event.getInventory().getTitle().contains("'s boosters")) {
                event.setCancelled(true);
                if (event.getSlotType() != InventoryType.SlotType.OUTSIDE) {
                    ItemStack currentItem = event.getCurrentItem();
                    if ((currentItem.getType() != Material.STAINED_GLASS_PANE) && (currentItem.getType() != Material.AIR) && (currentItem.getType() != Material.COMMAND)) {
                        BoosterType type = BoosterType.getByItem(currentItem.getType());
                        player.closeInventory();
                        player.openInventory(BoosterInventory.get().getAgree(type));
                    } else if (currentItem.getType() == Material.COMMAND) {
                        player.closeInventory();
                    }
                }
            } else if (event.getInventory().getTitle().contains("Are you sure")) {
                event.setCancelled(true);
                if (event.getSlotType() != InventoryType.SlotType.OUTSIDE) {
                    ItemStack currentItem = event.getCurrentItem();
                    if (currentItem.getType() == Material.WOOL) {
                        BoosterType type = BoosterType.getByItem(event.getInventory().getItem(4).getType());
                        if (currentItem.getDurability() == 13) {
                            BoosterUtil.get().removeBooster(player.getUniqueId(), type);
                            BoosterUtil.get().createBooster(type, player);
                            player.closeInventory();
                        } else {
                            player.closeInventory();
                        }
                    }
                }
            }
        }
    }
}

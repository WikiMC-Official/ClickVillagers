/*
 * Copyright 2020-2025 Clickism
 * Released under the GNU General Public License 3.0.
 * See LICENSE.md for details.
 */

package de.clickism.clickvillagers.villager;

import de.clickism.clickvillagers.util.Utils;
import de.clickism.clickvillagers.util.LangManager;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TradeInfoProviders {

    public static final TradeInfoProvider ALL_TRADES = TradeInfoProvider.builder()
            .filterIngredients(item -> true)
            .filterResults(item -> true)
            .build();
    public static final TradeInfoProvider LIBRARIAN = TradeInfoProvider.builder()
            .acceptResults(Material.ENCHANTED_BOOK)
            .ingredientFormatter(item -> {
                if (item.getType() != Material.EMERALD) return null;
                return item.getAmount() + " Emerald";
            })
            .resultFormatter(TradeInfoProviders::formatEnchantedBook)
            .build();
    private static final Function<ItemStack, String> ITEM_FORMATTER = item -> {
        Material material = item.getType();
        String prefix = getPrefix(material);
        prefix = (prefix == null) ? "" : prefix + " ";
        boolean isTool = item.getType().getMaxDurability() != 0;
        String matId = material.name().toLowerCase();
        String showName = isTool
                ? LangManager.getItemName(matId)
                : item.getAmount() + " " + LangManager.getItemName(matId);
        return prefix + showName;
    };
    public static final TradeInfoProvider FARMER = TradeInfoProvider.builder()
            .acceptIngredients(Material.WHEAT, Material.BEETROOT, Material.CARROT, Material.POTATO, Material.PUMPKIN, Material.MELON)
            .acceptResults(Material.GOLDEN_CARROT, Material.APPLE, Material.BREAD)
            .singleFormatter(ITEM_FORMATTER)
            .build();

    public static final TradeInfoProvider SMITH = TradeInfoProvider.builder()
            .acceptIngredients(Material.COAL)
            .acceptResults(Material.DIAMOND_AXE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL,
                    Material.DIAMOND_HOE, Material.DIAMOND_SWORD, Material.DIAMOND_HELMET,
                    Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
                    Material.SHIELD)
            .singleFormatter(ITEM_FORMATTER)
            .formatEnchantments()
            .build();

    public static final TradeInfoProvider BUTCHER = TradeInfoProvider.builder()
            .acceptIngredients(Material.CHICKEN, Material.PORKCHOP, Material.RABBIT,
                    Material.SWEET_BERRIES, Material.KELP)
            .acceptResults(Material.COOKED_CHICKEN, Material.COOKED_PORKCHOP)
            .singleFormatter(ITEM_FORMATTER)
            .build();

    public static final TradeInfoProvider FISHERMAN = TradeInfoProvider.builder()
            .acceptIngredients(Material.STRING, Material.COAL)
            .acceptResults(Material.CAMPFIRE, Material.FISHING_ROD)
            .singleFormatter(ITEM_FORMATTER)
            .formatEnchantments()
            .build();

    public static final TradeInfoProvider LEATHERWORKER = TradeInfoProvider.builder()
            .acceptIngredients(Material.LEATHER, Material.FLINT, Material.RABBIT_HIDE, Material.TURTLE_SCUTE)
            .acceptResults(Material.LEATHER_HORSE_ARMOR, Material.SADDLE)
            .singleFormatter(ITEM_FORMATTER)
            .build();

    public static final TradeInfoProvider CLERIC = TradeInfoProvider.builder()
            .acceptIngredients(Material.GOLD_INGOT)
            .acceptResults(Material.REDSTONE, Material.LAPIS_LAZULI, Material.GLOWSTONE, Material.ENDER_PEARL,
                    Material.EXPERIENCE_BOTTLE)
            .singleFormatter(ITEM_FORMATTER)
            .build();

    public static final TradeInfoProvider CARTOGRAPHER = TradeInfoProvider.builder()
            .acceptIngredients(Material.PAPER)
            .acceptResults(Material.ITEM_FRAME, Material.MAP)
            .singleFormatter(ITEM_FORMATTER)
            .build();

    public static final TradeInfoProvider FLETCHER = TradeInfoProvider.builder()
            .acceptIngredients(Material.STICK, Material.FLINT, Material.STRING)
            .filterResults(item -> {
                Material material = item.getType();
                if (material == Material.ARROW) return true;
                if (material == Material.BOW) {
                    ItemMeta meta = item.getItemMeta();
                    return meta != null && !meta.getEnchants().isEmpty();
                }
                return false;
            })
            .singleFormatter(ITEM_FORMATTER)
            .formatEnchantments()
            .build();

    public static final TradeInfoProvider MASON = TradeInfoProvider.builder()
            .acceptIngredients(Material.CLAY_BALL, Material.QUARTZ)
            .acceptResults(Material.BRICK, Material.QUARTZ_PILLAR, Material.QUARTZ_BLOCK,
                    // Normal terracotta
                    Material.TERRACOTTA, Material.WHITE_TERRACOTTA,
                    Material.ORANGE_TERRACOTTA, Material.MAGENTA_TERRACOTTA, Material.LIGHT_BLUE_TERRACOTTA,
                    Material.YELLOW_TERRACOTTA, Material.LIME_TERRACOTTA, Material.PINK_TERRACOTTA,
                    Material.GRAY_TERRACOTTA, Material.LIGHT_GRAY_TERRACOTTA, Material.CYAN_TERRACOTTA,
                    Material.PURPLE_TERRACOTTA, Material.BLUE_TERRACOTTA, Material.BROWN_TERRACOTTA,
                    Material.GREEN_TERRACOTTA, Material.RED_TERRACOTTA, Material.BLACK_TERRACOTTA,
                    // Glazed terracotta
                    Material.WHITE_GLAZED_TERRACOTTA, Material.ORANGE_GLAZED_TERRACOTTA,
                    Material.MAGENTA_GLAZED_TERRACOTTA, Material.LIGHT_BLUE_GLAZED_TERRACOTTA,
                    Material.YELLOW_GLAZED_TERRACOTTA, Material.LIME_GLAZED_TERRACOTTA,
                    Material.PINK_GLAZED_TERRACOTTA, Material.GRAY_GLAZED_TERRACOTTA,
                    Material.LIGHT_GRAY_GLAZED_TERRACOTTA, Material.CYAN_GLAZED_TERRACOTTA,
                    Material.PURPLE_GLAZED_TERRACOTTA, Material.BLUE_GLAZED_TERRACOTTA,
                    Material.BROWN_GLAZED_TERRACOTTA, Material.GREEN_GLAZED_TERRACOTTA,
                    Material.RED_GLAZED_TERRACOTTA, Material.BLACK_GLAZED_TERRACOTTA)
            .ingredientFormatter(ITEM_FORMATTER)
            .resultFormatter(item -> {
                Material material = item.getType();
                if (material == Material.BRICK || material == Material.QUARTZ_PILLAR
                    || material == Material.QUARTZ_BLOCK || material == Material.EMERALD) {
                    return ITEM_FORMATTER.apply(item);
                }
                String matId = material.name().toLowerCase();
                return "&6🪨 " + item.getAmount() + " " + LangManager.getItemName(matId);
            })
            .build();

    public static final TradeInfoProvider SHEPHERD = TradeInfoProvider.builder()
            .acceptIngredients()
            .acceptIngredients(Material.WHITE_WOOL, Material.BROWN_WOOL, Material.BLACK_WOOL, Material.GRAY_WOOL)
            .acceptResults(Material.SHEARS, Material.PAINTING)
            .singleFormatter(ITEM_FORMATTER)
            .build();

    private static final Map<Villager.Profession, TradeInfoProvider> PROVIDERS = Map.ofEntries(
            Map.entry(Villager.Profession.LIBRARIAN, LIBRARIAN),
            Map.entry(Villager.Profession.FARMER, FARMER),
            Map.entry(Villager.Profession.TOOLSMITH, SMITH),
            Map.entry(Villager.Profession.WEAPONSMITH, SMITH),
            Map.entry(Villager.Profession.ARMORER, SMITH),
            Map.entry(Villager.Profession.BUTCHER, BUTCHER),
            Map.entry(Villager.Profession.FISHERMAN, FISHERMAN),
            Map.entry(Villager.Profession.LEATHERWORKER, LEATHERWORKER),
            Map.entry(Villager.Profession.CLERIC, CLERIC),
            Map.entry(Villager.Profession.CARTOGRAPHER, CARTOGRAPHER),
            Map.entry(Villager.Profession.FLETCHER, FLETCHER),
            Map.entry(Villager.Profession.MASON, MASON),
            Map.entry(Villager.Profession.SHEPHERD, SHEPHERD)
    );

    public static TradeInfoProvider getProvider(Villager.Profession profession) {
        var provider = PROVIDERS.get(profession);
        if (provider != null) {
            return provider;
        }
        return ALL_TRADES;
    }

    @Nullable
    private static String getPrefix(Material material) {
        return switch (material) {
            case ENCHANTED_BOOK -> "&d📖";

            case GOLDEN_CARROT -> "&e🥕";
            case APPLE -> "&c🍎";

            case WHEAT -> "&e🌾";
            case CARROT -> "&6🥕";
            case POTATO -> "&6🥔";
            case PUMPKIN -> "&6🎃";
            case MELON -> "&a🍉";

            case COAL,
                 FLINT -> "&8🪨";

            case DIAMOND_SWORD -> "&b🗡";
            case DIAMOND_AXE,
                 DIAMOND_PICKAXE,
                 DIAMOND_SHOVEL,
                 DIAMOND_HOE -> "&b⛏";
            case DIAMOND_HELMET,
                 DIAMOND_CHESTPLATE,
                 DIAMOND_LEGGINGS,
                 DIAMOND_BOOTS -> "&b👕";

            case CHICKEN -> "&e🐓";
            case PORKCHOP -> "&e🐷";
            case RABBIT -> "&e🐰";
            case SWEET_BERRIES -> "&c🍇";
            case KELP -> "&a🌿";

            case COOKED_CHICKEN -> "&6🐓";
            case COOKED_PORKCHOP -> "&6🐷";

            case STRING -> "&f🪡";
            case CAMPFIRE -> "&c🔥";
            case FISHING_ROD -> "&6🎣";

            case SHIELD -> "&6🛡️";

            case LEATHER -> "&6🐄";
            case RABBIT_HIDE -> "&e🐇";
            case TURTLE_SCUTE -> "&a🐢";
            case SADDLE -> "&6🐴";

            case REDSTONE -> "&c💎";
            case GOLD_INGOT -> "&6💎";
            case LAPIS_LAZULI -> "&9💎";
            case GLOWSTONE -> "&e🌟";
            case ENDER_PEARL -> "&3◎";
            case EXPERIENCE_BOTTLE -> "&d🧪";

            case PAPER -> "&f📄";
            case MAP -> "&e📄";
            case ITEM_FRAME,
                 PAINTING -> "&6🖼";

            case STICK -> "&6🪃";
            case ARROW -> "&f➵";
            case BOW -> "&6🏹";

            case CLAY_BALL -> "&3🪨";
            case BRICK -> "&c🧱";
            case QUARTZ,
                 QUARTZ_BLOCK,
                 QUARTZ_PILLAR -> "&f💎";

            case WHITE_WOOL -> "&f🧶";
            case BROWN_WOOL -> "&6🧶";
            case BLACK_WOOL -> "&8🧶";
            case GRAY_WOOL -> "&7🧶";
            case SHEARS -> "&c✂";

            default -> null;
        };
    }

    private static String formatEnchantedBook(ItemStack item) {
        if (!(item.getItemMeta() instanceof EnchantmentStorageMeta meta)) return "";
        String enchantments = meta.getStoredEnchants().entrySet().stream()
                .map(entry -> {
                                        String enchantId = entry.getKey().getKey().getKey().toLowerCase();
                                        String level = Utils.toRomanNumeral(entry.getValue());
                                        String name = LangManager.getEnchantName(enchantId);
                                        return name + " " + level;
                })
                .collect(Collectors.joining(" + "));
        return "&d📖 " + enchantments;
    }
}

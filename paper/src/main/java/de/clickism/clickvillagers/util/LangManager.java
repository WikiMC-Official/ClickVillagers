package de.clickism.clickvillagers.util;

import de.clickism.clickvillagers.ClickVillagers;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LangManager {
    private static File itemsFile;
    private static FileConfiguration itemsConfig;
    private static File enchantFile;
    private static FileConfiguration enchantConfig;

    // 初始化双配置文件
    public static void init() {
        ClickVillagers plugin = ClickVillagers.INSTANCE;
        if (plugin == null) return;

        itemsFile = new File(plugin.getDataFolder(), "items.yml");
        if (!itemsFile.exists()) {
            try {
                plugin.saveResource("items.yml", false);
            } catch (Exception ignored) {}
        }

        enchantFile = new File(plugin.getDataFolder(), "enchantments.yml");
        if (!enchantFile.exists()) {
            try {
                plugin.saveResource("enchantments.yml", false);
            } catch (Exception ignored) {}
        }
        reload();
    }

    // 重载所有语言文件
    public static void reload() {
        if (itemsFile != null) {
            itemsConfig = YamlConfiguration.loadConfiguration(itemsFile);
        }
        if (enchantFile != null) {
            enchantConfig = YamlConfiguration.loadConfiguration(enchantFile);
        }
    }

    // 获取物品翻译
    public static String getItemName(String materialId) {
        if (itemsConfig == null) {
            return Utils.titleCase(materialId.replace("_", " "));
        }
        String translate = itemsConfig.getString(materialId);
        if (translate != null && !translate.isBlank()) {
            return translate;
        }
        return Utils.titleCase(materialId.replace("_", " "));
    }

    // 获取附魔翻译
    public static String getEnchantName(String enchantId) {
        if (enchantConfig == null) {
            return Utils.titleCase(enchantId.replace("_", " "));
        }
        String translate = enchantConfig.getString(enchantId);
        if (translate != null && !translate.isBlank()) {
            return translate;
        }
        return Utils.titleCase(enchantId.replace("_", " "));
    }
}

package me.bobulo.mine.devmod.gui;

import me.bobulo.mine.devmod.DevMod;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;
import java.util.stream.Collectors;

public class FeaturesMenuGui extends GuiConfig {

    public FeaturesMenuGui(GuiScreen parent) {
        super(
          parent,
          getElements(),
          DevMod.MOD_ID,
          false,
          false,
          "Configuration"
        );
    }

    private static List<IConfigElement> getElements() {
        Configuration config = DevMod.getConfigService().getMainConfig();

        return config.getCategoryNames().stream()
          .filter(key -> !config.getCategory(key).isChild())
          .map(key -> new ConfigElement(config.getCategory(key)))
          .collect(Collectors.toList());
    }

}

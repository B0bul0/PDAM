package me.bobulo.mine.pdam.feature.update;

import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.config.ConfigValue;

public final class Update {

    public static final String FEATURE_ID = "update";

    public static final String GITHUB_USER = "B0bul0";
    public static final String GITHUB_REPO = "PDAM";

    // Disable chat update warning
    public static final ConfigValue<Boolean> CHAT_WARNING = ConfigProperty.of(FEATURE_ID + ".chat_warning", true);

}

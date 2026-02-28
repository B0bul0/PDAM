package me.bobulo.mine.pdam.feature.itemname.mapper;

public final class IdItemMapper implements ItemMapper {

    @Override
    public String mapItemName(int minecraftItemId) {
        return String.valueOf(minecraftItemId);
    }

    @Override
    public int reverseMapItemName(String item) {
        try {
            return Integer.parseInt(item);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}

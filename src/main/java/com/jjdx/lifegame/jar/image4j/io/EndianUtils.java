package com.jjdx.lifegame.jar.image4j.io;

public class EndianUtils {
    public EndianUtils() {
    }

    public static int swapInteger(int var0) {
        return (var0 & -16777216) >> 24 | (var0 & 16711680) >> 8 | (var0 & '\uff00') << 8 | (var0 & 255) << 24;
    }


    public static String toHexString(int var0, boolean var1, int var2) {
        if (var1) var0 = swapInteger(var0);

        StringBuilder var3 = new StringBuilder();
        var3.append(Integer.toHexString(var0));
        if (var3.length() % 2 != 0) var3.insert(0, '0');

        while (var3.length() < var2 * 2) var3.insert(0, "00");

        return var3.toString();
    }

    public static void toCharString(StringBuilder var0, int var1, int var2, char var3) {
        int var4 = 24;

        for (int var5 = 0; var5 < var2; ++var5) {
            int var6 = var1 >> var4 & 255;
            char var7 = var6 < 32 ? var3 : (char) var6;
            var0.append(var7);
            var4 -= 8;
        }

    }

    public static String toInfoString(int var0) {
        StringBuilder var1 = new StringBuilder();
        var1.append("Decimal: ").append(var0);
        var1.append(", Hex BE: ").append(toHexString(var0, false, 4));
        var1.append(", Hex LE: ").append(toHexString(var0, true, 4));
        var1.append(", String BE: [");
        toCharString(var1, var0, 4, '.');
        var1.append(']');
        var1.append(", String LE: [");
        toCharString(var1, swapInteger(var0), 4, '.');
        var1.append(']');
        return var1.toString();
    }
}

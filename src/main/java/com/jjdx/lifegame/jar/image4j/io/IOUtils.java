package com.jjdx.lifegame.jar.image4j.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {
    public IOUtils() {
    }

    public static int skip(InputStream var0, int var1, boolean var2) throws IOException {
        int var3;
        for (var3 = 0; var3 < var1; ++var3) {
            int var4 = var0.read();
            if (var4 == -1) break;
        }

        if (var3 < var1 && var2) {
            throw new EOFException("Failed to skip " + var1 + " bytes in input");
        }
        return var3;
    }
}

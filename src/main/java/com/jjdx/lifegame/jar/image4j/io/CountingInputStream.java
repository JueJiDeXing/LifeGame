package com.jjdx.lifegame.jar.image4j.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CountingInputStream extends FilterInputStream {
    private int count;

    public CountingInputStream(InputStream var1) {
        super(var1);
    }

    public int getCount() {
        return this.count;
    }

    public int read() throws IOException {
        int var1 = super.read();
        if (var1 != -1) {
            ++this.count;
        }

        return var1;
    }

    public int read(byte[] var1, int var2, int var3) throws IOException {
        int var4 = super.read(var1, var2, var3);
        if (var4 > 0) {
            this.count += var4;
        }

        return var4;
    }
}

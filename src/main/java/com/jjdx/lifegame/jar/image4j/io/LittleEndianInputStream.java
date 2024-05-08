package com.jjdx.lifegame.jar.image4j.io;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

public class LittleEndianInputStream extends DataInputStream implements CountingDataInput {
    public LittleEndianInputStream(CountingInputStream var1) {
        super(var1);
    }

    public int getCount() {
        return ((CountingInputStream) this.in).getCount();
    }

    public int skip(int var1, boolean var2) throws IOException {
        return IOUtils.skip(this, var1, var2);
    }

    public short readShortLE() throws IOException {
        int var1 = this.read();
        int var2 = this.read();
        if (var1 >= 0 && var2 >= 0) {
            return (short) ((var2 << 8) + (var1));
        }
        throw new EOFException();
    }

    public int readIntLE() throws IOException {
        int var1 = this.read();
        int var2 = this.read();
        int var3 = this.read();
        int var4 = this.read();
        if (var1 >= -1 && var2 >= -1 && var3 >= -1 && var4 >= -1) {
            return (var4 << 24) + (var3 << 16) + (var2 << 8) + (var1);
        }

        throw new EOFException();
    }


}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jjdx.lifegame.jar.image4j.io;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

public class LittleEndianInputStream extends DataInputStream implements CountingDataInput {
    public LittleEndianInputStream(CountingInputStream var1) {
        super(var1);
    }

    public int getCount() {
        return ((CountingInputStream)this.in).getCount();
    }

    public int skip(int var1, boolean var2) throws IOException {
        return IOUtils.skip(this, var1, var2);
    }

    public short readShortLE() throws IOException {
        int var1 = this.read();
        int var2 = this.read();
        if (var1 >= 0 && var2 >= 0) {
            short var3 = (short)((var2 << 8) + (var1 << 0));
            return var3;
        } else {
            throw new EOFException();
        }
    }

    public int readIntLE() throws IOException {
        int var1 = this.read();
        int var2 = this.read();
        int var3 = this.read();
        int var4 = this.read();
        if (var1 >= -1 && var2 >= -1 && var3 >= -1 && var4 >= -1) {
            int var5 = (var4 << 24) + (var3 << 16) + (var2 << 8) + (var1 << 0);
            return var5;
        } else {
            throw new EOFException();
        }
    }

    public float readFloatLE() throws IOException {
        int var1 = this.readIntLE();
        float var2 = Float.intBitsToFloat(var1);
        return var2;
    }

    public long readLongLE() throws IOException {
        int var1 = this.readIntLE();
        int var2 = this.readIntLE();
        long var3 = ((long)var1 << 32) + ((long)var2 & 4294967295L);
        return var3;
    }

    public double readDoubleLE() throws IOException {
        long var1 = this.readLongLE();
        double var3 = Double.longBitsToDouble(var1);
        return var3;
    }

    public long readUnsignedInt() throws IOException {
        long var1 = (long)this.readUnsignedByte();
        long var3 = (long)this.readUnsignedByte();
        long var5 = (long)this.readUnsignedByte();
        long var7 = (long)this.readUnsignedByte();
        long var9 = var1 << 24 | var3 << 16 | var5 << 8 | var7;
        return var9;
    }

    public long readUnsignedIntLE() throws IOException {
        long var1 = (long)this.readUnsignedByte();
        long var3 = (long)this.readUnsignedByte();
        long var5 = (long)this.readUnsignedByte();
        long var7 = (long)this.readUnsignedByte();
        long var9 = var7 << 24 | var5 << 16 | var3 << 8 | var1;
        return var9;
    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jjdx.lifegame.jar.image4j.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LittleEndianOutputStream extends DataOutputStream {
    public LittleEndianOutputStream(OutputStream var1) {
        super(var1);
    }

    public void writeShortLE(short var1) throws IOException {
        var1 = EndianUtils.swapShort(var1);
        super.writeShort(var1);
    }

    public void writeIntLE(int var1) throws IOException {
        var1 = EndianUtils.swapInteger(var1);
        super.writeInt(var1);
    }

    public void writeFloatLE(float var1) throws IOException {
        var1 = EndianUtils.swapFloat(var1);
        super.writeFloat(var1);
    }

    public void writeLongLE(long var1) throws IOException {
        var1 = EndianUtils.swapLong(var1);
        super.writeLong(var1);
    }

    public void writeDoubleLE(double var1) throws IOException {
        var1 = EndianUtils.swapDouble(var1);
        super.writeDouble(var1);
    }

    public void writeUnsignedInt(long var1) throws IOException {
        int var3 = (int)(var1 >> 24);
        int var4 = (int)(var1 >> 16 & 255L);
        int var5 = (int)(var1 >> 8 & 255L);
        int var6 = (int)(var1 & 255L);
        this.write(var3);
        this.write(var4);
        this.write(var5);
        this.write(var6);
    }

    public void writeUnsignedIntLE(long var1) throws IOException {
        int var3 = (int)(var1 >> 24);
        int var4 = (int)(var1 >> 16 & 255L);
        int var5 = (int)(var1 >> 8 & 255L);
        int var6 = (int)(var1 & 255L);
        this.write(var6);
        this.write(var5);
        this.write(var4);
        this.write(var3);
    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jjdx.lifegame.jar.image4j.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LittleEndianRandomAccessFile extends RandomAccessFile {
    public LittleEndianRandomAccessFile(File var1, String var2) throws FileNotFoundException {
        super(var1, var2);
    }

    public LittleEndianRandomAccessFile(String var1, String var2) throws FileNotFoundException {
        super(var1, var2);
    }

    public short readShortLE() throws IOException {
        short var1 = super.readShort();
        var1 = EndianUtils.swapShort(var1);
        return var1;
    }

    public int readIntLE() throws IOException {
        int var1 = super.readInt();
        var1 = EndianUtils.swapInteger(var1);
        return var1;
    }

    public float readFloatLE() throws IOException {
        float var1 = super.readFloat();
        var1 = EndianUtils.swapFloat(var1);
        return var1;
    }

    public long readLongLE() throws IOException {
        long var1 = super.readLong();
        var1 = EndianUtils.swapLong(var1);
        return var1;
    }

    public double readDoubleLE() throws IOException {
        double var1 = super.readDouble();
        var1 = EndianUtils.swapDouble(var1);
        return var1;
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
}

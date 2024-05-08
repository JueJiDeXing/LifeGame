//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jjdx.lifegame.jar.image4j.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CountingDataInputStream extends DataInputStream implements CountingDataInput {
    public CountingDataInputStream(InputStream var1) {
        super(new CountingInputStream(var1));
    }

    public int getCount() {
        return ((CountingInputStream)this.in).getCount();
    }

    public int skip(int var1, boolean var2) throws IOException {
        return IOUtils.skip(this, var1, var2);
    }

    public String toString() {
        return this.getClass().getSimpleName() + "(" + this.in + ") [" + this.getCount() + "]";
    }
}

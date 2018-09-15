package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;

import utils.Mappable;

public class Mouse implements Mappable {

    FileInputStream mouse;

    BufferedWriter bw;
    File mouseData;
    FileOutputStream fos;

    String fileName;

    byte[] dat = new byte[3];

    public Mouse(int devID) {
        fileName = "/c/mouseData" + System.currentTimeMillis();

        try {
            mouse = new FileInputStream("/dev/input/mouse" + devID);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int sizeOf() {
        return ((BYTE_SIZE * 5) + (DOUBLE_SIZE));
    }

    @Override
    public void write(MappedByteBuffer mem, int index) {
        try {
            mouse.read(dat);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i : dat) {
            mem.put(index, this.dat[i]);
            index++;
        }
    }

    @Override
    public void read(MappedByteBuffer mem, int index) {
        for (int i : dat) {
            this.dat[i] = mem.get(index);
            index++;
        }
    }
}

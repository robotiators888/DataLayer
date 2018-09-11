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
        
        mem.put((byte) 1);
        mem.put((byte) 0);
        mem.putLong(System.currentTimeMillis());
        mem.put(dat);
        
    }

    @Override
    public void read(MappedByteBuffer mem, int index) {
        mem.get(null, (mem.position() - index), 2);
        mem.getDouble(index + 2);
        mem.get(dat, (mem.position() - (index + 10)), 3);
    }
}

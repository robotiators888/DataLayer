package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;

import utils.Mappable;

/**
 * This class is a {@link Mappable} hardware interface for an optical mouse
 * connected to a Linux operating system via a USB port.
 */
public class Mouse implements Mappable {

    FileInputStream mouse;

    BufferedWriter bw;
    File mouseData;
    FileOutputStream fos;

    String fileName;

    byte[] dat = new byte[3];

    /**
     * Opens the device file for the mouse.
     * 
     * @param devID
     *            The ID of the mouse device file.
     */
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
        return BYTE_SIZE * 3;
    }

    @Override
    public void write(MappedByteBuffer mem, int index) {

        try {
            mouse.read(dat); // Reads the data from the device file.
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

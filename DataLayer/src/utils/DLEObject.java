package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * This class allows for objects to have their data stored in a memory mapped
 * file. This will log the attributes of that object and allow for it to be
 * shared between classes quickly.
 * 
 * @param <T>
 *            T is any class that extents {@link Mappable} of which an object
 *            exists that needs to have its attributes stored in a memory mapped
 *            file.
 */
public class DLEObject<T extends Mappable> {

    MappedByteBuffer mem;

    Object[] items;

    byte[] spare = new byte[52]; // TODO Create header

    int header;
    int maxObj;
    int numObj;
    int objSize;
    int position;

    /**
     * Constructs the memory mapped file for an object of class {@link T}.
     * 
     * @param initVal
     *            The initial value of the object to be logged.
     * @param logName
     *            The name of the log file to be created
     * @throws IOException
     *             Thrown if the file is not able to be created
     * @throws FileNotFoundException
     *             Thrown if the log file is not created properly and therefore
     *             cannot be converted into a {@link RandomAccessFile}.
     */
    public DLEObject(T initVal, String logName)
            throws IOException, FileNotFoundException {

        header = 64;
        numObj = 0;
        maxObj = 15000;
        objSize = initVal.sizeOf();

        File log = new File(logName);

        FileChannel fc;

        if (log.createNewFile()) {
            /* File just created... initialize it. */
            log.delete();
            fc = new RandomAccessFile(log, "rw").getChannel();
        } else {
            /* File already exists... wait for initialization to complete. */
            fc = new RandomAccessFile(log, "rw").getChannel();
            while (fc.size() < 8) {
                continue;
            }
        }

        mem = fc.map(FileChannel.MapMode.READ_WRITE, 0,
                ((objSize * maxObj) + header));

        mem.putInt(maxObj);
        mem.putInt(numObj);
        mem.putInt(objSize);
        mem.put(spare);
    }

    /**
     * Constructs the memory mapped file for an object of class {@link T}.
     * 
     * @param initVal
     *            The initial value of the object to be logged.
     * @param logName
     *            The name of the log file to be created
     * @param p_maxObj
     *            The maximum number of objects that will be stored in the file.
     * @throws IOException
     *             Thrown if the file is not able to be created
     * @throws FileNotFoundException
     *             Thrown if the log file is not created properly and therefore
     *             cannot be converted into a {@link RandomAccessFile}.
     */
    public DLEObject(T initVal, String logName, int p_maxObj)
            throws IOException, FileNotFoundException {

        header = 64;
        numObj = 0;
        maxObj = p_maxObj;
        objSize = initVal.sizeOf();

        File log = new File(logName);

        FileChannel fc;

        if (log.createNewFile()) {
            // File just created... initialize it.
            log.delete();
            fc = new RandomAccessFile(log, "rw").getChannel();
        } else {
            // File already exists... wait for initialization to complete.
            fc = new RandomAccessFile(log, "rw").getChannel();
            while (fc.size() < 8) {
                continue;
            }
        }

        mem = fc.map(FileChannel.MapMode.READ_WRITE, 0,
                ((objSize * maxObj) + header));

        mem.putInt(maxObj);
        mem.putInt(numObj);
        mem.putInt(objSize);
        mem.put(spare);
    }

    /**
     * Updates an object so its attributes will match the latest values stored
     * in the memory mapped file.
     * 
     * @param t
     *            The object that will be updated.
     * @return The updated object.
     */
    public T get(T t) {
        position = mem.position();
        t.read(mem, position);
        return t;
    }

    /**
     * Updates an object so its attributes will match the values stored in the
     * memory mapped file from a specific iteration.
     * 
     * @param t
     *            The object that will be updated.
     * @param cycle
     *            The iteration number that t will match.
     * @return The updated object.
     */
    public T get(T t, int cycle) {
        position = mem.position();
        int location = position - (numObj - (t.sizeOf() * cycle));
        t.read(mem, location);
        return t;
    }

    /**
     * Puts an object's attributes into the memory mapped file.
     * 
     * @param t
     *            The object to log.
     */
    public void set(T t) {
        numObj++;
        t.write(mem, 0);
    }
}

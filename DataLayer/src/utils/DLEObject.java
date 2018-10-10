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
 *            T is any class that extends {@link Mappable} of which an object
 *            exists that needs to have its attributes stored in a memory mapped
 *            file.
 */
public class DLEObject<T extends Mappable> {

    FileChannel fc;

    MappedByteBuffer mem;

    final static byte READ = 1;
    final static byte WRITE = 1;

    final static int RW = 2;
    final static int STANDARD_DATA = 10;

    final static int HEADER = 128;
    final static int LOG_TIME = 90000;

    long timeOfObject;

    int objSize;
    int position;

    /**
     * Constructs the memory mapped file for an object of class {@link T}.
     * 
     * @param initVal
     *            The initial value of the object to be logged.
     * @param logName
     *            The name of the log file to be created.
     * @throws IOException
     *             Thrown if the file is not able to be created.
     * @throws FileNotFoundException
     *             Thrown if the log file is not created properly and therefore
     *             cannot be converted into a {@link RandomAccessFile}.
     */
    public DLEObject(T initVal, String logName)
            throws IOException, FileNotFoundException {
        long length = (long) ((objSize * (LOG_TIME * objSize)) + HEADER);
        init(initVal, logName, length);
    }

    /**
     * Constructs the memory mapped file for an object of class {@link T}.
     * 
     * @param initVal
     *            The initial value of the object to be logged.
     * @param logName
     *            The name of the log file to be created.
     * @param logSize
     *            The maximum number of objects that will be stored in the file.
     * @throws IOException
     *             Thrown if the file is not able to be created.
     * @throws FileNotFoundException
     *             Thrown if the log file is not created properly and therefore
     *             cannot be converted into a {@link RandomAccessFile}.
     */
    public DLEObject(T initVal, String logName, int logSize)
            throws IOException, FileNotFoundException {
        long length = (long) ((objSize * logSize) + HEADER);
        init(initVal, logName, length);
    }

    @SuppressWarnings("resource")
    private void init(T initVal, String logName, long length) 
            throws IOException, FileNotFoundException {
        objSize = STANDARD_DATA + initVal.sizeOf();

        File log = new File(logName);

        RandomAccessFile raf = new RandomAccessFile(log, "rw");

        if (log.createNewFile()) {
            /* File just created... initialize it. */
            log.delete();
            fc = raf.getChannel();
        } else {
            /* File already exists... wait for initialization to complete. */
            while (!log.exists() || log.isDirectory()) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                } finally {
                    fc = raf.getChannel();
                }
            }

            while (fc.size() < HEADER) {
                continue;
            }
        }

        raf.setLength(length);

        mem = fc.map(FileChannel.MapMode.READ_WRITE, 0, length);

        mem.putInt(4, this.objSize);
        mem.putInt(0, HEADER);
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
        int index = mem.getInt(0) - t.sizeOf();
        if (mem.get(index) == WRITE) {
            try {
                this.timeOfObject = mem.getLong(index + RW);
                t.read(mem, index + STANDARD_DATA);
            } catch (Exception e) {
                e.printStackTrace();
                // TODO Exception handling
            }
        }
        // TODO Timeout if the data wasn't written
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
        int index = mem.getInt(0) - (t.sizeOf() * cycle);
        if (mem.get(index) == WRITE) {
            try {
                this.timeOfObject = mem.getLong(index + RW);
                t.read(mem, index + STANDARD_DATA);
            } catch (Exception e) {
                e.printStackTrace();
                // TODO Exception handling
            }
        }
        // TODO Timeout if the data wasn't written
        return t;
    }

    /**
     * Puts an object's attributes into the memory mapped file.
     * 
     * @param t
     *            The object to log.
     */
    public void set(T t) {
        int index = mem.getInt(0) + t.sizeOf();
        try {
            mem.putInt(0, index);
            mem.putLong(index + RW, System.currentTimeMillis());
            t.write(mem, index + STANDARD_DATA);
            mem.put(index, WRITE);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO Exception handling
        }
    }
}

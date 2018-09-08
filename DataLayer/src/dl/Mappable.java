package dl;

import java.nio.MappedByteBuffer;

/**
 * All Data Layer Enabled Objects must implement the Serialize Interface. This
 * interface allows objects to be converted to bytes for storage in a memory map
 * file and then to be retrieved later.
 */
public interface Mappable {

    /**
     * The size of a primitive byte in bytes.
     */
    public final static int BYTE_SIZE = 1;

    /**
     * The size of a primitive integer in bytes.
     */
    public final static int INT_SIZE = 4;

    /**
     * The size of a primitive long in bytes.
     */
    public final static int LONG_SIZE = 8;

    /**
     * The size of a primitive double in bytes.
     */
    public final static int DOUBLE_SIZE = 8;

    /**
     * Calculates the number of bytes that an object occupies in memory.
     * 
     * @return The size of an object in bytes.
     */
    public int sizeOf();

    /**
     * Writes out the attributes of an object to a memory mapped file for
     * storage.
     * 
     * @param mem
     *            The memory mapped file to write to.
     * @param index
     *            The number of bytes to offset where the buffer beings writing.
     */
    public void write(MappedByteBuffer mem, int index);

    /**
     * Reads back attributes from a memory mapped file to reconstruct an object
     * with equivalent attributes.
     * 
     * @param mem
     *            The memory mapped file to read from.
     * @param index
     *            The number of bytes in the buffer to offset where the reading
     *            begins.
     */
    public void read(MappedByteBuffer mem, int index);
}

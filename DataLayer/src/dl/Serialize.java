package dl;

/**
 * All Data Layer Enabled Objects must implement the Serialize Interface. This
 * interface allows objects to be converted to bytes for storage in a memory map
 * file and then to be retrieved later.
 */
public interface Serialize {

    /**
     * This method should be used to convert an object to a byte array so that
     * the serialized data can be stored in its binary form.
     * 
     * @return The binary version of an object as a byte array.
     */
    public byte[] getBytes();

    /**
     * This method is used to calculate the number of bytes that an object
     * occupies in memory.
     * 
     * @return The size of an object in bytes.
     */
    public int sizeOf();

    /**
     * This method should be used to convert a serialized object back to its
     * original form.
     * 
     * @param object
     *            A byte array containing a serialized object.
     * @return An object of the type T where T is any class that implements
     *         Serialize.
     */
    public Object getObject(byte[] object);
}

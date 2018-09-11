package data;

import java.nio.MappedByteBuffer;

import disc.data.Waypoint;
import disc.util.WaypointException;
import utils.Mappable;

/**
 * Data type for storing a robot's pose
 */
public class Pose extends Waypoint implements Mappable {

    /**
     * Constructs a {@link Pose} from a String matching the {@link Waypoint}
     * format.
     * 
     * @param arg0
     *            The String to construct the Position from
     * @throws WaypointException
     *             If the data minimum is not met.
     */
    public Pose(String arg0) throws WaypointException {
        super(arg0);
    }

    /**
     * Constructor for cloning, or building a {@link Pose} with everything.
     */
    public Pose(String name, double x, double y, double z, double heading,
            double roll, double pitch) {
        super(name, x, y, z, heading, roll, pitch);
    }

    /**
     * Constructor for building a {@link Pose}, without needing a name to be
     * populated.
     */
    public Pose(double x, double y, double z, double heading, double roll,
            double pitch) {
        super("", x, y, z, heading, roll, pitch);
    }

    /**
     * Constructor for creating a {@link Pose} with only an x and a y.
     */
    public Pose(double x, double y) {
        super(x, y);
        this.name = "";
    }

    /**
     * Constructor for creating a {@link Pose} with only an x, y, and a
     * heading.
     */
    public Pose(double x, double y, double heading) {
        super(x, y, heading);
        this.name = "";
    }

    /**
     * @return the Name associated with this {@link Pose}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the Name to set for this {@link Pose}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the X associated with this {@link Pose}
     */
    @Override
    public double getX() {
        return x;
    }

    /**
     * @param x
     *            the X to set for this {@link Pose}
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the Y associated with this {@link Pose}
     */
    @Override
    public double getY() {
        return y;
    }

    /**
     * @param y
     *            The Y to set for this {@link Pose}
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return the Z associated with this {@link Pose}
     */
    @Override
    public double getZ() {
        return z;
    }

    /**
     * @param z
     *            the Z to set for this {@link Pose}
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * @return the Heading associated with this {@link Pose}
     */
    @Override
    public double getHeading() {
        return heading;
    }

    /**
     * @param heading
     *            the Heading to set for this {@link Position}
     */
    public void setHeading(double heading) {
        this.heading = heading;
    }

    /**
     * @return the Roll associated with this {@link Position}
     */
    @Override
    public double getRoll() {
        return roll;
    }

    /**
     * @param roll
     *            the Roll to set for this {@link Pose}
     */
    public void setRoll(double roll) {
        this.roll = roll;
    }

    /**
     * @return the Pitch associated with this {@link Pose}
     */
    @Override
    public double getPitch() {
        return pitch;
    }

    /**
     * @param pitch
     *            the Pitch to set for this {@link Pose}
     */
    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    /**
     * Rudimentary comparison to compare this {@link Pose} to a
     * {@link Waypoint}.
     * 
     * @param w
     *            the Waypoint to compare against
     * @param tolerance
     *            the tolerance for "how close" it can be
     * @return true if the values are within a tolerable difference
     */
    public boolean compareToWaypoint(Waypoint w, double tolerance) {
        return (comparePosition(w, tolerance) && compareAttitude(w, tolerance));
    }

    /**
     * Comparison to check for an exact match between this {@link Position} and
     * a {@link Waypoint}.
     * 
     * @param w
     *            the Waypoint to compare against
     * @return true if the Waypoint matches
     */
    public boolean compareToWaypoint(Waypoint w) {
        // Compares with a tolerance of 0.0000001 just incase of a floating
        // point error
        return compareToWaypoint(w, 1e-7);
    }

    /**
     * Rudimentary comparison to compare this {@link Pose} to a
     * {@link Waypoint}, only checking x, y, and z.
     * 
     * @param w
     *            the Waypoint to compare against
     * @param tolerance
     *            the tolerance for "how close" it can be
     * @return true if the values are within a tolerable difference
     */
    public boolean comparePosition(Waypoint w, double tolerance) {
        return (((this.x == w.getX())
                || (Math.abs(this.x - w.getX()) <= tolerance))
                && ((this.y == w.getY())
                        || (Math.abs(this.y - w.getY()) <= tolerance))
                && ((this.z == w.getZ())
                        || (Math.abs(this.z - w.getZ()) <= tolerance)));
    }

    /**
     * Comparison to check for an exact match between this {@link Pose} and
     * a {@link Waypoint}, only checking x, y, and z.
     * 
     * @param w
     *            the Waypoint to compare against
     * @return true if the Waypoint matches
     */
    public boolean comparePosition(Waypoint w) {
        // Compares with a tolerance of 0.0000001 just incase of a floating
        // point error
        return comparePosition(w, 1e-7);
    }

    /**
     * Rudimentary comparison to compare this {@link Pose} to a
     * {@link Waypoint}, only checking heading, pitch, and roll.
     * 
     * @param w
     *            the Waypoint to compare against
     * @param tolerance
     *            the tolerance for "how close" it can be
     * @return true if the values are within a tolerable difference
     */
    public boolean compareAttitude(Waypoint w, double tolerance) {
        return (((this.heading == w.getHeading())
                || (Math.abs(this.heading - w.getHeading()) <= tolerance))
                && ((this.pitch == w.getPitch())
                        || (Math.abs(this.pitch - w.getPitch()) <= tolerance))
                && ((this.roll == w.getRoll())
                        || (Math.abs(this.roll - w.getRoll()) <= tolerance)));
    }

    /**
     * Comparison to check for an exact match between this {@link Pose} and
     * a {@link Waypoint}, only checking heading, roll, and pitch.
     * 
     * @param w
     *            the Waypoint to compare against
     * @return true if the Waypoint matches
     */
    public boolean compareAttitude(Waypoint w) {
        // Compares with a tolerance of 0.0000001 just incase of a floating
        // point error
        return compareAttitude(w, 1e-7);
    }

    @Override
    public int sizeOf() {
        return DOUBLE_SIZE * 6;
    }

    @Override
    public void write(MappedByteBuffer mem, int index) {
        mem.putDouble(index,     this.x       );
        mem.putDouble(index + 1, this.y       );
        mem.putDouble(index + 2, this.z       );
        mem.putDouble(index + 3, this.heading );
        mem.putDouble(index + 4, this.pitch   );
        mem.putDouble(index + 5, this.roll    );
    }

    @Override
    public void read(MappedByteBuffer mem, int index) {
        this.x       =  mem.getDouble(index                    );
        this.y       =  mem.getDouble(index + (DOUBLE_SIZE)    );
        this.z       =  mem.getDouble(index + (DOUBLE_SIZE * 2));
        this.heading =  mem.getDouble(index + (DOUBLE_SIZE * 3));
        this.pitch   =  mem.getDouble(index + (DOUBLE_SIZE * 4));
        this.roll    =  mem.getDouble(index + (DOUBLE_SIZE * 5));
    }

}

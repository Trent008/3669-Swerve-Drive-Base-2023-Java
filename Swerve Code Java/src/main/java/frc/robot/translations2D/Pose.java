package frc.robot.translations2D;

/**
 * stores a Vector object for a positional value
 * and an Angle object for an angular value
*/
public class Pose {
    public Vector vector; // stores the positional value of the pose
    public Angle angle;   // stores the angular value of the pose
    
    public Pose(Vector vector, Angle angle)
    {
        this.vector = vector;
        this.angle = angle;
    }
    
    public Pose() {
        this(new Vector(), new Angle());
    }

    public Pose getAdded(Pose obj)
    {
        Pose res = this;
        res.vector.add(obj.vector);
        res.angle.add(obj.angle);
        return res;
    }

    public Pose getDifference(Pose obj)
    {
        Pose res = this;
        res.vector.subtract(obj.vector);
        res.angle.subtract(obj.angle);
        return res;
    }

    public void scale(double kPositional, double kAngular)
    {
        vector.scale(kPositional);
        angle.scale(kAngular);
    }

    public void divide(double k)
    {
        vector.divide(k);
        angle.divide(k);
    }

    public void moveToward(Pose pose, double rate)
    {
        vector.moveToward(pose.vector, rate);
        angle.moveToward(pose.angle, rate);
    }

    public void limit(double vectorLimit, double angleLimit)
    {
        if (vector.getMagnitude() > vectorLimit)
        {
            vector.scale(vectorLimit / vector.getMagnitude());
        }
        if (angle.getMagnitude() > angleLimit)
        {
            angle.scale(angleLimit / angle.getMagnitude());
        }
    }

    public Pose getRotatedCW(double angle)
    {
        return new Pose(vector.getRotatedCW(angle), this.angle);
    }
};
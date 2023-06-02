package frc.robot.translations2D;

/**
 * stores 2D coordinates
 * can be added/subtacted with other Vector objects
 * or multiplied/divided by a double
*/
public class Vector {
    public double x, y; // coordinates

    /**
     * constructor for 2D vector
     * given x and y coordinates
     * */ 
    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector() {
        this(0,0);
    }

    // add another vector to this vector and return the result
    public void add(Vector obj)
    {
        x += obj.x;
        y += obj.y;
    }

    public Vector getAdded(Vector obj)
    {
        
        return new Vector(x + obj.x, y + obj.y);
    }

    // subtract another vector from this vector
    public void subtract(Vector obj)
    {
        x -= obj.x;
        y -= obj.y;
    }

    public Vector getDifference(Vector obj)
    {
        return new Vector(x - obj.x, y - obj.y);
    }

    // return this vector after scaling by the given constant
    public void scale(double k)
    {
        x *= k;
        y *= k;
    }

    public Vector getScaled(double k)
    {
        return new Vector(x*k, y*k);
    }

    // return this vector after dividing by the given constant
    public void divide(double k)
    {
        x /= k;
        y /= k;
    }

    public Vector getDivided(double k)
    {
        return new Vector(x/k, y/k);
    }

    // return this vector's angle (-180 to 180 degrees)
    public double getAngle()
    {
        return Math.atan2(x, y) * 180 / Math.PI;
    }

    // return this vector's magnitude as a double
    public double getMagnitude()
    {
        return Math.hypot(x, y);
    }

    // move this vector toward another vector's value by the specified increment
    public void moveToward(Vector target, double increment)
    {
        target.subtract(this);
        if (target.getMagnitude() > 2 * increment)
        {
            this.add(target.getDivided(target.getMagnitude()).getScaled(increment));
        }
        else
        {
            this.add(target.getDivided(2.0));
        }
    }

    // rotate this vector clockwise by the given angle
    public void rotateCW(double angle)
    {
        angle *= Math.PI / 180;
        double newX, newY;
        newX = x * Math.cos(angle) + y * Math.sin(angle);
        newY = y * Math.cos(angle) - x * Math.sin(angle);
        x = newX;
        y = newY;
    }

    public Vector getRotatedCW(double angle)
    {
        angle *= Math.PI / 180;
        return new Vector(x * Math.cos(angle) + y * Math.sin(angle), y * Math.cos(angle) - x * Math.sin(angle));
    }
};
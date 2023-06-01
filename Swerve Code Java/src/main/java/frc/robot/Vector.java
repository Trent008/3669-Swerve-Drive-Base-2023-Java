/**
 * stores 2D coordinates
 * can be added/subtacted with other Vector objects
 * or multiplied/divided by a double
*/
class Vector {
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
        this.Vector(0,0);
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
        return atan2(x, y) * 180 / M_PI;
    }

    // return this vector's magnitude as a double
    public double getMagnitude()
    {
        return hypot(x, y);
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
    public Vector rotateCW(double angle)
    {
        angle *= M_PI / 180;
        this = new Vector(x * cos(angle) + y * sin(angle), y * cos(angle) - x * sin(angle));
        return this;
    }

    public Vector getRotatedCW(double angle)
    {
        angle *= M_PI / 180;
        return new Vector(x * cos(angle) + y * sin(angle), y * cos(angle) - x * sin(angle));
    }
};
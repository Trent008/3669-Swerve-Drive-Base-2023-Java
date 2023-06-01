/**
 * stores angular values from -180 to 180 degrees
 * can be added or subtracted with other angles without going out of bounds
 * can be multiplied or divided by a double
 * */
class Angle
{
    public double value; // size of the angle

    /**
     * angle object for storing angular values in degrees
     * 
     * angle is limited to -180 to 180 when added or subtracted
     * */
    public Angle(double angle)
    {
        value = angle;
    }

    public Angle() {
        this.Angle(0);
    }

    /**
     * add another angle to this and return the result
     * limited to -180 to 180 degrees
     * */
    public Angle add(Angle obj)
    {
        value += obj.value;
        value += value > 180 ? -360 : value < -180 ? 360
                                                   : 0;
        return this;
    }

    /**
     * subtract another angle from this and return the result
     * limited to -180 to 180 degrees
     * */
    public Angle subtract(Angle obj)
    {
        value -= obj.value;
        value += value > 180 ? -360 : value < -180 ? 360
                                                   : 0;
        return this;
    }

    public Angle getAdded(Angle obj)
    {
        Angle res = this;
        res.value += obj.value;
        res.value += res.value > 180 ? -360 : res.value < -180 ? 360
                                                   : 0;
        return res;
    }

    public Angle getSubtracted(Angle obj)
    {
        Angle res = this;
        res.value -= obj.value;
        res.value += res.value > 180 ? -360 : res.value < -180 ? 360
                                                   : 0;
        return res;
    }


    public Angle scale(double k)
    {
        value *= k;
        return this;
    }

    public Angle divide(double k)
    {
        value /= k;
        return this;
    }
    
    public Angle getMagnitude()
    {
        return abs(value);
    }

    // move this Angle object toward another Angle object
    public void moveToward(Angle target, double rate)
    {
        target.subtract(this);
        if (abs(target.value) > 2 * rate)
        {
            target.divide(abs(target.value));
            target.scale(rate);
            this.add(target);
        }
        else // if (std::abs(target.value) > .005)
        {
            target.divide(2);
            this.add(target);
        }
    }
};
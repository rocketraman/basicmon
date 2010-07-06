package org.basicmon;

/**
 * A BasicCounter interface.
 */
public interface BasicCounter extends BasicMon {

    public long getVal();

    public void setVal(long  val);

    public void increment();

    public void decrement();

}

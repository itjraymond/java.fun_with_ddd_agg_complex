package ca.jent.dddjdbccomplex.types;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class Price extends BigDecimal {

    // factory method
    public static Price of(String value) {
        return new Price(value);
    }

    public static Price of(BigDecimal value) {
        return new Price(value.toString());
    }

    public Price(char[] in, int offset, int len) {
        super(in, offset, len);
    }

    public Price(char[] in, int offset, int len, MathContext mc) {
        super(in, offset, len, mc);
    }

    public Price(char[] in) {
        super(in);
    }

    public Price(char[] in, MathContext mc) {
        super(in, mc);
    }

    public Price(String val) {
        super(val);
    }

    public Price(String val, MathContext mc) {
        super(val, mc);
    }

    public Price(double val) {
        super(val);
    }

    public Price(double val, MathContext mc) {
        super(val, mc);
    }

    public Price(BigInteger val) {
        super(val);
    }

    public Price(BigInteger val, MathContext mc) {
        super(val, mc);
    }

    public Price(BigInteger unscaledVal, int scale) {
        super(unscaledVal, scale);
    }

    public Price(BigInteger unscaledVal, int scale, MathContext mc) {
        super(unscaledVal, scale, mc);
    }

    public Price(int val) {
        super(val);
    }

    public Price(int val, MathContext mc) {
        super(val, mc);
    }

    public Price(long val) {
        super(val);
    }

    public Price(long val, MathContext mc) {
        super(val, mc);
    }
}

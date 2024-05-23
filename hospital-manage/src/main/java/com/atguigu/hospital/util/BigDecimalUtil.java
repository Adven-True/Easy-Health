package com.atguigu.hospital.util;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;

public class BigDecimalUtil {

    //Default operation precision
    private static final int DEFAULT_DIV_SCALE = 10;

    /**
     * Provide precise addition operation
     *
     * @param v1
     * @param v2
     * @return sum of v1 and v2
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.add(b2).doubleValue();

    }

    /**
     * Provide precise addition operation
     *
     * @param v1
     * @param v2
     * @param scale Indicate precision to a certain number of decimal places
     * @return The sum of two parameters in mathematical addition, returned as a string
     */
    public static double add(double v1, double v2, int scale) {
        return round(add(v1, v2), 2);
    }

    /**
     * Provide precise addition operation
     *
     * @param v1
     * @param v2
     * @return The sum of two parameters in mathematical addition, returned as a string
     */

    public static String add(String v1, String v2) {
        if (StringUtils.isEmpty(v1)) v1 = "0";
        if (StringUtils.isEmpty(v2)) v2 = "0";

        BigDecimal b1 = new BigDecimal(v1);

        BigDecimal b2 = new BigDecimal(v2);

        return b1.add(b2).toString();

    }

    /**
     * Provide precise addition operation
     *
     * @param v1
     * @param v2
     * @param scale Indicate precision to a certain number of decimal places
     * @return The sum of two parameters in mathematical addition, returned as a string
     */

    public static String add(String v1, String v2, int scale) {
        if (StringUtils.isEmpty(v1)) v1 = "0";
        if (StringUtils.isEmpty(v2)) v2 = "0";
        return round(add(v1, v2), 2);

    }

    /**
     * Provide precise addition operation
     *
     * @param v1
     * @param v2
     * @return The difference between two parameters
     */

    public static double subtract(double v1, double v2)

    {

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.subtract(b2).doubleValue();

    }

    /**
     * Provide precise subtraction operation
     *
     * @param v1
     * @param v2
     * @param scale Indicate precision to a certain number of decimal places
     * @return The difference between two parameters
     */

    public static double subtract(double v1, double v2, int scale)

    {
        return round(subtract(v1, v2), scale);

    }

    /**
     * Provide precise subtraction operation
     *
     * @param v1
     * @param v2
     * @return The difference between two parameters in mathematical subtraction, returned as a string
     */

    public static String subtract(String v1, String v2) {
        if (StringUtils.isEmpty(v1)) v1 = "0";
        if (StringUtils.isEmpty(v2)) v2 = "0";

        BigDecimal b1 = new BigDecimal(v1);

        BigDecimal b2 = new BigDecimal(v2);

        return b1.subtract(b2).toString();

    }

    /**
     *
     *
     * @param v1
     * @param v2
     * @param scale
     * @return
     */

    public static String subtract(String v1, String v2, int scale)

    {
        return round(subtract(v1, v2), 2);

    }

    /**
     *
     *
     * @param v1
     * @param v2
     * @param scale
     * @return The product of two parameters
     */

    public static double multiply(double v1, double v2, int scale)

    {
        return round(multiply(v1, v2), scale);

    }

    /**
     *
     *
     * @param v1
     * @param v2
     * @return The product of two parameters
     */

    public static double multiply(double v1, double v2) {

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.multiply(b2).doubleValue();

    }

    /**
     *
     *
     * @param v1
     * @param v2
     * @param scale
     * @return
     */

    public static String multiply(String v1, String v2, int scale)

    {
        return round(multiply(v1, v2), scale);

    }

    /**
     *
     *
     * @param v1
     * @param v2
     * @return
     */

    public static String multiply(String v1, String v2) {
        if (StringUtils.isEmpty(v1)) v1 = "0";
        if (StringUtils.isEmpty(v2)) v2 = "0";

        BigDecimal b1 = new BigDecimal(v1);

        BigDecimal b2 = new BigDecimal(v2);

        return b1.multiply(b2).toString();

    }

    /**
     * Provide (relatively) precise division operation.
     * When division results in a non-terminating decimal, precision is maintained to 10 decimal places,
     * with rounding using the ROUND_HALF_EVEN rounding mode
     *
     * @param v1
     * @param v2
     * @return
     */

    public static double divide(double v1, double v2)

    {

        return divide(v1, v2, DEFAULT_DIV_SCALE);

    }

    /**
     * ROUND_HALF_EVEN
     *
     * @param v1
     * @param v2
     * @param scale
     * @return
     */

    public static double divide(double v1, double v2, int scale)

    {

        return divide(v1, v2, scale, BigDecimal.ROUND_HALF_EVEN);

    }

    /**
     * The rounding mode is determined by the user-specified rounding mode
     *
     * @param v1
     * @param v2
     * @param scale
     * @param round_mode
     * @return
     */

    public static double divide(double v1, double v2, int scale, int round_mode) {

        if (scale < 0)

        {

            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");

        }

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.divide(b2, scale, round_mode).doubleValue();

    }

    /**
     * ROUND_HALF_EVEN
     *
     * @param v1
     * @param v2
     * @return
     */

    public static String divide(String v1, String v2) {
        if (StringUtils.isEmpty(v1)) v1 = "0";
        if (StringUtils.isEmpty(v2) || Double.parseDouble(v2) == 0) v2 = "1";

        return divide(v1, v2, DEFAULT_DIV_SCALE);

    }

    /**
     * ROUND_HALF_EVEN
     *
     * @param v1
     * @param v2
     * @param scale
     * @return
     */

    public static String divide(String v1, String v2, int scale)

    {
        if (StringUtils.isEmpty(v2) || Double.parseDouble(v2) == 0) v2 = "1";
        return divide(v1, v2, scale, BigDecimal.ROUND_HALF_EVEN);

    }

    /**
     *
     *
     * @param v1
     * @param v2
     * @param scale
     * @param round_mode
     * @return
     */

    public static String divide(String v1, String v2, int scale, int round_mode)

    {

        if (scale < 0)

        {

            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");

        }

        BigDecimal b1 = new BigDecimal(v1);

        BigDecimal b2 = new BigDecimal(v2);

        return b1.divide(b2, scale, round_mode).toString();

    }

    /**
     * ROUND_HALF_EVEN
     *
     * @param v
     * @param scale
     * @return
     */

    public static double round(double v, int scale)

    {

        return round(v, scale, BigDecimal.ROUND_HALF_EVEN);

    }

    /**
     *
     *
     * @param v
     * @param scale
     * @param round_mode
     * @return
     */

    public static double round(double v, int scale, int round_mode)

    {

        if (scale < 0)

        {

            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");

        }

        BigDecimal b = new BigDecimal(Double.toString(v));

        return b.setScale(scale, round_mode).doubleValue();

    }

    /**
     * ROUND_HALF_EVEN
     *
     * @param v
     * @param scale
     * @return
     */

    public static String round(String v, int scale)

    {

        return round(v, scale, BigDecimal.ROUND_HALF_EVEN);

    }

    /**
     *
     *
     * @param v
     * @param scale
     * @param round_mode
     * @return
     */

    public static String round(String v, int scale, int round_mode)

    {

        if (scale < 0)

        {

            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");

        }

        BigDecimal b = new BigDecimal(v);

        return b.setScale(scale, round_mode).toString();

    }
}

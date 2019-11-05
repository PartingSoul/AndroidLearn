package com.parting_soul.learn.layoutinflater.redbook;

/**
 * @author parting_soul
 * @date 2019-10-10
 * 规定x轴正方向移动是值为正，否则为负值
 */
public class SplashAttributesTag {
    int index;
    float xIn;
    float xOut;
    float yIn;
    float yOut;
    float aIn;
    float aOut;

    public boolean isEmpty() {
        return notSetData(xIn) && notSetData(xOut) && notSetData(yIn) && notSetData(yOut) &&
                notSetData(aIn) && notSetData(aOut);
    }

    public boolean notSetData(float data) {
        return Math.abs(data) < 10e-6;
    }

    @Override
    public String toString() {
        return "SplashAttributesTag{" +
                "xIn=" + xIn +
                ", xOut=" + xOut +
                ", yIn=" + yIn +
                ", yOut=" + yOut +
                ", aIn=" + aIn +
                ", aOut=" + aOut +
                '}';
    }
}

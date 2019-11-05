package com.parting_soul.support.utils.tuple;

/**
 * 三元祖
 *
 * @author parting_soul
 * @date 2019/4/25
 */
public class ThreeTuple<A, B, C> {
    public final A tupleA;
    public final B tupleB;
    public final C tupleC;

    public ThreeTuple(A tupleA, B tupleB, C tupleC) {
        this.tupleA = tupleA;
        this.tupleB = tupleB;
        this.tupleC = tupleC;
    }
}

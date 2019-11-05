package com.parting_soul.support.utils.tuple;

/**
 * 二元组
 *
 * @author parting_soul
 * @date 2019/4/25
 */
public class TwoTuple<A, B> {
    public final A tupleA;
    public final B tupleB;

    public TwoTuple(A tupleA, B tupleB) {
        this.tupleA = tupleA;
        this.tupleB = tupleB;
    }
}

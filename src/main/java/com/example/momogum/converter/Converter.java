package com.example.momogum.converter;

public interface Converter<S, T> {
    T convert(S source);
}

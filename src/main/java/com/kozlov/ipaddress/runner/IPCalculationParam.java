package com.kozlov.ipaddress.runner;

public interface IPCalculationParam {

    int DEFAULT_BUFFER_SIZE = 1024 * 1024;

    String getPath();

    int getBufferSize();

}

package com.kozlov.ipaddress.runner;

import java.util.Objects;

import static java.lang.Integer.parseInt;

public class ArgsIPCalcParam implements IPCalculationParam {

    private final String path;
    private final int bufferSize;

    public ArgsIPCalcParam(String[] args) {
        Objects.requireNonNull(args);

        if (args.length < 1) {
            throw new IllegalArgumentException("Parameter not found");
        }

        this.path = args[0];
        if (args.length == 2) {
            try {
                String param = args[1];
                if (param == null || param.length() == 0) {
                    this.bufferSize = DEFAULT_BUFFER_SIZE;
                } else {
                    this.bufferSize = parseInt(param);
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException("Bad format buffer size parameter.", e);
            }
        } else {
            this.bufferSize = DEFAULT_BUFFER_SIZE;
        }
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public int getBufferSize() {
        return bufferSize;
    }

}

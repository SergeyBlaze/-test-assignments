package com.kozlov.ipaddress.v2;

import com.kozlov.ipaddress.IPCalculator;
import com.kozlov.ipaddress.exception.CalculationException;
import com.kozlov.ipaddress.runner.IPCalculationParam;

import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UniqueIPCalcV2 implements IPCalculator {

    private final IPCalculationParam param;
    private final InputStream inputStream;

    public UniqueIPCalcV2(IPCalculationParam param, InputStream inputStream) {
        Objects.requireNonNull(param);
        Objects.requireNonNull(inputStream);
        this.param = param;
        this.inputStream = inputStream;
    }

    @Override
    public int getCount() {
        BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>(64);
        FileReader fileReader = new FileReader(inputStream, queue, param.getBufferSize());

        BufferUniqueIPReader uniqueIPCalc = new BufferUniqueIPReader(queue);

        ThreadGroup group = new ThreadGroup("calculating_group");
        Thread readT = new Thread(group, fileReader);
        Thread countT = new Thread(group, uniqueIPCalc);

        readT.start();
        countT.start();

        try {
            countT.join();
        } catch (InterruptedException e) {
            countT.interrupt();
            throw new CalculationException("Подсчет уникальных значений прерван.", e);
        }
        return uniqueIPCalc.getResult();
    }

}

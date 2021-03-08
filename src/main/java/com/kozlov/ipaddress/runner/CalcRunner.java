package com.kozlov.ipaddress.runner;

import com.kozlov.ipaddress.IPCalculator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;
import java.util.function.BiFunction;

public class CalcRunner implements Runnable {

    private final IPCalculationParam param;

    private final BiFunction<IPCalculationParam, InputStream, IPCalculator> run;

    public CalcRunner(IPCalculationParam param, BiFunction<IPCalculationParam, InputStream, IPCalculator> run) {
        Objects.requireNonNull(param);
        Objects.requireNonNull(run);
        this.param = param;
        this.run = run;
    }

    @Override
    public void run() {
        System.out.println("Start calculating unique ip addresses...");

        System.out.println("Params:");
        System.out.println(" file_path   - " + param.getPath());
        System.out.println(" buffer_size - " + param.getBufferSize());

        try {
            File file = new File(param.getPath());
            InputStream is = new FileInputStream(file);
            System.out.println("Wait...");
            int count = run.apply(param, is).getCount();
            System.out.println("Calculating complete.");
            System.out.println("Result: " + count);
        } catch (FileNotFoundException e) {
            System.out.println("File not fount.");
        } catch (Throwable e) {
            System.out.println("Calculating complete with error...");
            e.printStackTrace();
        }
    }

}

package com.kozlov.ipaddress;

import com.kozlov.ipaddress.runner.ArgsIPCalcParam;
import com.kozlov.ipaddress.runner.CalcRunner;
import com.kozlov.ipaddress.v1.UniqueIPCalcV1;

public class IPCalcV1 {

    /*
     * Test.
     * Windows 10
     * 120gb file
     * SSD: Max read speed          = 550 MB/sec
     * SSD: Average app read speed  = 205 MB/sec
     * JDK: OpenJDK 1.11
     * JVM: -Xms512m -Xmx1g
     * Full file read time:
     * 4m 00s - best
     * 4m 30s - worst
     * Read time count distinct:
     * 13m 30s - best
     * 14m 20s - worst
     *
     * found: 1000000000
     * count of file reading for best time: 10
     * */
    public static void main(String[] args) {
        new CalcRunner(new ArgsIPCalcParam(args), UniqueIPCalcV1::new).run();
    }

}

package com.kozlov.ipaddress;

import com.kozlov.ipaddress.runner.ArgsIPCalcParam;
import com.kozlov.ipaddress.runner.CalcRunner;
import com.kozlov.ipaddress.v2.UniqueIPCalcV2;

public class IPCalcV2 {

    /*
     * Test.
     * Windows 10
     * 120gb file
     * SSD: Max read speed          = 550 MB/sec
     * SSD: Average app read speed  = 220 MB/sec
     * JDK: OpenJDK 1.11
     * JVM: -Xms512m -Xmx1g
     * Full file read time:
     * 4m 00s - best
     * 4m 30s - worst
     * Read time count distinct:
     * 8m 20m - best
     * 8m 50m - worst
     *
     * found: 1000000000
     * count of file reading for best time: 10
     * */
    public static void main(String[] args) {
        new CalcRunner(new ArgsIPCalcParam(args), UniqueIPCalcV2::new).run();
    }

}

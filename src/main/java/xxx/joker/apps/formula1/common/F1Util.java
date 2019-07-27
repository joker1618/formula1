package xxx.joker.apps.formula1.common;

import xxx.joker.libs.core.format.JkOutput;

import java.text.NumberFormat;

public class F1Util {

    public static String safePrint(Double d) {
        NumberFormat nf = JkOutput.getNumberFmtEN(0, 2);
        return d == null || d == 0d ? "" : nf.format(d.doubleValue());
    }
}

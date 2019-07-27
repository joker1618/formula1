package xxx.joker.libs.repository.dao;

import xxx.joker.libs.core.types.JkFormattable;
import xxx.joker.libs.core.utils.JkStrings;

import static xxx.joker.libs.core.utils.JkStrings.strf;

class DaoFK implements JkFormattable<DaoFK> {
//public class DaoFK {

    private static final String SEP = "-";

    private long fromID;
    private long depID;
    private String fieldName;

    public DaoFK() {

    }
    public DaoFK(long fromID, long depID, String fieldName) {
        this.fromID = fromID;
        this.fieldName = fieldName;
        this.depID = depID;
    }

    public long getFromID() {
        return fromID;
    }
    public String getFieldName() {
        return fieldName;
    }
    public long getDepID() {
        return depID;
    }

    @Override
    public String format() {
        return strf("{}{}{}{}{}", fromID, SEP, depID, SEP, fieldName);
    }

    @Override
    public DaoFK parse(String s) {
        String[] split = JkStrings.splitArr(s, SEP);
        fromID = Long.parseLong(split[0]);
        depID = Long.parseLong(split[1]);
        fieldName = split[2];
        return this;
    }
}

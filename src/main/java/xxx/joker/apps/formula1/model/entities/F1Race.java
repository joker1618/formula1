package xxx.joker.apps.formula1.model.entities;

import xxx.joker.libs.core.datetime.JkDuration;
import xxx.joker.libs.core.tests.JkTests;
import xxx.joker.libs.datalayer.design.RepoEntity;
import xxx.joker.libs.datalayer.design.EntityField;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class F1Race extends RepoEntity {

    @EntityField
    private String gpPK;
    @EntityField
    private Integer pos;
    @EntityField
    private Integer startGrid;
    @EntityField
    private F1Entrant entrant;
    @EntityField
    private Integer laps;
    @EntityField
    private F1RaceOutcome outcome;
    @EntityField
    private JkDuration time;
    @EntityField
    private Double points;

    public F1Race() {
    }

    @Override
    public String getPrimaryKey() {
        return strf("%s-race-%02d", gpPK, pos);
    }

    public String getGpPK() {
        return gpPK;
    }

    public void setGpPK(String gpPK) {
        this.gpPK = gpPK;
    }

    public F1Entrant getEntrant() {
        return entrant;
    }

    public void setEntrant(F1Entrant entrant) {
        this.entrant = entrant;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    public Integer getStartGrid() {
        return startGrid;
    }

    public void setStartGrid(Integer startGrid) {
        this.startGrid = startGrid;
    }

    public Integer getLaps() {
        return laps;
    }

    public void setLaps(Integer laps) {
        this.laps = laps;
    }

    public F1RaceOutcome getOutcome() {
        return outcome;
    }

    public void setOutcome(F1RaceOutcome outcome) {
        this.outcome = outcome;
    }

    public Double getPoints() {
        return points == null ? 0d : points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public JkDuration getTime() {
        return time;
    }

    public void setTime(JkDuration time) {
        this.time = time;
    }

    public enum F1RaceOutcome {
        EXCLUDED("EX"),
        NOT_QUALIFIED("DNQ"),
        OTHER("DNPQ"),
        NOT_CLASSIFIABLE("NC"),
        DISQUALIFIED("DSQ"),
        RETIRED("RET"),
        NOT_STARTED("DNS"),
        WITHDRAW("WD"),
        FINISHED
        ;

        private String label;

        F1RaceOutcome() {
            label = "";
        }

        F1RaceOutcome(String label) {
            this.label = label;
        }

        public static F1RaceOutcome byLabel(String label) {
            if(JkTests.isInt(label))    return FINISHED;
            if(JkTests.isInt(label.replaceAll("[ (].*", "")) && JkTests.isInt(label.replaceAll(".*\\(|\\)*", ""))){
                return FINISHED;
            }

            for (F1RaceOutcome ro : values()) {
                if(label.equalsIgnoreCase(ro.label)) {
                    return ro;
                }
            }

            return null;
        }

        public String getLabel() {
            return label;
        }
    }
}

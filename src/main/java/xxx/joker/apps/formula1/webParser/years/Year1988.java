package xxx.joker.apps.formula1.webParser.years;

import org.apache.commons.lang3.StringUtils;
import xxx.joker.apps.formula1.model.entities.*;
import xxx.joker.apps.formula1.webParser.AWikiParser2018;
import xxx.joker.libs.core.datetime.JkDuration;
import xxx.joker.libs.core.exception.JkRuntimeException;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.scanners.JkScanners;
import xxx.joker.libs.core.scanners.JkTag;
import xxx.joker.libs.core.utils.JkConvert;
import xxx.joker.libs.core.utils.JkStruct;

import java.util.*;

public class Year1988 extends AWikiParser2018 {


    public Year1988() {
        super(1988);
    }

    @Override
    protected void parseEntrants(String html) {
        JkTag tableEntrants = JkScanners.parseHtmlTag(html, "table", "<span class=\"mw-headline\" id=\"Teams_and_drivers\">", "<table class=\"wikitable\"");
        if (tableEntrants == null) {
            tableEntrants = JkScanners.parseHtmlTag(html, "table", "<span class=\"mw-headline\" id=\"Drivers_and_constructors\">", "<table class=\"wikitable\"");
        }
        JkTag tbody = tableEntrants.getChild("tbody");

        F1Entrant previous = null;
        for (JkTag tr : tbody.getChildren("tr")) {
            List<JkTag> tdList = tr.getChildren("td");

            if (tdList.size() == tr.getChildren().size() && tdList.size() == 2) {
                JkTag spanTag;
                JkTag aTag;
                JkTag chTag = tr.getChild(0).getChild("span");
                if (chTag.getAttribute("class").equals("nowrap")) {
                    spanTag = chTag.getChild("span");
                    aTag = chTag.getChild("a");
                } else {
                    spanTag = chTag;
                    aTag = tr.getChild(0).getChild("a");
                }

                F1Driver d = retrieveDriver(aTag.getAttribute("title"), true);
                if (StringUtils.isBlank(d.getCountry())) {
                    JkTag img = spanTag.findFirstTag("img");
                    d.setCountry(fixCountry(img.getAttribute("alt")));
                    checkNation(d, d.getCountry());
                    parseDriverPage(d, aTag);
                }

                F1Entrant e = new F1Entrant();
                e.setYear(year);
                e.setTeam(previous.getTeam());
                e.setCarNo(previous.getCarNo());
                e.setDriver(d);
                model.add(e);

                previous = e;

            } else if (tdList.size() == tr.getChildren().size() && tdList.size() == 3) {
                int carNum = JkConvert.toInt(tr.getChild(0).getText(), -1);
                if (year == 1988 && "1988-Dallara-Alex Caffi-36".equals(previous.getPrimaryKey()) && carNum == -1 && model.getEntrants(1988).size() == 36)
                    continue;

                JkTag spanTag;
                JkTag aTag;
                JkTag chTag = tr.getChild(1).getChild("span");
                if (chTag.getAttribute("class").equals("nowrap")) {
                    spanTag = chTag.getChild("span");
                    aTag = chTag.getChild("a");
                } else {
                    spanTag = chTag;
                    aTag = tr.getChild(1).getChild("a");
                }

                F1Driver d = retrieveDriver(aTag.getAttribute("title"), true);
                if (StringUtils.isBlank(d.getCountry())) {
                    JkTag img = spanTag.findFirstTag("img");
                    d.setCountry(fixCountry(img.getAttribute("alt")));
                    checkNation(d, d.getCountry());
                    parseDriverPage(d, aTag);
                }

                F1Entrant e = new F1Entrant();
                e.setYear(year);
                e.setTeam(previous.getTeam());
                e.setCarNo(carNum);
                e.setDriver(d);
                model.add(e);

                previous = e;

            } else if (tdList.size() >= 6) {
                JkTag tagTeamName = tr.getChild(1).walkFirstChild("a", "span a");
                F1Team team = retrieveTeam(tagTeamName.getText(), true);
                if (StringUtils.isBlank(team.getCountry())) {
                    JkTag img = tr.getChild(0).findFirstTag("img");
                    team.setCountry(fixCountry(img.getAttribute("alt")));
                    checkNation(team, team.getCountry());
                }

                int carNum = Integer.valueOf(tr.getChild(5).getText());

                JkTag spanTag;
                JkTag aTag;
                JkTag chTag = tr.getChild(6).getChild("span");
                if (chTag.getAttribute("class").equals("nowrap")) {
                    spanTag = chTag.getChild("span");
                    aTag = chTag.getChild("a");
                } else {
                    spanTag = chTag;
                    aTag = tr.getChild(6).getChild("a");
                }

                F1Driver d = retrieveDriver(aTag.getAttribute("title"), true);
                if (StringUtils.isBlank(d.getCountry())) {
                    JkTag img = spanTag.findFirstTag("img");
                    d.setCountry(fixCountry(img.getAttribute("alt")));
                    checkNation(d, d.getCountry());
                    parseDriverPage(d, aTag);
                }

                F1Entrant e = new F1Entrant();
                e.setYear(year);
                e.setTeam(team);
                e.setCarNo(carNum);
                e.setDriver(d);
                model.add(e);

                previous = e;
            }
        }

    }

    @Override
    protected List<String> getGpUrls(String html) {
        JkTag tableEntrants = JkScanners.parseHtmlTag(html, "table", "<span class=\"mw-headline\" id=\"Grands_Prix\">", "<table class=\"wikitable");
        JkTag tbody = tableEntrants.getChild("tbody");

        List<JkTag> thList = tbody.getChild(0).getChildren();
        Map<String, Integer> posMap = new HashMap<>();
        for(int i = 0; i < thList.size(); i++) {
            posMap.put(thList.get(i).getTextFlat(), i);
        }

        List<String> urls = new ArrayList<>();
        for (JkTag tr : tbody.getChildren("tr")) {
            List<JkTag> chlist = tr.getChildren();
            int tdNum = tr.getChildren("td").size();
            int thNum = tr.getChildren("th").size();
            if(thNum == 1 && tdNum == posMap.size() - 1 && chlist.size() == posMap.size()) {
                JkTag a = chlist.get(posMap.get("Report")).getChild("a");
                urls.add(a.getAttribute("href"));
            }
        }

        return urls;
    }

    @Override
    protected Map<String, Double> getExpectedDriverPoints(String html) {
        Map<String, Double> map = new LinkedHashMap<>();

        JkTag tableEntrants = JkScanners.parseHtmlTag(html, "table", "<span class=\"mw-headline\" id=\"World_Drivers'_Championship_–_final_standings\">"
                , "<table>", "<table class=\"wikitable\"");
        JkTag tbody = tableEntrants.getChild("tbody");

        for (JkTag tr : tbody.getChildren("tr")) {
            if(tr.getChildren("th").size() == 1 && tr.getChildren().get(0).getTagName().equals("th") && tr.getChildren().size() > 1) {
                JkTag dTag = tr.getChild(1).walkFirstChild("a", "span a");
                F1Driver driver = retrieveDriver(dTag.getText(), false);
                String spoints = JkStruct.getLastElem(tr.getChildren()).getChild("b").getText();
                spoints = spoints.replaceAll("\\(.*", "").trim();
                double points = Double.parseDouble(spoints);
                map.put(driver.getFullName(), points);
            }
        }

        return map;
    }

    @Override
    protected Map<String, Double> getExpectedTeamPoints(String html) {
        Map<String, Double> map = new LinkedHashMap<>();

        JkTag tableEntrants = JkScanners.parseHtmlTag(html, "table", "<span class=\"mw-headline\" id=\"World_Constructors'_Championship_final_standings\">");
        JkTag tbody = tableEntrants.getChild("tbody");

        for (JkTag tr : tbody.getChildren("tr")) {
            if(tr.getChildren("th").size() == 1 && tr.getChildren().get(0).getTagName().equals("th")) {
                JkTag teamTag = tr.getChild(1).walkFirstChild("a", "span a");
                F1Team team = retrieveTeam(teamTag.getText(), false);
                String spoints = JkStruct.getLastElem(tr.getChildren()).getChild("b").getText();
                spoints = spoints.replaceAll("\\(.*\\)", "").trim();
                map.put(team.getTeamName(), Double.parseDouble(spoints));
            }
        }

        return map;
    }

    @Override
    protected void parseQualify(String html, F1GranPrix gp) {
        JkTag tableQualify = JkScanners.parseHtmlTag(html, "table", "<span class=\"mw-headline\" id=\"Qualifying_2\">", "<table class=\"wikitable");
        if(tableQualify == null) {
            tableQualify = JkScanners.parseHtmlTag(html, "table", "<span class=\"mw-headline\" id=\"Qualifying\">", "<table class=\"wikitable");
        }

        JkTag tbody = tableQualify.getChild("tbody");

        List<JkTag> thList = tbody.getChild(0).getChildren();
        Map<String, Integer> posMap = new HashMap<>();
        for(int i = 0; i < thList.size(); i++) {
            String key = thList.get(i).getTextFlat();
            key = key.replaceAll("^No\\.$", "No");
            posMap.put(key, i);
        }

        int pos = 1;

        for (JkTag tr : tbody.getChildren("tr")) {
            int thNum = tr.getChildren("th").size();
            int tdNum = tr.getChildren("td").size();

            if(tdNum >= 5) {
                F1Qualify q = new F1Qualify();
                q.setGpPK(gp.getPrimaryKey());
                q.setPos(pos++);
                gp.getQualifies().add(q);

                int carNo = Integer.parseInt(tr.getChild(posMap.get("No")).getText().replace("‡", ""));
                String dname = tr.getChild(posMap.get("Driver")).walkFirstChild("a", "b a").getText();
                F1Driver d = retrieveDriver(dname, false);
                Integer index = posMap.containsKey("Constructor") ? posMap.get("Constructor") : posMap.get("Team");
                JkTag ttag = tr.getChild(index).walkFirstChild("a", "span a", "b a");
                F1Team team = retrieveTeam(ttag.getText().replaceAll("-$", ""), false);
                if(year==1988 && "Bernd Schneider".equals(d.getFullName())) carNo = 10;

                q.setEntrant(getEntrant(year, d, carNo, team));
                if(q.getEntrant() == null) {
                    if(team.getTeamName().contains("Larrousse")) {
                        team = retrieveTeam("Venturi", false);
                        q.setEntrant(getEntrant(year, d, carNo, team));
                    }
                    if(q.getEntrant() == null) {
                        LOG.error("Entrant[name={}]", dname);
                        throw new JkRuntimeException("Null entrant for {}", q.getPrimaryKey());
                    }
                }

//                Integer fnum = posMap.get("Lap");
//                if(fnum == null)    fnum = posMap.get("Time");
//                if(fnum == null)    fnum = posMap.get("Lap Time");
//                JkTag chTime = tr.getChild(fnum);
//                q.getTimes().add(getQualTime(chTime));


                JkDuration q1Time = null;
                for (String key : Arrays.asList("Q1 Time", "Q1 time", "Q1")) {
                    Integer idx = posMap.get(key);
                    if(idx != null) {
                        q1Time = getQualTime(tr.getChild(idx));
                    }
                }
                boolean reduced = false;
                if(q1Time == null && posMap.containsKey("Time")) {
                    q1Time = getQualTime(tr.getChild(posMap.get("Time")));
                    reduced = true;
                }
                q.getTimes().add(q1Time);

                if(!reduced) {
                    JkDuration q2Time = null;
                    for (String key : Arrays.asList("Q2 Time", "Q2 time", "Q2")) {
                        Integer idx = posMap.get(key);
                        if (idx != null) {
                            q2Time = getQualTime(tr.getChild(idx));
                        }
                    }
                    q.getTimes().add(q2Time);
                }

                JkDuration q3Time = getQualTime(tr.getChild(posMap.get("Gap")));
                q.getTimes().add(q3Time);
            }
        }

        if(gp.getQualifies().isEmpty()) {
            throw new JkRuntimeException("no qualifies for {}", gp);
        }

    }
    private JkDuration getQualTime(JkTag tag) {
        JkTag t = tag.getChild("b");
        if(t == null) {
            t = tag;
        }
        if(t.getTextFlat().startsWith("&")) return null;
        return parseDuration(t.getTextFlat());
    }

    @Override
    protected void parseRace(String html, F1GranPrix gp) {
        JkTag tableRace = JkScanners.parseHtmlTag(html, "table", "<span class=\"mw-headline\" id=\"Race_3\">Race</span>", "<table class=\"wikitable");
//        if(tableRace == null) tableRace = JkScanners.parseHtmlTag(html, "table", "<span class=\"mw-headline\" id=\"Race\">", "<table class=\"wikitable");
        if(tableRace == null) tableRace = JkScanners.parseHtmlTag(html, "table", "<span class=\"mw-headline\" id=\"Race_2\">Race</span>", "<table class=\"wikitable");
        if(tableRace == null) tableRace = JkScanners.parseHtmlTag(html, "table", "<span class=\"mw-headline\" id=\"Race\">Race</span>", "<table class=\"wikitable");

        JkTag tbody = tableRace.getChild("tbody");

        List<JkTag> thList = tbody.getChild(0).getChildren();
        Map<String, Integer> posMap = new HashMap<>();
        for(int i = 0; i < thList.size(); i++) {
            String key = thList.get(i).getTextFlat();
            key = key.replaceAll("^No\\.$", "No").replaceAll("Retired&.*$", "Retired");
            posMap.put(key, i);
        }

        Map<Integer, F1Qualify> qualifyMap = JkStreams.toMapSingle(gp.getQualifies(), q -> q.getEntrant().getCarNo());
        int pos = 1;

        for (JkTag tr : tbody.getChildren("tr")) {
            int tdNum = tr.getChildren("td").size();
            int thNum = tr.getChildren("th").size();
            if(thNum == 1 && tdNum == 7) {
                int carNum = Integer.parseInt(tr.getChild(posMap.get("No")).getText().replace("‡", ""));
                F1Qualify q = qualifyMap.remove(carNum);
                if(q != null) {

                    F1Race r = new F1Race();
                    r.setGpPK(gp.getPrimaryKey());
                    r.setPos(pos++);
                    gp.getRaces().add(r);

                    String outcome = tr.getChild(0).getText().replaceAll("[†|‡]", "").trim();
                    r.setOutcome(F1Race.F1RaceOutcome.byLabel(outcome));

                    r.setStartGrid(JkConvert.toInt(tr.getChild(posMap.get("Grid")).getText(), -1));
                    r.setEntrant(q.getEntrant());
                    q.setFinalGrid(r.getStartGrid());

                    r.setLaps(JkConvert.toInt(tr.getChild(posMap.get("Laps")).getText(), 0));

                    Integer index = posMap.get("Time/Retired");
                    if(index == null) index = posMap.get("Time/retired");
                    r.setTime(parseDuration(tr.getChild(index).getText()));
                    if (gp.getRaces().size() > 1 && r.getTime() != null) {
                        F1Race firstRace = gp.getRaces().get(0);
                        JkDuration ft = firstRace.getTime().plus(r.getTime());
                        r.setTime(ft);
                    }

                    JkTag lastChild = tr.getChild(posMap.get("Points"));
                    if (lastChild.getChild("b") == null) {
                        r.setPoints(JkConvert.toDouble(lastChild.getText(), 0d));
                    } else {
                        r.setPoints(JkConvert.toDouble(lastChild.getChild("b").getText(), 0d));
                    }
                }
            }
        }

        qualifyMap.values().forEach(q -> {
            if(q.getFinalGrid() == null) {
                q.setFinalGrid(gp.getQualifies().indexOf(q)+1);
            }
        });

        LOG.info("Gp {}: num races {}", gp, gp.getRaces().size());
    }

}

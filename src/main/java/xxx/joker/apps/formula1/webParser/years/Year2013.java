package xxx.joker.apps.formula1.webParser.years;

import org.apache.commons.lang3.StringUtils;
import xxx.joker.apps.formula1.model.entities.*;
import xxx.joker.apps.formula1.webParser.AWikiParser2018;
import xxx.joker.libs.core.datetime.JkDuration;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.scanners.JkScanners;
import xxx.joker.libs.core.scanners.JkTag;
import xxx.joker.libs.core.utils.JkConvert;
import xxx.joker.libs.core.utils.JkStruct;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Year2013 extends AWikiParser2018 {


    public Year2013() {
        super(2013);
    }

    @Override
    protected void parseEntrants(String html) {
        JkTag tableEntrants = JkScanners.parseHtmlTag(html, "table", "<span class=\"mw-headline\" id=\"Teams_and_drivers\">", "<table class=\"wikitable\"");
        JkTag tbody = tableEntrants.getChild("tbody");

        F1Entrant previous = null;
        for (JkTag tr : tbody.getChildren("tr")) {
            List<JkTag> tdList = tr.getChildren("td");

            if(tdList.size() == tr.getChildren().size() && tdList.size() == 2) {
                JkTag spanTag;
                JkTag aTag;
                JkTag chTag = tr.getChild(0).getChild("span");
                if(chTag.getAttribute("class").equals("nowrap")) {
                    spanTag = chTag.getChild("span");
                    aTag = chTag.getChild("a");
                } else  {
                    spanTag = chTag;
                    aTag = tr.getChild(0).getChild("a");
                }

                F1Driver d = retrieveDriver(aTag.getAttribute("title"), true);
                if(StringUtils.isBlank(d.getCountry())) {
                    JkTag img = spanTag.findFirstTag("img");
                    d.setCountry(img.getAttribute("alt"));
                    checkNation(d, d.getCountry());
                    parseDriverPage(d, aTag);
                }

                F1Entrant e = new F1Entrant();
                e.setYear(year);
                e.setTeam(previous.getTeam());
                e.setCarNo(previous.getCarNo());
                e.setDriver(d);
                model.add(e);

            } else if(tdList.size() == tr.getChildren().size() && tdList.size() == 3) {
                int carNum = Integer.valueOf(tr.getChild(0).getText());

                JkTag spanTag;
                JkTag aTag;
                JkTag chTag = tr.getChild(1).getChild("span");
                if(chTag.getAttribute("class").equals("nowrap")) {
                    spanTag = chTag.getChild("span");
                    aTag = chTag.getChild("a");
                } else  {
                    spanTag = chTag;
                    aTag = tr.getChild(1).getChild("a");
                }

                F1Driver d = retrieveDriver(aTag.getAttribute("title"), true);
                if(StringUtils.isBlank(d.getCountry())) {
                    JkTag img = spanTag.findFirstTag("img");
                    d.setCountry(img.getAttribute("alt"));
                    checkNation(d, d.getCountry());
                    parseDriverPage(d, aTag);
                }

                F1Entrant e = new F1Entrant();
                e.setYear(year);
                e.setTeam(previous.getTeam());
                e.setCarNo(carNum);
                e.setDriver(d);
                model.add(e);
                previous = null;

            } else if(tdList.size() >= 7) {
                JkTag tagTeamName = tr.getChild(1).walkFirstChild("a");
                F1Team team = retrieveTeam(tagTeamName.getText(), true);
                if(StringUtils.isBlank(team.getCountry())) {
                    JkTag img = tr.getChild(0).findFirstTag("img");
                    team.setCountry(img.getAttribute("alt"));
                    checkNation(team, team.getCountry());
                }

                int carNum = Integer.valueOf(tr.getChild(4).getText());

                JkTag spanTag;
                JkTag aTag;
                JkTag chTag = tr.getChild(5).getChild("span");
                if(chTag.getAttribute("class").equals("nowrap")) {
                    spanTag = chTag.getChild("span");
                    aTag = chTag.getChild("a");
                } else  {
                    spanTag = chTag;
                    aTag = tr.getChild(5).getChild("a");
                }

                F1Driver d = retrieveDriver(aTag.getAttribute("title"), true);
                if(StringUtils.isBlank(d.getCountry())) {
                    JkTag img = spanTag.findFirstTag("img");
                    d.setCountry(img.getAttribute("alt"));
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

        List<String> urls = new ArrayList<>();
        for (JkTag tr : tbody.getChildren("tr")) {
            List<JkTag> tdList = tr.getChildren("td");
            if(tdList.size() == 6) {
                JkTag a = tdList.get(5).getChild("a");
                urls.add(a.getAttribute("href"));
            }
        }

        return urls;
    }

    @Override
    protected Map<String, Double> getExpectedDriverPoints(String html) {
        Map<String, Double> map = new LinkedHashMap<>();

        JkTag tableEntrants = JkScanners.parseHtmlTag(html, "table", "<span class=\"mw-headline\" id=\"World_Drivers'_Championship_standings\">"
                , "<table class=\"wikitable\"", "<table>", "<table class=\"wikitable\"");
        JkTag tbody = tableEntrants.getChild("tbody");

        for (JkTag tr : tbody.getChildren("tr")) {
            if(tr.getChildren("th").size() == 2) {
                JkTag dTag = tr.getChild(1).walkFirstChild("a", "span a");
                F1Driver driver = retrieveDriver(dTag.getText(), false);
                String spoints = JkStruct.getLastElem(tr.getChildren()).getText();
                double points = Double.parseDouble(spoints);
                map.put(driver.getFullName(), points);
            }
        }

        return map;
    }

    @Override
    protected Map<String, Double> getExpectedTeamPoints(String html) {
        Map<String, Double> map = new LinkedHashMap<>();

        JkTag tableEntrants = JkScanners.parseHtmlTag(html, "table", "<span class=\"mw-headline\" id=\"World_Constructors'_Championship_standings\">", "<table class=\"wikitable\"");
        JkTag tbody = tableEntrants.getChild("tbody");

        for (JkTag tr : tbody.getChildren("tr")) {
            if(tr.getChildren("th").size() <= 2 && tr.getChild(0).getTagName().equals("th")) {
                JkTag teamTag = tr.getChild(1).walkFirstChild("a", "span a");
                F1Team team = retrieveTeam(teamTag.getText(), false);
                JkTag last = JkStruct.getLastElem(tr.getChildren());
                String spoints = last.getTagName().equals("th") ? last.getText() : last.getChild("b").getText();
                spoints = spoints.replaceAll(".*\\(|\\).*", "");
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

        int pos = 1;

        for (JkTag tr : tbody.getChildren("tr")) {
            int tdNum = tr.getChildren("td").size();
            if(tr.getChildren("th").size() == 1 && (tdNum >= 6 && tdNum <= 8)) {
                F1Qualify q = new F1Qualify();
                q.setGpPK(gp.getPrimaryKey());
                q.setPos(pos++);
                gp.getQualifies().add(q);

                int counter = 4;

                F1Driver d = super.retrieveDriver(tr.getChild(2).walkFirstChild("a").getText(), false);
                F1Entrant entrant = JkStreams.findUnique(model.getEntrants(year), e -> d.equals(e.getDriver()));
                q.setEntrant(entrant);

                q.getTimes().add(parseDuration(tr.getChild(counter++).getTextFlat()));
                q.getTimes().add(parseDuration(tr.getChild(counter++).getTextFlat()));
                q.getTimes().add(parseDuration(tr.getChild(counter++).getTextFlat()));

                q.setFinalGrid(JkConvert.toInt(tr.getChild(counter).getText(), -1));
            }
        }
    }

    @Override
    protected void parseRace(String html, F1GranPrix gp) {
        JkTag tableRace = JkScanners.parseHtmlTag(html, "table", "<span class=\"mw-headline\" id=\"Race_2\">", "<table class=\"wikitable\"");
        if(tableRace == null) {
            tableRace = JkScanners.parseHtmlTag(html, "table", "<span class=\"mw-headline\" id=\"Race\">", "<table class=\"wikitable");
        }
        JkTag tbody = tableRace.getChild("tbody");

        Map<Integer, F1Qualify> qualifyMap = JkStreams.toMapSingle(gp.getQualifies(), q -> q.getEntrant().getCarNo());
        int pos = 1;

        for (JkTag tr : tbody.getChildren("tr")) {
            int tdNum = tr.getChildren("td").size();
            if(tr.getChildren("th").size() == 1 && (tdNum == 7 || tdNum == 8)) {
                F1Race r = new F1Race();
                r.setGpPK(gp.getPrimaryKey());
                r.setPos(pos++);
                gp.getRaces().add(r);

                String outcome = tr.getChild(0).getText().replaceAll("[†|‡]", "").trim();
				r.setOutcome(F1Race.F1RaceOutcome.byLabel(outcome));


                int carNum = Integer.parseInt(tr.getChild(1).getText());
                F1Qualify q = qualifyMap.get(carNum);
                r.setStartGrid(q.getFinalGrid());
                r.setEntrant(q.getEntrant());

                int counter = tdNum == 7 ? 4 : 5;

                r.setLaps(Integer.parseInt(tr.getChild(counter++).getText()));

                r.setTime(parseDuration(tr.getChild(counter++).getText()));
                if(gp.getRaces().size() > 1 && r.getTime() != null) {
                    F1Race firstRace = gp.getRaces().get(0);
                    JkDuration ft = firstRace.getTime().plus(r.getTime());
                    r.setTime(ft);
                }

                counter++;
                r.setPoints(JkConvert.toDouble(tr.getChild(counter).getTextFlat(), 0d));
            }
        }
    }

}

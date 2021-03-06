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

public class Year2011 extends AWikiParser2018 {


    public Year2011() {
        super(2011);
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
                JkTag firstTd = tr.getChild(0);
                JkTag chTag = firstTd.getChild("span");
                if(chTag.getAttribute("class").equals("nowrap")) {
                    spanTag = chTag.getChild("span");
                    aTag = chTag.getChild("a");
                } else  {
                    spanTag = chTag;
                    aTag = firstTd.getChild("a");
                    if(aTag == null) {
                        aTag = firstTd.getChild("span", "class=nowrap").getChild("a");
                    }
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

                previous = e;

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
//        List<F1Entrant> elist = model.getEntrants(year);
//        elist.sort(Comparator.comparing(F1Entrant::getCarNo));
//        System.out.println(RepoUtil.formatEntities(elist));
//        System.exit(1);
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

//    @Override
//    protected void parseGpDetails(String html, F1GranPrix gp) {
//        JkTag tableGp = JkScanners.parseHtmlTag(html, "table", "<table class=\"infobox vevent\"");
//        JkTag tbody = tableGp.getChild("tbody");
//
//        F1FastLap fastLap = new F1FastLap();
//        int seeker = 0;
//
//        for (JkTag tr : tbody.getChildren("tr")) {
//            if(seeker == 0) {
//                // find track map image
//                if(tr.getChildren().size() == 1) {
//                    JkTag aTag = tr.getChild(0).getChild("a");
//                    if(aTag != null && aTag.getChild("img") != null) {
//                        downloadTrackMap(gp, aTag);
//                        seeker++;
//                    }
//                }
//
//            } else if(seeker == 1) {
//                if(tr.getChildren().size() == 2) {
//                    if(tr.getChild(0).getText().equals("Location")) {
//                        F1Circuit f1Circuit = null;
//                        if(tr.getChild(1).getChildren().size() == 1) {
//                            String tdText = tr.walkFirstChild("td a").getText();
//                            if(tdText.equals("Circuit de Monaco")) {
//                                f1Circuit = super.retrieveCircuit("Monte Carlo", "Monaco", true);
//                                gp.setCircuit(f1Circuit);
//                            } else if(tdText.equals("Autódromo José Carlos Pace")) {
//                                f1Circuit = super.retrieveCircuit("São Paulo", "Brazil", true);
//                                gp.setCircuit(f1Circuit);
//                            }
//                        } else {
//                            String allText = tr.getChild(1).getHtmlTag().replaceAll("<br[^<]*?>", ",").replaceAll(",[ ]*?,", ",").replaceAll("<[^<]*?>", "");
//                            List<String> list = JkStrings.splitList(allText, ",", true);
//                            String nation = list.get(list.size() - 1);
//                            String city = JkStreams.join(list.subList(1, list.size() - 1), ", ");
//                            f1Circuit = super.retrieveCircuit(city, nation, true);
//                            gp.setCircuit(f1Circuit);
//                        }
//                        checkNation(f1Circuit, f1Circuit.getCountry());
//                    } else if(tr.getChild(0).getText().equals("Course length")) {
//                        String lenStr = tr.getChild(1).getText().replaceAll("[ ]*km.*", "").trim();
//                        gp.setLapLength(Double.parseDouble(lenStr));
//                    } else if(tr.getChild(0).getText().equals("Distance")) {
//                        String numStr = tr.getChild(1).getText().replaceAll("[ ]*laps.*", "").trim();
//                        gp.setNumLapsRace(Integer.parseInt(numStr));
//                    } else if(tr.getChild(0).getText().equals("Date")) {
//                        String attrValue = tr.getChild(1).getChild("span").getAttribute("title");
//                        if(attrValue == null) {
//                            attrValue = tr.getChild(1).walkFirstChild("span span").getText();
//                        }
//                        LocalDate date = LocalDate.parse(attrValue, DateTimeFormatter.ISO_LOCAL_DATE);
//                        gp.setDate(date);
//                    }
//                } else if(tr.getChildren().size() == 1) {
//                    if(tr.getChild(0).getTextFlat().equalsIgnoreCase("Fastest lap")) {
//                        seeker++;
//                    }
//                }
//
//            } else if(seeker == 2) {
//                if(tr.getChild(0).getText().equals("Driver")) {
//                    F1Driver d = retrieveDriver(tr.walkFirstChild("td span a").getAttribute("title"), false);
//                    if(d == null) {
//                        d = retrieveDriver(tr.walkFirstChild("td a").getAttribute("title"), false);
//                    }
//                    fastLap.setDriverPK(d.getPrimaryKey());
//
//                } else if(tr.getChild(0).getText().equals("Time")) {
//                    String txt = tr.walkFirstChild("td").getText().replaceAll(" .*", "");
//                    fastLap.setLapTime(parseDuration(txt));
//                    gp.setFastLap(fastLap);
//                    break;
//                }
//            }
//        }
//    }

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

                int carNo = Integer.parseInt(tr.getChild(1).getText());
                F1Driver d = retrieveDriver(tr.getChild(2).walkFirstChild("a").getText(), false);
                JkTag ttag = tr.getChild(3).walkFirstChild("a", "span a");
                F1Team team = retrieveTeam(ttag.getText(), false);
                q.setEntrant(getEntrant(year, d, carNo, team));

                int counter = 4;
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

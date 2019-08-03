package xxx.joker.apps.formula1.webParser;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.model.entities.*;
import xxx.joker.apps.formula1.model.fields.F1FastLap;
import xxx.joker.libs.core.datetime.JkDuration;
import xxx.joker.libs.core.debug.JkDebug;
import xxx.joker.libs.core.exception.JkRuntimeException;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.scanners.JkHtmlChars;
import xxx.joker.libs.core.scanners.JkScanners;
import xxx.joker.libs.core.scanners.JkTag;
import xxx.joker.libs.core.utils.JkStrings;
import xxx.joker.libs.repository.design.RepoEntity;
import xxx.joker.libs.repository.entities.RepoResource;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public abstract class AWikiParser2018 extends AWebParser implements WikiParser {

    protected static final Logger LOG = LoggerFactory.getLogger(AWikiParser2018.class);
    protected int year;

    protected AWikiParser2018(int year) {
        this.year = year;
    }

    /**
     * Add entrants, teams (name and nation) and drivers (name and nation) to repository
     * Add driver web page link
     */
    protected abstract void parseEntrants(String html);
    protected abstract List<String> getGpUrls(String html);
    protected abstract Map<String, Double> getExpectedDriverPoints(String html);
    protected abstract Map<String, Double> getExpectedTeamPoints(String html);

    protected abstract void parseQualify(String html, F1GranPrix gp);
    protected abstract void parseRace(String html, F1GranPrix gp);

    @Override
    public void parseYear() {
        JkDebug.startTimer("Parse entrant");
        parseEntrants(getMainPageHtml());
        JkDebug.stopTimer("Parse entrant");
//       if(1==1)    return;

        Map<String, Double> expDriverPoints = getExpectedDriverPoints(getMainPageHtml());
        List<F1SeasonPoints> plist = JkStreams.map(expDriverPoints.entrySet(), en -> {
            F1SeasonPoints sp = new F1SeasonPoints();
            sp.setYear(year);
            sp.setName(en.getKey());
            sp.setFinalPoints(en.getValue());
            return sp;
        });
        plist.sort(Comparator.comparing(F1SeasonPoints::getFinalPoints));
        for(int i = 0; i < plist.size(); i++) {
            plist.get(i).setFinalPos(i + 1);
        }
        model.addAll(plist);

        Map<String, Double> expTeamPoints = getExpectedTeamPoints(getMainPageHtml());
        plist = JkStreams.map(expTeamPoints.entrySet(), en -> {
            F1SeasonPoints sp = new F1SeasonPoints();
            sp.setYear(year);
            sp.setName(en.getKey());
            sp.setFinalPoints(en.getValue());
            return sp;
        });
        plist.sort(Comparator.comparing(F1SeasonPoints::getFinalPoints));
        for(int i = 0; i < plist.size(); i++) {
            plist.get(i).setFinalPos(i + 1);
        }
        model.addAll(plist);

//        List<Map.Entry<String, Integer>> entriesDriverMap = JkStreams.sorted(expDriverMap.entrySet(), Comparator.comparing(Map.Entry::getValue));
//        String strDrivers = JkStreams.join(entriesDriverMap, "\n", w -> strf("  %-5d%s", w.getValue(), w.getKey()));
//        display("*** Expected driver points ({})\n{}", entriesDriverMap.size(), strDrivers);
//
//        Map<String, Integer> expTeamMap = getExpectedTeamPoints(dwHtml.getHtml(mainPageUrl));
//        List<Map.Entry<String, Integer>> entriesTeamMap = JkStreams.sorted(expTeamMap.entrySet(), Comparator.comparing(Map.Entry::getValue));
//        String strTeams  = JkStreams.join(entriesTeamMap, "\n", w -> strf("  %-5d%s", w.getValue(), w.getKey()));
//        display("*** Expected team points ({})\n{}", entriesTeamMap.size(), strTeams);


        List<String> gpUrls = getGpUrls(getMainPageHtml());
//        gpUrls.forEach(u -> display(u));
//        System.exit(1);

        F1GranPrix firstGp = null;
        for (int i = 0; i < gpUrls.size(); i++) {
//            display(""+i);
            String wikiUrl = createWikiUrl(gpUrls.get(i));
            String html = dwHtml.getHtml(wikiUrl);
            try {
                F1GranPrix gp = new F1GranPrix(year, i);
                if (i == 0) firstGp = gp;
                if (model.add(gp)) {
                    JkDebug.startTimer("Parse GP details");
                    parseGpDetails(html, gp);
                    JkDebug.stopTimer("Parse GP details");

//                display(gp.getCircuit().getCountry());
                    JkDebug.startTimer("Parse qualifies");
                    parseQualify(html, gp);
                    JkDebug.stopTimer("Parse qualifies");

                    JkDebug.startTimer("Parse races");
                    parseRace(html, gp);
                    JkDebug.stopTimer("Parse races");

                    gp.getQualifies().forEach(q -> {
                        if(q.getFinalGrid() == null || q.getFinalGrid() < 1) {
                            q.setFinalGrid(q.getPos());
                            gp.getRaces().stream().filter(r -> r.getEntrant().equals(q.getEntrant())).forEach(r -> r.setStartGrid(q.getPos()));
                        }
                    });

                    if (gp.getNumLapsRace() == null) {
                        gp.setNumLapsRace(gp.getRaces().get(0).getLaps());
                    }
                    if (gp.getQualifies().isEmpty()) {
                        for (int index = 0; index < gp.getRaces().size(); index++) {
                            F1Race r = gp.getRaces().get(index);
                            F1Qualify q = new F1Qualify();
                            q.setGpPK(gp.getPrimaryKey());
                            q.setPos(r.getStartGrid());
                            q.setFinalGrid(r.getStartGrid());
                            q.setEntrant(r.getEntrant());
                            gp.getQualifies().add(q);
                            if (i > 0) {
                                int numRounds = Math.max(firstGp.getQualifies().get(0).getTimes().size(), 0);
                                for (int nr = 0; nr < numRounds; nr++) {
                                    q.getTimes().add(null);
                                }
                            }
                        }
                    }

                }
            } catch (Exception ex) {
                LOG.error("Working on {}", wikiUrl);
                throw new JkRuntimeException(ex);
            }
        }
    }

    @Override
    public Map<String, Double> getExpectedDriverPoints() {
        return getExpectedDriverPoints(getMainPageHtml());

    }

    @Override
    public Map<String, Double> getExpectedTeamPoints() {
        return getExpectedTeamPoints(getMainPageHtml());
    }

    protected RepoResource downloadTrackMap(F1GranPrix gp, JkTag aTag) {
        String url = createWikiUrl(aTag.getAttribute("href"));
        String html = dwHtml.getHtml(url);
        JkTag imgTag = JkScanners.parseHtmlTag(html, "img", "<div class=\"fullImageLink\"", "<a", "<img");
        String imgUrl = createResourceUrl(imgTag);
        Pair<Boolean, Path> dwRes = dwTemp.downloadResource(imgUrl);
        return model.saveGpTrackMap(dwRes.getValue(), gp);
    }

    protected F1Circuit retrieveCircuit(String city, String nation, boolean createIfMissing) {
        nation = fixNation(nation);
        city = fixCity(city);
        F1Circuit circuit = model.getCircuit(city, nation);
        if(circuit == null && createIfMissing) {
            circuit = new F1Circuit(nation, city);
            model.add(circuit);
        }
        return circuit;
    }

    public static String fixNation(String nation) {
        if(nation.contains("Rio de Janeiro"))  return "Brazil";
        if(nation.contains("Ireland"))  return "Ireland";
        if(nation.contains("Monte Carlo"))  return "Monaco";
        if(nation.contains("Indiana") || nation.contains("Arizona"))  return "United States";
        if(nation.contains("Texas") || nation.contains("Michigan"))  return "United States";
        if(nation.contains("Germany"))  return "Germany";
        if(nation.contains("China"))  return "China";
        if(nation.contains("Canada"))  return "Canada";
        if(nation.contains("Lombardy"))  return "Italy";
        if(nation.contains("Monza"))  return "Italy";
        if(nation.contains("England") || nation.contains("Great Britain"))  return "United Kingdom";
        if(nation.contains("Wallonia"))  return "Belgium";
        if(nation.contains("Sepang"))  return "Malaysia";
        if(nation.contains("Mexico City")) return "Mexico";
        if(nation.contains("Melbourne")) return "Australia";
        if(nation.contains("South Australia")) return "Australia";
        return nation;
    }
    private String fixCity(String city) {
        if(city.contains("Mogyoród") || city.contains("Budapest"))  return "Mogyoród";
        if(city.contains("Jacarepaguá"))  return "Rio de Janeiro";
        if(city.contains("Magny-Cours"))  return "Magny-Cours";
        if(city.contains("Melbourne"))  return "Melbourne";
        if(city.contains("Estoril"))     return "Estoril";
        if(city.contains("Austin"))     return "Austin";
        if(city.contains("Imola"))     return "Imola";
        if(city.contains("Monza"))     return "Monza";
        if(city.contains("Montreal"))   return "Montreal";
        if(city.contains("Oyama"))  return "Oyama";
        if(city.contains("Suzuka"))  return "Suzuka";
        if(city.contains("Sepang"))  return "Sepang";
        if(city.contains("Mexico City"))  return "Mexico City";
        if(city.contains("Magdalena Mixhuca"))  return "Mexico City";
        if(city.contains("Abu Dhabi"))  return "Abu Dhabi";
        if(city.contains("Circuit de Monaco") || city.contains("Monte Carlo") || city.contains("Monte-Carlo"))  return "Monte Carlo";
        if(city.contains("Montmel") || city.contains("Valencia"))  return "Barcelona";
        if(city.contains("Sochi"))  return "Sochi";
        if(city.contains("Stavelot"))  return "Spa";
        if(city.contains("Le Castellet"))  return "Le Castellet";
        if(city.contains("Spielberg"))  return "Spielberg";
        if(city.contains("Nürburg") || city.contains("Hockenheim") || city.contains("Baden-Württemberg"))  return "Hockenheim";
        if(city.contains("Northamptonshire"))  return "Silverstone";
        if(city.contains("South Jeolla"))  return "Yeongam";
        if(city.contains("Uttar Pradesh"))  return "Uttar Pradesh";
        if(city.contains("Albert Park"))  return "Melbourne";
        if(city.contains("Adelaide"))  return "Adelaide";
        return city;
    }

    protected F1Team retrieveTeam(String teamName, boolean createIfMissing) {
        String tn = fixTeamName(teamName);
        F1Team team = model.getTeam(tn);
        if(team == null && createIfMissing) {
            team = new F1Team(tn);
            if(model.getTeams().add(team));
        }
        return team;
    }
    private String fixTeamName(String teamName) {
        switch (teamName) {
            case "Scuderia Toro Rosso":     return "Toro Rosso";
            case "Red Bull Racing":         return "Red Bull";
            case "McLaren-Mercedes":        return "McLaren";
            case "Spyker MF1":
            case "MF1":
                return "Midland F1";
            default:    return teamName;
        }
    }

    protected F1Driver retrieveDriver(String driverName, boolean createIfMissing) {
        String dname = fixDriverName(driverName);
        F1Driver driver = model.getDriver(dname);
        if(driver == null && createIfMissing) {
            driver = new F1Driver(dname);
            model.getDrivers().add(driver);
        }
        return driver;
    }
    private String fixDriverName(String driverName) {
        String dn = JkHtmlChars.fixDirtyChars(driverName);
        dn = dn.replace("(racing driver)", "").trim();
        dn = dn.replace("(driver)", "").trim();
        switch (dn) {
            case "Alessandro Zanardi":  return "Alex Zanardi";
            case "Nelson Piquet, Jr.":  return "Nelson Piquet Jr.";
            case "Juan-Pablo Montoya":  return "Juan Pablo Montoya";
        }
        if(dn.startsWith("Carlos Sainz")) {
            return "Carlos Sainz Jr.";
        }
        if(dn.equals("Adrián Campos")) {
            return "Adrian Campos";
        }
        return dn;
    }

    protected void checkNation(RepoEntity e, String nation) {
        F1Country n = model.getCountry(nation);
        if(n == null) {
            throw new JkRuntimeException("Nation [{}] not exists. Entity: {}", nation, e);
        }
    }


    protected F1Entrant getEntrant(int year, F1Driver driver, F1Team team) {
        return JkStreams.findUnique(model.getEntrants(year), e -> e.getDriver().equals(driver), e -> e.getTeam().equals(team));
    }
    protected F1Entrant getEntrant(int year, F1Driver driver, int carNo, F1Team team) {
        return JkStreams.findUnique(model.getEntrants(year), e -> e.getDriver().equals(driver), e -> e.getCarNo() == carNo, e -> e.getTeam().equals(team));
    }

    protected JkDuration parseDuration(String str) {
        String s;
        int idx = str.lastIndexOf(".");
        if(idx != -1) {
            s = str.substring(0, idx).replace(".", ":");
            s += str.substring(idx);
        } else {
            int idx2 = str.lastIndexOf(":");
            if(idx2 != -1) {
                s = str.substring(0, idx2) + "." + str.substring(idx2+1);
            } else {
                s = str;
            }
        }
        return JkDuration.of(s);
    }



    /**
     * Parse:
     * - driver, birth day, birth city, download pic
     */
    protected void parseDriverPage(F1Driver driver, JkTag aTag) {
        parseDriverPage(driver, aTag.getAttribute("href"));
    }
    protected void parseDriverPage(F1Driver driver, String pageUrl) {
        if(checkExceptions(driver, pageUrl)) {
            return;
        }

        String wikiUrl = createWikiUrl(pageUrl);
        String html = dwHtml.getHtml(wikiUrl);

        JkTag tableEntrants = JkScanners.parseHtmlTag(html, "table", "<table class=\"infobox");
        JkTag tbody = tableEntrants.getChild("tbody");

        JkTag aTag = null;
        List<JkTag> trList = tbody.getChildren("tr");
        int rowNum = 0;
        while(aTag == null && rowNum < trList.size()) {
            aTag = tbody.getChild(rowNum, 0).getChild("a", "class=image");
            rowNum++;
        }

        // Driver cover
        if(aTag != null) {
            String url = createWikiUrl(aTag.getAttribute("href"));
            String dhtml = dwHtml.getHtml(url);
            JkTag imgTag = JkScanners.parseHtmlTag(dhtml, "img", "<div class=\"fullImageLink\"", "<a", "<img");
            String imgUrl = createResourceUrl(imgTag);
            Pair<Boolean, Path> dwRes = dwTemp.downloadResource(imgUrl);
            model.saveDriverCover(dwRes.getValue(), driver);
        } else {
            rowNum = 0;
        }

        // Driver details
        JkTag rowBorn = null;
        while (rowBorn == null && rowNum < trList.size()) {
            JkTag tr = tbody.getChild(rowNum);
            JkTag ch = tr.getChild(0);
            if (ch.getTagName().equals("th") && "Born".equals(ch.getText())) {
                rowBorn = tr;
            }
            rowNum++;
        }

        String strBirth = rowBorn.getChild(1).walkFirstChild("span span").getText();
        driver.setBirthDay(LocalDate.parse(strBirth));

        String[] split = rowBorn.getHtmlTag().split("<br[ ]?/>");
        String strCity = split[split.length - 1].replaceAll("<[^<]*?>", "").replaceAll(",[^,]*$", "").trim();
        driver.setCity(strCity);
    }
    protected boolean checkExceptions(F1Driver driver, String pageUrl) {
        if(StringUtils.equalsAny(driver.getFullName(), "Christophe Bouchut")) {
            String wikiUrl = createWikiUrl(pageUrl);
            String html = dwHtml.getHtml(wikiUrl);
            JkTag ptag = JkScanners.parseHtmlTag(html, "p", "<div class=\"mw-parser-output\">", "p");
            String[] split = ptag.getText().replaceAll(".*born", "").trim().split(" ");
            LocalDate ld = LocalDate.parse(strf("{} {} {}", split[0], split[1], split[2]), DateTimeFormatter.ofPattern("dd MMMM yyyy"));
            driver.setBirthDay(ld);
            return true;
        }
        return false;
    }

    protected void parseGpDetails(String html, F1GranPrix gp) {
        JkTag tableGp = JkScanners.parseHtmlTag(html, "table", "<table class=\"infobox vevent\"");
        JkTag tbody = tableGp.getChild("tbody");

        F1FastLap fastLap = new F1FastLap();
        int seeker = 0;

        for (JkTag tr : tbody.getChildren("tr")) {
            if(seeker == 0) {
                // find track map image
                if(tr.getChildren().size() == 1) {
                    JkTag aTag = tr.getChild(0).getChild("a");
                    if(aTag != null && aTag.getChild("img") != null) {
                        downloadTrackMap(gp, aTag);
                        seeker++;
                    }
                }

            } else if(seeker == 1) {
                if(tr.getChildren().size() == 2) {
                    if(tr.getChild(0).getText().equals("Location")) {
                        String allText = tr.getChild(1).getHtmlTag().replaceAll("<br[^<]*?>", ",").replaceAll(",[ ]*?,", ",").replaceAll("<[^<]*?>", "");
                        List<String> list = JkStrings.splitList(allText, ",", true);
                        String nation = null;
                        String city = null;
                        if(list.size() > 2) {
                            nation = list.get(list.size() - 1);
                            city = JkStreams.join(list.subList(1, list.size() - 1), ", ");
                        } else if(list.size() == 2) {
                            nation = list.get(1);
                            city = list.get(0);
                        } else if(list.get(0).equals("Circuit de Monaco")) {
                            nation = "Monaco";
                            city = "Monte Carlo";
                        } else if(list.get(0).equals("Autódromo do Estoril")) {
                            nation = "Portugal";
                            city = "Autódromo do Estoril";
                        } else if(list.get(0).equals("Silverstone Circuit")) {
                            nation = "United Kingdom";
                            city = "Silverstone";
                        } else if(list.get(0).equals("Circuit de Spa-Francorchamps")) {
                            nation = "Belgium";
                            city = "Spa";
                        }
                        F1Circuit f1Circuit = retrieveCircuit(city, nation, true);
                        gp.setCircuit(f1Circuit);
                        checkNation(f1Circuit, f1Circuit.getCountry());
                    } else if(tr.getChild(0).getText().equals("Course length")) {
                        String lenStr = tr.getChild(1).getText().replaceAll("[ ]*km.*", "").trim();
                        gp.setLapLength(Double.parseDouble(lenStr));
                    } else if(tr.getChild(0).getText().equals("Distance")) {
                        String numStr = tr.getChild(1).getText().replaceAll("[ ]*laps.*", "").trim();
                        gp.setNumLapsRace(Integer.parseInt(numStr));
                    } else if(tr.getChild(0).getText().equals("Date")) {
                        String attrValue = tr.getChild(1).getChild("span").getAttribute("title");
                        if(attrValue == null) {
                            attrValue = tr.getChild(1).walkFirstChild("span span").getText();
                        }
                        LocalDate date = LocalDate.parse(attrValue, DateTimeFormatter.ISO_LOCAL_DATE);
                        gp.setDate(date);
                    }
                } else if(tr.getChildren().size() == 1) {
                    if(tr.getChild(0).getTextFlat().equalsIgnoreCase("Fastest lap")) {
                        seeker++;
                    }
                }

            } else if(seeker == 2) {
                if(tr.getChild(0).getText().equals("Driver")) {
                    F1Driver d = retrieveDriver(tr.walkFirstChild("td span a").getAttribute("title"), false);
                    if(d == null && tr.walkFirstChild("td a") != null) {
                        d = retrieveDriver(tr.walkFirstChild("td a").getAttribute("title"), false);
                    }
                    if(d == null) {
                        d = retrieveDriver(tr.walkFirstChild("td").getText(), false);
                    }
                    fastLap.setDriverPK(d.getPrimaryKey());

                } else if(tr.getChild(0).getText().equals("Time")) {
                    String txt = tr.walkFirstChild("td").getText().replaceAll(" .*", "");
                    fastLap.setLapTime(parseDuration(txt));
                    gp.setFastLap(fastLap);
                    break;
                }
            }
        }
    }


    private String createResourceUrl(JkTag img) {
        String srcset = img.getAttribute("srcset");
        if(srcset == null) {
            return strf("https:{}", img.getAttribute("src").trim());
        }
        return strf("https:{}", srcset.replaceAll(" [^ ]+$", "").replaceAll(".*,", "").trim());
    }

    private String getMainPageHtml() {
        String mainPageUrl = createWikiUrl(strf("/wiki/{}_Formula_One_World_Championship", year));
        LOG.info("Main url {}: {}", year, mainPageUrl);
        return dwHtml.getHtml(mainPageUrl);
    }

}

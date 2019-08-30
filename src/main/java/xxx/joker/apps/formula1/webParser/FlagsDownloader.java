package xxx.joker.apps.formula1.webParser;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.model.entities.Country;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.scanners.JkScanners;
import xxx.joker.libs.core.scanners.JkTag;
import xxx.joker.libs.core.utils.JkStrings;
import xxx.joker.libs.datalayer.entities.RepoResource;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class FlagsDownloader extends AWebParser {

    private static final Logger LOG = LoggerFactory.getLogger(FlagsDownloader.class);

    public void downloadCountries() {
        String html = dwHtml.getHtml(createWikiUrl("/wiki/List_of_ISO_3166_country_codes"));

        JkTag tableTag = JkScanners.parseHtmlTag(html, "table", "Current ISO 3166 country codes", "<table class=\"wikitable");
        List<JkTag> trList = tableTag.walkChildren("tbody tr");

        for (JkTag tr : trList) {
            if(tr.getChildren().size() == 8 && StringUtils.containsIgnoreCase(tr.getChild(2).getTextFlat(), "UN member state")) {
                JkTag td = tr.getChild(0);
                JkTag span = td.findFirstTag("span", "class=flagicon");
                JkTag img = span.walkFirstChild("a img");

                String countryName = img.getAttribute("alt");
                countryName = AWikiParser2018.fixCountry(countryName);
                Country country = new Country(countryName);

                boolean add = model.add(country);
                if(!add)    continue;

                LOG.info("Added new country {}", country);

                String iconUrl = getImageUrl(img);
                String code = tr.getChild(4).findFirstTag("span", "class=monospaced").getText();
                country.setCode(code);

                Pair<Boolean, Path> dwRes1 = dwTemp.downloadResource(iconUrl);
                model.saveFlagIcon(dwRes1.getValue(), country);

                String nationPageUrl = createWikiUrl(span.getChild("a"));
                JkTag vcard = JkScanners.parseHtmlTag(dwHtml.getHtml(nationPageUrl), "table", "<table class=\"infobox geography vcard\"");
                List<JkTag> vcardRows = vcard.walkChildren("tbody tr");

                for (JkTag row : vcardRows) {
                    List<JkTag> alist = row.findAllTags("a", "class=image");
                    if(alist.size() >= 1) {
                        String flagPageUrl = createWikiUrl(alist.get(0));
                        JkTag imgTag = JkScanners.parseHtmlTag(dwHtml.getHtml(flagPageUrl), "img", "<div class=\"fullImageLink\"");
                        String imageUrl = getImageUrl(imgTag);

                        Pair<Boolean, Path> dwRes2 = dwTemp.downloadResource(imageUrl);
                        model.saveFlagImage(dwRes2.getValue(), country);

                        break;
                    }
                }
            }
        }
    }

    public void downloadCountryFlags() {
        String html = dwHtml.getHtml(createWikiUrl("/wiki/List_of_ISO_3166_country_codes"));

        JkTag tableTag = JkScanners.parseHtmlTag(html, "table", "Current ISO 3166 country codes", "<table class=\"wikitable");
        List<JkTag> trList = tableTag.walkChildren("tbody tr");

        for (JkTag tr : trList) {
            if(tr.getChildren().size() == 8 && StringUtils.containsIgnoreCase(tr.getChild(2).getTextFlat(), "UN member state")) {
                JkTag td = tr.getChild(0);
                JkTag span = td.findFirstTag("span", "class=flagicon");
                JkTag img = span.walkFirstChild("a img");

                String countryName = img.getAttribute("alt");
                countryName = AWikiParser2018.fixCountry(countryName);
                Country country = new Country(countryName);
                if(!model.add(country))     continue;

                LOG.info("Added new country {}", country);

                String code = tr.getChild(4).findFirstTag("span", "class=monospaced").getText();
                country.setCode(code);

                List<String> imageUrls = getImageUrls(img);
                for(int i = 0; i < imageUrls.size(); i++) {
                    String iconUrl = imageUrls.get(i);
                    Pair<Boolean, Path> dwRes = dwTemp.downloadResource(iconUrl);
                    model.saveFlagIcon(dwRes.getValue(), country);
                }

                String nationPageUrl = createWikiUrl(span.getChild("a"));
                JkTag vcard = JkScanners.parseHtmlTag(dwHtml.getHtml(nationPageUrl), "table", "<table class=\"infobox geography vcard\"");
                List<JkTag> vcardRows = vcard.walkChildren("tbody tr");

                for (JkTag row : vcardRows) {
                    List<JkTag> alist = row.findAllTags("a", "class=image");
                    if(alist.size() >= 1) {
                        String flagPageUrl = createWikiUrl(alist.get(0));
                        JkTag imgTag = JkScanners.parseHtmlTag(dwHtml.getHtml(flagPageUrl), "img", "<div class=\"fullImageLink\"");
                        imageUrls = getImageUrls(imgTag);
                        for(int i = 0; i < imageUrls.size(); i++) {
                            String iconUrl = imageUrls.get(i);
                            Pair<Boolean, Path> dwRes = dwTemp.downloadResource(iconUrl);
                            model.saveFlagImage(dwRes.getValue(), country);
                        }
                        break;
                    }
                }
            }
        }
    }

    protected String getImageUrl(JkTag img) {
        return strf("https:{}", img.getAttribute("srcset").replaceAll(" [^ ]+$", "").replaceAll(".*,", "").trim());
    }

    protected List<String> getImageUrls(JkTag img) {
        List<String> list = new ArrayList<>();
        list.add(strf("https:{}", img.getAttribute("src")));
        List<String> srcset = JkStrings.splitList(img.getAttribute("srcset"), ",", true);
        list.addAll(JkStreams.map(srcset, s -> strf("https:{}", s.substring(0, s.indexOf(" ")))));
        return list;
    }
}

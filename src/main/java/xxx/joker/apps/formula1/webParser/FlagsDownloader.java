package xxx.joker.apps.formula1.webParser;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.formula1.model.entities.F1Country;
import xxx.joker.libs.core.scanners.JkScanners;
import xxx.joker.libs.core.scanners.JkTag;
import xxx.joker.libs.repository.entities.RepoResource;

import java.nio.file.Path;
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
                countryName = AWikiParser2018.fixNation(countryName);
                String iconUrl = getImageUrl(img);
                String code = tr.getChild(4).findFirstTag("span", "class=monospaced").getText();


                F1Country country = model.getCountry(countryName);
                if(country != null)  continue;

                country = new F1Country(countryName);
                model.add(country);
                country.setCode(code);

//                if(countryName.startsWith("B")) break;

                if(country.getFlagIcon() == null) {
                    Pair<Boolean, Path> dwRes = dwTemp.downloadResource(iconUrl);
                    RepoResource iconResource = model.addResource(dwRes.getValue(), countryName, "icon flag");
                    country.setFlagIcon(iconResource);
                }

                if(country.getFlagImage() == null) {
                    String nationPageUrl = createWikiUrl(span.getChild("a"));
                    JkTag vcard = JkScanners.parseHtmlTag(dwHtml.getHtml(nationPageUrl), "table", "<table class=\"infobox geography vcard\"");
                    List<JkTag> vcardRows = vcard.walkChildren("tbody tr");

                    for (JkTag row : vcardRows) {
                        List<JkTag> alist = row.findAllTags("a", "class=image");
                        if(alist.size() >= 1) {
                            String flagPageUrl = createWikiUrl(alist.get(0));
                            JkTag imgTag = JkScanners.parseHtmlTag(dwHtml.getHtml(flagPageUrl), "img", "<div class=\"fullImageLink\"");
                            String imageUrl = getImageUrl(imgTag);

                            Pair<Boolean, Path> dwRes = dwTemp.downloadResource(imageUrl);
                            RepoResource imageURI = model.addResource(dwRes.getValue(), countryName, "image flag");
                            country.setFlagImage(imageURI);

                            break;
                        }
                    }
                }
            }
        }
    }

    protected String getImageUrl(JkTag img) {
        return strf("https:{}", img.getAttribute("srcset").replaceAll(" [^ ]+$", "").replaceAll(".*,", "").trim());
    }
}

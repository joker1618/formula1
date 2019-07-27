package xxx.joker.apps.formula1.webParser;


import xxx.joker.apps.formula1.common.F1Const;
import xxx.joker.apps.formula1.model.F1ModelNew;
import xxx.joker.apps.formula1.model.F1ModelNewImpl;
import xxx.joker.libs.core.scanners.JkTag;
import xxx.joker.libs.core.web.JkDownloader;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public abstract class AWebParser {

    protected static final String PREFIX_URL = "https://en.wikipedia.org";

    protected final JkDownloader dwHtml = new JkDownloader(F1Const.HTML_FOLDER);
    protected final JkDownloader dwTemp = new JkDownloader(F1Const.TMP_FOLDER);

    protected final F1ModelNew model = F1ModelNewImpl.getInstance();

    protected String createWikiUrl(String wikiSubPath) {
        if(wikiSubPath.startsWith(PREFIX_URL))  {
            return wikiSubPath;
        }
        return strf("{}/{}", PREFIX_URL, wikiSubPath.replaceFirst("^/", ""));
    }

    protected String createWikiUrl(JkTag aTag) {
        return createWikiUrl(aTag.getAttribute("href"));
    }


}

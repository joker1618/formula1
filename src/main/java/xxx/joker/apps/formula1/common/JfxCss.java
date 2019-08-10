package xxx.joker.apps.formula1.common;

import javafx.scene.Parent;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.scanners.JkScanners;
import xxx.joker.libs.core.scanners.JkTextScanner;
import xxx.joker.libs.core.utils.JkConvert;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JfxCss {

    public static int retrieveInt(Parent parent, String cssProp) {
        String s = retrieveValue(parent, cssProp);
        return JkConvert.toInt(s, 0);
    }

    public static String retrieveValue(Parent parent, String cssProp) {
        JkTextScanner scanner = JkScanners.getTextScanner(parent.getStyle());
        if(scanner.startAfter(cssProp, ":")) {
            scanner.endAt(";");
            String value = scanner.toString().replaceAll("\n.*", "");
            return value.trim();
        }

        for (String ssheet : parent.getStylesheets()) {
            Path cssFilePath = Paths.get(URI.create(ssheet));
            List<String> lines = JkFiles.readLines(cssFilePath);
            scanner = JkScanners.getTextScanner(JkStreams.joinLines(lines));
            if(scanner.startAfter(cssProp, ":")) {
                scanner.endAt(";");
                String value = scanner.toString().replaceAll("\n.*", "");
                return value.trim();
            }
        }
        return null;
    }
}

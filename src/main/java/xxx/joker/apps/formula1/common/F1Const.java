package xxx.joker.apps.formula1.common;

import java.nio.file.Path;
import java.nio.file.Paths;

public class F1Const {

    public static final Path BASE_FOLDER = Paths.get("XXX_formula1_2");
//    public static final Path BASE_FOLDER = JkEnvironment.getAppsFolder().resolve("formula1_2");

    public static final Path REPO_FOLDER = BASE_FOLDER.resolve("repo");
    public static final Path RSOURCES_FOLDER = REPO_FOLDER.resolve("resources");
    public static final Path DB_FOLDER = REPO_FOLDER.resolve("db");
    public static final String DB_NAME = "f1";

    public static final Path HTML_FOLDER = BASE_FOLDER.resolve("html");
    public static final Path TMP_FOLDER = BASE_FOLDER.resolve("tmp");

}

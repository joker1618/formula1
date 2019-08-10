package xxx.joker.apps.formula1.common;

import xxx.joker.libs.core.runtimes.JkEnvironment;

import java.nio.file.Path;

public class F1Const {

//    public static final Path BASE_FOLDER = Paths.get("XXX_formula1_2");
    public static final Path BASE_FOLDER = JkEnvironment.getAppsFolder().resolve("formula1");

    public static final Path REPO_FOLDER = BASE_FOLDER.resolve("repo");
//    public static final Path REPO_FOLDER = BASE_FOLDER.resolve("repoFull");
//    public static final Path REPO_FOLDER = BASE_FOLDER.resolve("repoMini");
    public static final String DB_NAME = "f1";

    public static final Path HTML_FOLDER = BASE_FOLDER.resolve("html");
    public static final Path TMP_FOLDER = BASE_FOLDER.resolve("tmp");

}

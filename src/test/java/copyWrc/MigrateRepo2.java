package copyWrc;

import org.junit.Test;
import common.CommonTest;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.utils.JkStrings;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.migration.wrc6.nuevo.WrcRepoNew;
import xxx.joker.migration.wrc6.nuevo.WrcRepoNewImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static xxx.joker.libs.core.utils.JkConsole.display;
import static xxx.joker.libs.core.utils.JkStrings.strf;

public class MigrateRepo2 extends CommonTest {

    @Test
    public void migrFiles() throws IOException, ClassNotFoundException {
        Path baseFolder = Paths.get("wrc-data/migrated");
        List<Path> dataPaths = JkFiles.find(Paths.get("wrc-data/repo/wrc"), false, p -> p.toString().endsWith(".data"));
        for (Path dataPath : dataPaths) {
            Path descrPath = Paths.get(dataPath.toString().replaceAll("\\.data$", ".descr"));
            List<String> fieldNames = JkStreams.map(JkFiles.readLines(descrPath), s -> s.replaceAll(":.*", ""));
            String header = JkStreams.join(fieldNames, "|");
            List<String> lines = JkFiles.readLines(dataPath);
            lines.add(0, header);
            String[] split = dataPath.getFileName().toString().split("#");
            Path outpData = baseFolder.resolve(strf("{}#{}#{}", split[0], split[1].replaceAll(".*\\.", ""), split[2]));
            JkFiles.writeFile(outpData, lines);

            Path fkeysPath = Paths.get(dataPath.toString().replaceAll("\\.data$", ".fkeys"));
            if(Files.exists(fkeysPath)) {
                List<String> fklines = JkFiles.readLines(fkeysPath);
                List<String> newFkLines = JkStreams.map(fklines, fkline -> {
                    List<String> lsplit = JkStrings.splitList(fkline, "|");
                    String fname = fieldNames.get(Integer.parseInt(lsplit.get(1)));
                    lsplit.set(1, fname);
                    return JkStreams.join(lsplit, "|");
                });
                Path outpFkeys = Paths.get(outpData.toString().replaceAll("\\.data$", ".fkeys"));
                JkFiles.writeFile(outpFkeys, newFkLines);
            }
        }
    }
    @Test
    public void oldToNew() throws IOException, ClassNotFoundException {
//        WrcRepo repoOld = WrcRepoImpl.getInstance();
        WrcRepoNew repoNew = WrcRepoNewImpl.getInstance();

//        List<RepoEntity> allEntities = new ArrayList<>();
//        repoOld.getDataSets().forEach((clazz,set) -> {
//            String newCName;
//            if(clazz.getName().contains(".libs."))  {
//                newCName = clazz.getName().replaceAll("\\.class$", "");
//            } else {
//                newCName = strf("xxx.joker.migration.wrc6.nuevo.entities." + clazz.getSimpleName());
//            }
//            Class<RepoEntity> aClass = (Class<RepoEntity>)JkReflection.classForName(newCName);
//            List<RepoEntity> list = JkStreams.map(set, el -> JkReflection.copyFields(el, aClass));
//            allEntities.addAll(list);
//        });
//
//        repoNew.initRepoContent(allEntities);
//        repoNew.commit();

        display("END");
    }

}

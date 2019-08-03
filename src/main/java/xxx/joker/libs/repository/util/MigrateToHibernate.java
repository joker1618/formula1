package xxx.joker.libs.repository.util;

import org.apache.commons.lang3.StringUtils;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.core.utils.JkStrings;
import xxx.joker.libs.repository.config.RepoCtx;
import xxx.joker.libs.repository.wrapper.ClazzWrap;
import xxx.joker.libs.repository.wrapper.FieldWrap;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class MigrateToHibernate {

    public static void migrate(Path destFolder, RepoCtx ctx) {
        for (ClazzWrap cw : ctx.getClazzWraps().values()) {
            List<String> newLines = new ArrayList<>();
            newLines.add("import javax.persistence.*;");
            newLines.add("import java.io.Serializable;");
            newLines.add("import java.util.*;");
            newLines.add("import java.time.*;");
            newLines.add("");
            newLines.add("@Entity");
            newLines.add(strf("public class {} extends JpaEntity implements Serializable {", cw.getEClazz().getSimpleName()));
            newLines.add("");
            newLines.add("@Id");
            newLines.add("@GeneratedValue");
            newLines.add("private Long jpaID;");
            newLines.add("");

            for (FieldWrap fw : cw.getFieldWraps()) {
                if(fw.isEntity()) {
                    newLines.add("@ManyToOne");
                } else if(fw.isEntityColl()) {
                    newLines.add("@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})");
                }
                newLines.add(strf("private {} {};", fw.getFieldType().getSimpleName(), fw.getFieldName()));
            }

            newLines.add("");

            newLines.add(strf("public Long getJpaID() {"));
            newLines.add(strf("return jpaID;"));
            newLines.add(strf("}"));

            for (FieldWrap fw : cw.getFieldWraps()) {
                if(fw.isEntity()) {
                    newLines.add("@ManyToOne");
                } else if(fw.isEntityColl()) {
                    newLines.add("@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})");
                }
                newLines.add(strf("public () get{}() {", fw.getFieldType().getSimpleName(), StringUtils.capitalize(fw.getFieldName())));
                newLines.add(strf("return {};", fw.getFieldName()));
                newLines.add(strf("}"));
                newLines.add(strf("public void set{}({} {}) {", StringUtils.capitalize(fw.getFieldName()), fw.getFieldType().getSimpleName(), fw.getFieldName()));
                newLines.add(strf("this.{} = {};", fw.getFieldName(), fw.getFieldName()));
                newLines.add(strf("}"));
            }
            newLines.add(strf("}"));
            JkFiles.writeFile(destFolder.resolve(strf("{}.java", cw.getEClazz().getSimpleName())), newLines);

            String str = strf("import org.springframework.data.jpa.repository.*;\n" +
                    "import org.springframework.stereotype.Repository;\n" +
                    "\n" +
                    "@Repository\n" +
                    "public interface {}Dao extends JpaRepository<{}, Long> {\n" +
                    "\n" +
                    "}", cw.getEClazz().getSimpleName(), cw.getEClazz().getSimpleName());
            JkFiles.writeFile(destFolder.resolve(strf("{}Dao.java", cw.getEClazz().getSimpleName())), str);
        }

        String str = "import org.apache.commons.lang3.builder.ToStringBuilder;\n" +
                "import org.apache.commons.lang3.builder.ToStringStyle;\n" +
                "\n" +
                "public abstract class JpaEntity {\n" +
                "\n" +
                "    @Override\n" +
                "    public String toString() {\n" +
                "        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);\n" +
                "    }\n" +
                "\n" +
                "}";
        JkFiles.writeFile(destFolder.resolve("JpaEntity.java"), str);
    }

}

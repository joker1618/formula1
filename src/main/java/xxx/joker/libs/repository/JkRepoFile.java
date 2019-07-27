package xxx.joker.libs.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.libs.core.datetime.JkTimer;
import xxx.joker.libs.core.lambdas.JkStreams;
import xxx.joker.libs.repository.config.RepoConfig;
import xxx.joker.libs.repository.config.RepoCtx;
import xxx.joker.libs.repository.dao.DaoHandler;
import xxx.joker.libs.repository.design.RepoEntity;
import xxx.joker.libs.repository.entities.RepoResource;
import xxx.joker.libs.repository.entities.RepoTags;
import xxx.joker.libs.repository.jpa.JpaHandler;
import xxx.joker.libs.repository.resourcer.ResourceHandler;
import xxx.joker.libs.repository.util.RepoUtil;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

public abstract class JkRepoFile implements JkRepo {

    private static final Logger LOG = LoggerFactory.getLogger(JkRepoFile.class);

    protected final RepoCtx ctx;

    private DaoHandler daoHandler;
    private JpaHandler jpaHandler;
    private ResourceHandler resourceHandler;


    protected JkRepoFile(Path repoFolder, String dbName, String... packages) {
        this(null, repoFolder, dbName, packages);
    }
    protected JkRepoFile(String encrPwd, Path repoFolder, String dbName, String... packages) {
        Set<Class<?>> eclasses = new HashSet<>();
        eclasses.addAll(RepoUtil.scanPackages(getClass(), packages));
        eclasses.addAll(RepoUtil.scanPackages(RepoConfig.class, RepoConfig.PACKAGE_COMMON_ENTTIES));

        this.ctx = new RepoCtx(repoFolder, dbName, eclasses, encrPwd);

        LOG.info("Init repo [folder={}, dbName={}, encr={}", ctx.getRepoFolder(), ctx.getDbName(), ctx.getEncrPwd());
        eclasses.forEach(ec -> LOG.info("Repo entity class: {}", ec));

        this.daoHandler = new DaoHandler(ctx);
        List<RepoEntity> lines = daoHandler.loadDataFromFiles();
        this.jpaHandler = new JpaHandler(ctx, lines);
        this.resourceHandler = new ResourceHandler(ctx, jpaHandler);

    }


    @Override
    public <T extends RepoEntity> Map<Class<T>, Set<T>> getDataSets() {
        return jpaHandler.getDataSets();
    }

    @Override
    public <T extends RepoEntity> Set<T> getDataSet(Class<T> entityClazz) {
        return jpaHandler.getDataSet(entityClazz);
    }

    @Override
    public <T extends RepoEntity> List<T> getList(Class<T> entityClazz, Predicate<T>... filters) {
        return JkStreams.filter(getDataSet(entityClazz), filters);
    }

    @Override
    public <T extends RepoEntity> T get(Class<T> entityClazz, Predicate<T>... filters) {
        return jpaHandler.get(entityClazz, filters);
    }

    @Override
    public <T extends RepoEntity> T getByPk(T entity) {
        return (T) get(entity.getClass(), entity::equals);
    }

    @Override
    public <T extends RepoEntity> boolean add(T toAdd) {
        Set<T> ds = (Set<T>) getDataSet(toAdd.getClass());
        return ds.add(toAdd);
    }

    @Override
    public void commit() {
        try {
            ctx.getWriteLock().lock();
            JkTimer timer = new JkTimer();
            Collection<RepoEntity> values = jpaHandler.getDataById().values();
            daoHandler.persistData(values);
            LOG.info("Committed repo in {}", timer.toStringElapsed());
        } finally {
            ctx.getWriteLock().unlock();
        }

    }

    @Override
    public RepoResource getResource(String resName, String... tags) {
        return resourceHandler.getResource(resName, RepoTags.of(tags));
    }

    @Override
    public RepoResource addResource(Path sourcePath, String resName, String... tags) {
        return resourceHandler.addResource(sourcePath, resName, RepoTags.of(tags));
    }

    @Override
    public RepoCtx getRepoCtx() {
        return ctx;
    }
}

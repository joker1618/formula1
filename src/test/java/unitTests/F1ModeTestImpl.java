package unitTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.libs.datalayer.JkRepoFile;

import java.nio.file.Paths;

public class F1ModeTestImpl extends JkRepoFile implements F1ModeTest {

    private static final Logger LOG = LoggerFactory.getLogger(F1ModeTestImpl.class);
    private static F1ModeTest instance;

//    private Map<Integer, F1Season> seasonMap = new HashMap<>();

    private F1ModeTestImpl() {
        super(Paths.get("testRepo"), "testDb", "unitTests");
    }

    public static synchronized F1ModeTest getInstance() {
        if(instance == null) {
            instance = new F1ModeTestImpl();
//            instance = ModelHandler.createHandler();
        }
        return instance;
    }
//
//    private static class ModelHandler implements InvocationHandler {
//
//        private F1ModelNew model;
//
//        public ModelHandler() {
//            F1ModelNew m = new F1ModelNewImpl();
//            ClassLoader loader = F1ModelNew.class.getClassLoader();
//            Class[] interfaces = {F1ModelNew.class};
//            model = (F1ModelNew) Proxy.newProxyInstance(loader, interfaces, this);
//        }
//
//        public static F1ModelNew createHandler() {
//            ModelHandler mh = new ModelHandler();
//            return mh.model;
//        }
//
//        @Override
//        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//            try {
//                LOG.trace();
//
//            } finally {
//
//            }
//        }
//    }


}

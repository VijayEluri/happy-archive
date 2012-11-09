package org.yi.happy.archive;

import java.io.InputStream;
import java.io.PrintStream;

import org.yi.happy.archive.file_system.RealFileSystem;

public class MyInjector {

    public static Provider<MainCommand> providerFileStoreTagPutMain() {
        return new Provider<MainCommand>() {
            @Override
            public MainCommand get() {
                return injectFileStoreTagPutMain();
            }
        };
    }

    public static MainCommand injectFileStoreTagPutMain() {
        return new FileStoreTagPutMain(injectFileSystem(), injectOutput());
    }

    public static PrintStream injectOutput() {
        return System.out;
    }

    public static RealFileSystem injectFileSystem() {
        return new RealFileSystem();
    }

    public static Provider<MainCommand> providerFileStoreTagGetMain() {
        return new Provider<MainCommand>() {
            @Override
            public MainCommand get() {
                return injectFileStoreTagGetMain();
            }
        };
    }

    public static MainCommand injectFileStoreTagGetMain() {
        return new FileStoreTagGetMain(injectFileSystem(), injectWaitHandler(),
                injectInput(), injectError());
    }

    public static PrintStream injectError() {
        return System.err;
    }

    public static InputStream injectInput() {
        return System.in;
    }

    public static WaitHandlerProgressiveDelay injectWaitHandler() {
        return new WaitHandlerProgressiveDelay();
    }

    public static Provider<MainCommand> providerShowEnvMain() {
        return new Provider<MainCommand>() {
            @Override
            public MainCommand get() {
                return injectShowEnvMain();
            }
        };
    }

    public static MainCommand injectShowEnvMain() {
        return new ShowEnvMain();
    }

    public static Provider<MainCommand> providerLocalCandidateListMain() {
        return new Provider<MainCommand>() {
            @Override
            public MainCommand get() {
                return injectLocalCandidateListMain();
            }
        };
    }

    public static MainCommand injectLocalCandidateListMain() {
        return new LocalCandidateListMain();
    }

    public static Provider<MainCommand> providerStoreRemoveMain() {
        return new Provider<MainCommand>() {
            @Override
            public MainCommand get() {
                return injectStoreRemoveMain();
            }
        };
    }

    public static MainCommand injectStoreRemoveMain() {
        return new StoreRemoveMain(injectFileSystem(), injectError());
    }

    public static Provider<MainCommand> providerStoreListMain() {
        return new Provider<MainCommand>() {
            @Override
            public MainCommand get() {
                return injectStoreListMain();
            }
        };
    }

    public static MainCommand injectStoreListMain() {
        return new FileStoreListMain(injectFileSystem(), injectOutput(),
                injectError());
    }

    public static Provider<MainCommand> providerIndexSearchMain() {
        return new Provider<MainCommand>() {
            @Override
            public MainCommand get() {
                return injectIndexSearchMain();
            }
        };
    }

    public static MainCommand injectIndexSearchMain() {
        return new IndexSearchMain(injectFileSystem(), injectOutput());
    }

}

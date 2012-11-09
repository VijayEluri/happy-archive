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

    public static Provider<MainCommand> providerIndexVolumeMain() {
        return new Provider<MainCommand>() {
            @Override
            public MainCommand get() {
                return injectIndexVolumeMain();
            }
        };
    }

    public static MainCommand injectIndexVolumeMain() {
        return new IndexVolumeMain(injectFileSystem(), injectOutput(),
                injectError());
    }

    public static Provider<MainCommand> providerBuildImageMain() {
        return new Provider<MainCommand>() {
            @Override
            public MainCommand get() {
                return injectBuildImageMain();
            }
        };
    }

    public static MainCommand injectBuildImageMain() {
        return new BuildImageMain(injectFileSystem(), injectOutput(),
                injectError());
    }

    public static Provider<MainCommand> providerVolumeGetMain() {
        return new Provider<MainCommand>() {
            @Override
            public MainCommand get() {
                return injectVolumeGetMain();
            }
        };
    }

    public static MainCommand injectVolumeGetMain() {
        return new VolumeGetMain(injectFileSystem(), injectInput(),
                injectOutput(), injectError());
    }

    public static Provider<MainCommand> providerVerifyMain() {
        return new Provider<MainCommand>() {
            @Override
            public MainCommand get() {
                return injectVerifyMain();
            }
        };
    }

    public static MainCommand injectVerifyMain() {
        return new VerifyMain(injectFileSystem(), injectOutput());
    }

    public static Provider<MainCommand> providerEncodeContentMain() {
        return null;
    }

    public static MainCommand injectEncodeContentMain() {
        return new EncodeContentMain(injectFileSystem(), injectOutput());
    }

    public static Provider<MainCommand> providerDecodeBlockMain() {
        return new Provider<MainCommand>() {
            @Override
            public MainCommand get() {
                return injectDecodeBlockMain();
            }
        };
    }

    public static MainCommand injectDecodeBlockMain() {
        return new DecodeBlockMain(injectFileSystem(), injectOutput());
    }

    public static Provider<MainCommand> providerFileStoreTagAddMain() {
        return new Provider<MainCommand>() {
            @Override
            public MainCommand get() {
                return injectFileStoreTagAddMain();
            }
        };
    }

    public static MainCommand injectFileStoreTagAddMain() {
        return new FileStoreTagAddMain();
    }

    public static Provider<MainCommand> providerFileStoreStreamPutMain() {
        return new Provider<MainCommand>() {
            @Override
            public MainCommand get() {
                return injectFileStoreStreamPutMain();
            }
        };
    }

    public static MainCommand injectFileStoreStreamPutMain() {
        return new FileStoreStreamPutMain(injectFileSystem(), injectInput(),
                injectOutput());
    }

    public static Provider<MainCommand> providerFileStoreStreamGetMain() {
        return new Provider<MainCommand>() {
            @Override
            public MainCommand get() {
                return injectFileStoreStreamGetMain();
            }
        };
    }

    public static MainCommand injectFileStoreStreamGetMain() {
        return new FileStoreStreamGetMain(injectFileSystem(), injectOutput(),
                injectWaitHandler());
    }

    public static Provider<MainCommand> providerFileStoreBlockPutMain() {
        return new Provider<MainCommand>() {
            @Override
            public MainCommand get() {
                return injectFileStoreBlockPutMain();
            }
        };
    }

    public static MainCommand injectFileStoreBlockPutMain() {
        return new FileStoreBlockPutMain(injectFileSystem());
    }

    public static Provider<MainCommand> providerMakeIndexDatabaseMain() {
        return new Provider<MainCommand>() {
            @Override
            public MainCommand get() {
                return injectMakeIndexDatabaseMain();
            }
        };
    }

    public static MainCommand injectMakeIndexDatabaseMain() {
        return new MakeIndexDatabaseMain();
    }

    public static Provider<MainCommand> providerFileStoreFileGetMain() {
        return new Provider<MainCommand>() {
            @Override
            public MainCommand get() {
                return injectFileStoreFileGetMain();
            }
        };
    }

    public static MainCommand injectFileStoreFileGetMain() {
        return new FileStoreFileGetMain(injectFileSystem(), injectWaitHandler());
    }
}

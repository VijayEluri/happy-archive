package org.yi.happy.archive;

import java.io.InputStream;
import java.io.PrintStream;

import org.yi.happy.archive.file_system.RealFileSystem;

public class MyInjector {

    public static MainCommand injectFileStoreTagPutMain() {
        return new FileStoreTagPutMain(injectFileSystem(), injectOutput());
    }

    public static PrintStream injectOutput() {
        return System.out;
    }

    public static RealFileSystem injectFileSystem() {
        return new RealFileSystem();
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

    public static MainCommand injectShowEnvMain() {
        return new ShowEnvMain();
    }

    public static MainCommand injectLocalCandidateListMain() {
        return new LocalCandidateListMain();
    }

    public static MainCommand injectStoreRemoveMain() {
        return new StoreRemoveMain(injectFileSystem(), injectError());
    }

    public static MainCommand injectFileStoreListMain() {
        return new FileStoreListMain(injectFileSystem(), injectOutput(),
                injectError());
    }

    public static MainCommand injectIndexSearchMain() {
        return new IndexSearchMain(injectFileSystem(), injectOutput());
    }

    public static MainCommand injectIndexVolumeMain() {
        return new IndexVolumeMain(injectFileSystem(), injectOutput(),
                injectError());
    }

    public static MainCommand injectBuildImageMain() {
        return new BuildImageMain(injectFileSystem(), injectOutput(),
                injectError());
    }

    public static MainCommand injectVolumeGetMain() {
        return new VolumeGetMain(injectFileSystem(), injectInput(),
                injectError());
    }

    public static MainCommand injectVerifyMain() {
        return new VerifyMain(injectFileSystem(), injectOutput());
    }

    public static MainCommand injectEncodeContentMain() {
        return new EncodeContentMain(injectFileSystem(), injectOutput());
    }

    public static MainCommand injectDecodeBlockMain() {
        return new DecodeBlockMain(injectFileSystem(), injectOutput());
    }

    public static MainCommand injectFileStoreTagAddMain() {
        return new FileStoreTagAddMain();
    }

    public static MainCommand injectFileStoreStreamPutMain() {
        return new FileStoreStreamPutMain(injectFileSystem(), injectInput(),
                injectOutput());
    }

    public static MainCommand injectFileStoreStreamGetMain() {
        return new FileStoreStreamGetMain(injectFileSystem(), injectOutput(),
                injectWaitHandler());
    }

    public static MainCommand injectFileStoreBlockPutMain() {
        return new FileStoreBlockPutMain(injectFileSystem());
    }

    public static MainCommand injectMakeIndexDatabaseMain() {
        return new MakeIndexDatabaseMain();
    }

    public static MainCommand injectFileStoreFileGetMain() {
        return new FileStoreFileGetMain(injectFileSystem(), injectWaitHandler());
    }
}

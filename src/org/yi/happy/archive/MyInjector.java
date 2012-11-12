package org.yi.happy.archive;

import java.io.InputStream;
import java.io.PrintStream;

import org.yi.happy.archive.file_system.RealFileSystem;

public class MyInjector {

    public static MainCommand injectFileStoreTagPutMain(ApplicationScope scope) {
        return new FileStoreTagPutMain(injectBlockStore(scope),
                injectFileSystem(scope), injectOutput(scope));
    }

    public static PrintStream injectOutput(ApplicationScope scope) {
        return System.out;
    }

    public static RealFileSystem injectFileSystem(ApplicationScope scope) {
        return new RealFileSystem();
    }

    public static MainCommand injectFileStoreTagGetMain(ApplicationScope scope) {
        return new FileStoreTagGetMain(injectFileSystem(scope),
                injectWaitHandler(scope), injectInput(scope),
                injectError(scope));
    }

    public static PrintStream injectError(ApplicationScope scope) {
        return System.err;
    }

    public static InputStream injectInput(ApplicationScope scope) {
        return System.in;
    }

    public static WaitHandlerProgressiveDelay injectWaitHandler(
            ApplicationScope scope) {
        return new WaitHandlerProgressiveDelay();
    }

    public static MainCommand injectShowEnvMain(ApplicationScope scope) {
        return new ShowEnvMain();
    }

    public static MainCommand injectLocalCandidateListMain(
            ApplicationScope scope) {
        return new LocalCandidateListMain();
    }

    public static MainCommand injectStoreRemoveMain(ApplicationScope scope) {
        return new StoreRemoveMain(injectFileSystem(scope), injectError(scope));
    }

    public static MainCommand injectFileStoreListMain(ApplicationScope scope) {
        return new FileStoreListMain(injectFileSystem(scope),
                injectOutput(scope), injectError(scope));
    }

    public static MainCommand injectIndexSearchMain(ApplicationScope scope) {
        return new IndexSearchMain(injectFileSystem(scope), injectOutput(scope));
    }

    public static MainCommand injectIndexVolumeMain(ApplicationScope scope) {
        return new IndexVolumeMain(injectFileSystem(scope),
                injectOutput(scope), injectError(scope));
    }

    public static MainCommand injectBuildImageMain(ApplicationScope scope) {
        return new BuildImageMain(injectFileSystem(scope), injectOutput(scope),
                injectError(scope));
    }

    public static MainCommand injectVolumeGetMain(ApplicationScope scope) {
        return new VolumeGetMain(injectFileSystem(scope), injectInput(scope),
                injectError(scope));
    }

    public static MainCommand injectVerifyMain(ApplicationScope scope) {
        return new VerifyMain(injectFileSystem(scope), injectOutput(scope));
    }

    public static MainCommand injectEncodeContentMain(ApplicationScope scope) {
        return new EncodeContentMain(injectFileSystem(scope),
                injectOutput(scope));
    }

    public static MainCommand injectDecodeBlockMain(ApplicationScope scope) {
        return new DecodeBlockMain(injectFileSystem(scope), injectOutput(scope));
    }

    public static MainCommand injectFileStoreTagAddMain(ApplicationScope scope) {
        return new FileStoreTagAddMain(injectBlockStore(scope),
                injectFileSystem(scope));
    }

    public static MainCommand injectFileStoreStreamPutMain(
            ApplicationScope scope) {
        return new FileStoreStreamPutMain(injectFileSystem(scope),
                injectInput(scope), injectOutput(scope));
    }

    public static MainCommand injectFileStoreStreamGetMain(
            ApplicationScope scope) {
        return new FileStoreStreamGetMain(injectBlockStore(scope),
                injectFileSystem(scope), injectOutput(scope),
                injectWaitHandler(scope));
    }

    public static MainCommand injectFileStoreBlockPutMain(ApplicationScope scope) {
        return new FileStoreBlockPutMain(injectBlockStore(scope),
                injectFileSystem(scope));
    }

    public static MainCommand injectMakeIndexDatabaseMain(ApplicationScope scope) {
        return new MakeIndexDatabaseMain();
    }

    public static MainCommand injectFileStoreFileGetMain(ApplicationScope scope) {
        return new FileStoreFileGetMain(injectBlockStore(scope),
                injectFileSystem(scope), injectWaitHandler(scope));
    }

    public static BlockStore injectBlockStore(ApplicationScope scope) {
        return new FileBlockStore(injectFileSystem(scope), scope.getStore());
    }
}

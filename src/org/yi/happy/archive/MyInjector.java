package org.yi.happy.archive;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;

/**
 * The dependency injector for this project, this gives me much more flexibility
 * in object creation and increased testability of commands.
 */
public class MyInjector {

    /**
     * get a {@link FileStoreTagPutMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectFileStoreTagPutMain(ApplicationScope scope) {
        return new FileStoreTagPutMain(injectBlockStore(scope),
                injectFileSystem(scope), injectOutput(scope), injectEnv(scope));
    }

    /**
     * get an output stream.
     * 
     * @param scope
     *            the scope object.
     * @return the stream.
     */
    public static PrintStream injectOutput(ApplicationScope scope) {
        return System.out;
    }

    /**
     * get a {@link FileSystem}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static FileSystem injectFileSystem(ApplicationScope scope) {
        return new RealFileSystem();
    }

    /**
     * get a {@link FileStoreTagGetMain}
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectFileStoreTagGetMain(ApplicationScope scope) {
        return new FileStoreTagGetMain(injectBlockStore(scope),
                injectFileSystem(scope), injectWaitHandler(scope),
                injectInput(scope), injectNeedHandler(scope));
    }

    public static NeedHandler injectNeedHandler(ApplicationScope scope) {
        return new NeedWriter(injectFileSystem(scope), injectNeedFile(scope));
    }

    public static String injectNeedFile(ApplicationScope scope) {
        return scope.getNeedFile();
    }

    /**
     * get an error stream.
     * 
     * @param scope
     *            the scope object.
     * @return the stream.
     */
    public static PrintStream injectError(ApplicationScope scope) {
        return System.err;
    }

    /**
     * get an input stream.
     * 
     * @param scope
     *            the scope object.
     * @return the stream.
     */
    public static InputStream injectInput(ApplicationScope scope) {
        return System.in;
    }

    /**
     * get a {@link WaitHandler}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static WaitHandler injectWaitHandler(ApplicationScope scope) {
        return new WaitHandlerProgressiveDelay();
    }

    /**
     * get a {@link ShowEnvMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectShowEnvMain(ApplicationScope scope) {
        return new ShowEnvMain(injectEnv(scope));
    }

    /**
     * get a {@link LocalCandidateListMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectLocalCandidateListMain(
            ApplicationScope scope) {
        return new LocalCandidateListMain(injectBlockStore(scope),
                injectEnv(scope));
    }

    /**
     * get a {@link StoreRemoveMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectStoreRemoveMain(ApplicationScope scope) {
        return new StoreRemoveMain(injectBlockStore(scope),
                injectFileSystem(scope), injectEnv(scope));
    }

    /**
     * get a {@link FileStoreListMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectFileStoreListMain(ApplicationScope scope) {
        return new FileStoreListMain(injectBlockStore(scope),
                injectOutput(scope));
    }

    /**
     * get a {@link IndexSearchMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectIndexSearchMain(ApplicationScope scope) {
        return new IndexSearchMain(injectFileSystem(scope),
                injectOutput(scope), injectEnv(scope));
    }

    /**
     * get an {@link IndexVolumeMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectIndexVolumeMain(ApplicationScope scope) {
        return new IndexVolumeMain(injectFileSystem(scope),
                injectOutput(scope), injectError(scope), injectEnv(scope));
    }

    /**
     * get a {@link BuildImageMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectBuildImageMain(ApplicationScope scope) {
        return new BuildImageMain(injectBlockStore(scope),
                injectFileSystem(scope), injectOutput(scope),
                injectError(scope), injectArgs(scope));
    }

    /**
     * get the arguments.
     * 
     * @param scope
     *            the scope object.
     * @return the arguments.
     */
    public static List<String> injectArgs(ApplicationScope scope) {
        return scope.getArgs();
    }

    /**
     * get the {@link Env}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static Env injectEnv(ApplicationScope scope) {
        return scope.getEnv();
    }

    /**
     * get a {@link VolumeGetMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectVolumeGetMain(ApplicationScope scope) {
        return new VolumeGetMain(injectBlockStore(scope),
                injectFileSystem(scope), injectInput(scope),
                injectError(scope), injectArgs(scope));
    }

    /**
     * get a {@link VerifyMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectVerifyMain(ApplicationScope scope) {
        return new VerifyMain(injectFileSystem(scope), injectOutput(scope),
                injectEnv(scope));
    }

    /**
     * get an {@link EncodeContentMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectEncodeContentMain(ApplicationScope scope) {
        return new EncodeContentMain(injectFileSystem(scope),
                injectOutput(scope), injectArgs(scope));
    }

    /**
     * get a {@link DecodeBlockMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectDecodeBlockMain(ApplicationScope scope) {
        return new DecodeBlockMain(injectFileSystem(scope),
                injectOutput(scope), injectArgs(scope));
    }

    /**
     * get a {@link FileStoreTagAddMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectFileStoreTagAddMain(ApplicationScope scope) {
        return new FileStoreTagAddMain(injectBlockStore(scope),
                injectFileSystem(scope));
    }

    /**
     * get a {@link FileStoreStreamPutMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectFileStoreStreamPutMain(
            ApplicationScope scope) {
        return new FileStoreStreamPutMain(injectBlockStore(scope),
                injectFileSystem(scope), injectInput(scope),
                injectOutput(scope));
    }

    /**
     * get a {@link FileStoreStreamGetMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectFileStoreStreamGetMain(
            ApplicationScope scope) {
        return new FileStoreStreamGetMain(injectBlockStore(scope),
                injectFileSystem(scope), injectOutput(scope),
                injectWaitHandler(scope), injectNeedHandler(scope),
                injectEnv(scope));
    }

    /**
     * get a {@link FileStoreBlockPutMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectFileStoreBlockPutMain(ApplicationScope scope) {
        return new FileStoreBlockPutMain(injectBlockStore(scope),
                injectFileSystem(scope), injectArgs(scope));
    }

    /**
     * get a {@link MakeIndexDatabaseMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectMakeIndexDatabaseMain(ApplicationScope scope) {
        return new MakeIndexDatabaseMain();
    }

    /**
     * get a {@link FileStoreFileGetMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectFileStoreFileGetMain(ApplicationScope scope) {
        return new FileStoreFileGetMain(injectBlockStore(scope),
                injectFileSystem(scope), injectWaitHandler(scope),
                injectEnv(scope));
    }

    /**
     * get a {@link BlockStore}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static BlockStore injectBlockStore(ApplicationScope scope) {
        return new FileBlockStore(injectFileSystem(scope), scope.getStore());
    }

    /**
     * get a {@link HelpMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectHelpMain(ApplicationScope scope) {
        return new HelpMain(injectOutput(scope), injectCommands(scope));
    }

    /**
     * get a {@link CriticalListMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectCriticalListMain(ApplicationScope scope) {
        return new CriticalListMain(injectBlockStore(scope), injectEnv(scope));
    }

    /**
     * get the command to implementation map.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static Map<String, Class<? extends MainCommand>> injectCommands(
            ApplicationScope scope) {
        return scope.getCommands();
    }
}

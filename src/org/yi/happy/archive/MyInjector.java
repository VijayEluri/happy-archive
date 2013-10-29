package org.yi.happy.archive;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import org.yi.happy.annotate.GlobalFilesystem;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;

/**
 * The dependency injector for this project, this gives me much more flexibility
 * in object creation and increased testability of commands.
 */
public class MyInjector {

    /**
     * get a {@link StoreTagPutMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectStoreTagPutMain(ApplicationScope scope) {
        return new StoreTagPutMain(injectBlockStore(scope),
                injectFileSystem(scope), injectOutput(scope), injectArgs(scope));
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
     * get a {@link StoreTagGetMain}
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectStoreTagGetMain(ApplicationScope scope) {
        return new StoreTagGetMain(injectClearBlockSource(scope),
                injectFragmentSave(scope), injectNotReadyHandler(scope),
                injectInput(scope));
    }

    /**
     * get a {@link NotReadyHandler}
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static NotReadyHandler injectNotReadyHandler(ApplicationScope scope) {
        return new NotReadyNeedAndWait(injectNeedHandler(scope),
                injectWaitHandler(scope));
    }

    /**
     * get a {@link NeedHandler}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static NeedHandler injectNeedHandler(ApplicationScope scope) {
        return new NeedWriter(injectNeedFile(scope));
    }

    /**
     * get the file name of the needed block list.
     * 
     * @param scope
     *            the scope object.
     * @return the file name.
     */
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
        return new ShowEnvMain(injectEnv(scope), injectOutput(scope));
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
                injectIndexSearch(scope), injectArgs(scope));
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
                injectFileSystem(scope), injectArgs(scope));
    }

    /**
     * get a {@link StoreListMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectStoreListMain(ApplicationScope scope) {
        return new StoreListMain(injectBlockStore(scope), injectOutput(scope));
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
                injectOutput(scope), injectIndexSearch(scope),
                injectArgs(scope));
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
                injectOutput(scope), injectError(scope), injectArgs(scope));
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
                injectArgs(scope));
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
     * get a {@link StoreTagAddMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectStoreTagAddMain(ApplicationScope scope) {
        return new StoreTagAddMain(injectBlockStore(scope),
                injectFileSystem(scope));
    }

    /**
     * get a {@link StoreStreamPutMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectStoreStreamPutMain(ApplicationScope scope) {
        return new StoreStreamPutMain(injectBlockStore(scope),
                injectFileSystem(scope), injectInput(scope),
                injectOutput(scope));
    }

    /**
     * get a {@link StoreStreamGetMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectStoreStreamGetMain(
            ApplicationScope scope) {
        return new StoreStreamGetMain(injectBlockStore(scope),
                injectOutput(scope), injectWaitHandler(scope),
                injectNeedHandler(scope), injectArgs(scope));
    }

    /**
     * get a {@link StoreBlockPutMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectStoreBlockPutMain(ApplicationScope scope) {
        return new StoreBlockPutMain(injectBlockStore(scope),
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
     * get a {@link StoreFileGetMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectStoreFileGetMain(ApplicationScope scope) {
        return new StoreFileGetMain(injectClearBlockSource(scope),
                injectFragmentSave(scope), injectWaitHandler(scope),
                injectNeedHandler(scope), injectArgs(scope));
    }

    @GlobalFilesystem
    private static FragmentSave injectFragmentSave(ApplicationScope scope) {
        return new FragmentSaveFile();
    }

    private static ClearBlockSource injectClearBlockSource(
            ApplicationScope scope) {
        return new StorageClearBlockSource(injectBlockStore(scope));
    }

    /**
     * get a {@link BlockStore}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static BlockStore injectBlockStore(ApplicationScope scope) {
        return new FileBlockStore(injectStoreFile(scope));
    }

    /**
     * get the {@link File} object representing the base of the store.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    @GlobalFilesystem
    public static File injectStoreFile(ApplicationScope scope) {
        return new File(scope.getStore());
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
        return new CriticalListMain(injectBlockStore(scope),
                injectIndexSearch(scope));
    }

    /**
     * get a {@link IndexSearch}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static IndexSearch injectIndexSearch(ApplicationScope scope) {
        return new IndexSearch(injectFileSystem(scope), injectIndexPath(scope));
    }

    /**
     * get the path to the index.
     * 
     * @param scope
     *            the scope object.
     * @return the path.
     */
    public static String injectIndexPath(ApplicationScope scope) {
        return scope.getIndexPath();
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

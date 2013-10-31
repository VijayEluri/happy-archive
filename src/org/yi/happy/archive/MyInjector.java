package org.yi.happy.archive;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import org.yi.happy.annotate.GlobalFilesystem;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.file_system.FileStore;
import org.yi.happy.archive.file_system.FileStoreFile;

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
                injectFileStore(scope), injectOutput(scope), injectArgs(scope));
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
     * get a {@link FileStore}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static FileStore injectFileStore(ApplicationScope scope) {
        return new FileStoreFile();
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
        return new NotReadyPostAndWait(injectNeed(scope));
    }

    /**
     * get the file name of the needed block list.
     * 
     * @param scope
     *            the scope object.
     * @return the file name.
     */
    public static String injectNeed(ApplicationScope scope) {
        return scope.getNeed();
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
                injectIndexSearch(scope), injectOutput(scope),
                injectError(scope), injectArgs(scope));
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
                injectFileStore(scope), injectArgs(scope));
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
        return new IndexSearchMain(injectInput(scope), injectOutput(scope),
                injectError(scope), injectIndexSearch(scope));
    }

    /**
     * get an {@link IndexVolumeMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectIndexVolumeMain(ApplicationScope scope) {
        return new IndexVolumeMain(injectFileStore(scope), injectOutput(scope),
                injectError(scope), injectArgs(scope));
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
                injectFileStore(scope), injectInput(scope),
                injectOutput(scope), injectError(scope), injectArgs(scope));
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
                injectFileStore(scope), injectInput(scope), injectError(scope),
                injectArgs(scope));
    }

    /**
     * get a {@link VerifyMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectVerifyMain(ApplicationScope scope) {
        return new VerifyMain(injectFileStore(scope), injectOutput(scope),
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
        return new EncodeContentMain(injectFileStore(scope),
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
        return new DecodeBlockMain(injectFileStore(scope), injectOutput(scope),
                injectArgs(scope));
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
                injectFileStore(scope));
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
                injectFileStore(scope), injectInput(scope), injectOutput(scope));
    }

    /**
     * get a {@link StoreStreamGetMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static MainCommand injectStoreStreamGetMain(ApplicationScope scope) {
        return new StoreStreamGetMain(injectClearBlockSource(scope),
                injectOutput(scope), injectNotReadyHandler(scope),
                injectArgs(scope));
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
                injectFileStore(scope), injectArgs(scope));
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
                injectFragmentSave(scope), injectNotReadyHandler(scope),
                injectArgs(scope));
    }

    /**
     * get a {@link FragmentSave}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    @GlobalFilesystem
    public static FragmentSave injectFragmentSave(ApplicationScope scope) {
        return new FragmentSaveFile();
    }

    /**
     * get a {@link ClearBlockSource}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static ClearBlockSource injectClearBlockSource(ApplicationScope scope) {
        return new ClearBlockSourceStore(injectBlockStore(scope));
    }

    /**
     * get a {@link BlockStore}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static BlockStore injectBlockStore(ApplicationScope scope) {
        return new BlockStoreFile(injectStoreFile(scope));
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
        return new File(injectStore(scope));
    }

    /**
     * get the base of the store.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    @GlobalFilesystem
    public static String injectStore(ApplicationScope scope) {
        return scope.getStore();
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
                injectIndexSearch(scope), injectOutput(scope),
                injectError(scope));
    }

    /**
     * get a {@link IndexSearch}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static IndexSearch injectIndexSearch(ApplicationScope scope) {
        return new IndexSearch(injectIndexStore(scope));
    }

    /**
     * get a {@link IndexStore}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static IndexStore injectIndexStore(ApplicationScope scope) {
        return new IndexStoreFileStore(injectFileStore(scope),
                injectIndex(scope));
    }

    /**
     * get the path to the index.
     * 
     * @param scope
     *            the scope object.
     * @return the path.
     */
    public static String injectIndex(ApplicationScope scope) {
        return scope.getIndex();
    }

    /**
     * get a {@link IndexCheckMain}.
     * 
     * @param scope
     *            the scope object.
     * @return the object.
     */
    public static IndexCheckMain injectIndexCheckMain(ApplicationScope scope) {
        return new IndexCheckMain(injectFileStore(scope), injectInput(scope),
                injectOutput(scope), injectError(scope), injectArgs(scope));
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

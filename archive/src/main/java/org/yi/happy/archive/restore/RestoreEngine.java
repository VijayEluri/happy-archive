package org.yi.happy.archive.restore;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

import org.yi.happy.archive.Bytes;
import org.yi.happy.archive.Fragment;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;

/**
 * Assistance with the logic required to use {@link RestoreWork} to put one or
 * many data blocks back together. This insulates the client code from having to
 * deal with {@link RestoreItem}s.
 * 
 * <p>
 * for one job:
 * </p>
 * 
 * <pre>
 * {@link RestoreEngine} r = new {@link RestoreEngine}(key);
 * 
 * r.{@link #start()};
 * while(r.{@link #findReady()}) {
 *   {@link Block} in = getBlock(r.{@link #getKey()});
 *   if(in == null) {
 *     r.{@link #skip()};
 *     continue;
 *   }
 *   
 *   {@link Fragment} out = r.{@link #step}(in);
 *   
 *   if(out != null) {
 *     handleOutput(fragment);
 *   }
 * }
 * </pre>
 * 
 * <p>
 * for many jobs:
 * </p>
 * 
 * <pre>
 * {@link RestoreEngine} r = new {@link RestoreEngine}();
 * r.add("1", key1);
 * r.add("2", key2);
 * 
 * r.{@link #start()};
 * while(r.{@link #findReady()}) {
 *   {@link Block} in = getBlock(r.{@link #getKey()});
 *   if(in == null) {
 *     r.{@link #skip()};
 *     continue;
 *   }
 *   
 *   {@link Fragment} out = r.{@link #step}(in);
 *   
 *   if(out != null) {
 *     handleOutput(r.{@link #getJobName()}, fragment);
 *   }
 * }
 * </pre>
 */
public class RestoreEngine {
    private static class Job {
        public final String name;
        public final RestoreWork work;

        public Job(String name, RestoreWork work) {
            this.name = name;
            this.work = work;
        }
    }

    /**
     * The jobs.
     */
    private List<Job> jobs = new ArrayList<RestoreEngine.Job>();

    /**
     * The current job.
     */
    private int jobIndex = 0;

    /**
     * The current item in the current job.
     */
    private int index = 0;

    /**
     * Start with no jobs.
     */
    public RestoreEngine() {
    }

    /**
     * Start with one job.
     * 
     * @param name
     *            the name of the job.
     * @param work
     *            the work to put together.
     */
    public RestoreEngine(String name, RestoreWork work) {
        this();
        jobs.add(new Job(name, work));
    }

    /**
     * Start with one job.
     * 
     * @param name
     *            the name of the job.
     * @param key
     *            the key to put together.
     */
    public RestoreEngine(String name, FullKey key) {
        this(name, new RestoreWork(key));
    }

    /**
     * Start with one job. The job name is null.
     * 
     * @param key
     *            the key to put together.
     */
    public RestoreEngine(FullKey key) {
        this(null, key);
    }

    /**
     * Add a job.
     * 
     * @param name
     *            the name of the job.
     * @param work
     *            the work to put together.
     */
    public void add(String name, RestoreWork work) {
        jobs.add(new Job(name, work));
    }

    /**
     * Add a job.
     * 
     * @param name
     *            the name of the job.
     * @param key
     *            the key to put together.
     */
    public void add(String name, FullKey key) throws DuplicateJobName {
        if (hasJob(name)) {
            throw new DuplicateJobName(name);
        }
        add(name, new RestoreWork(key));
    }

    private boolean hasJob(String name) {
        for (Job job : jobs) {
            if (Objects.equals(job.name, name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the list of blocks that are known to be needed.
     */
    public List<FullKey> getNeeded() {
        LinkedHashSet<FullKey> needed = new LinkedHashSet<FullKey>();
        for (Job job : jobs) {
            for (int index = 0; index < job.work.count(); index++) {
                needed.add(job.work.getKey(index));
            }
        }
        return new ArrayList<FullKey>(needed);
    }

    /**
     * Set the cursor back to the beginning.
     */
    public void start() {
        jobIndex = 0;
        index = 0;
    }

    /**
     * Find a ready item starting with the current item. The first item is
     * always ready when it exists.
     * 
     * @return true if a ready item is found
     */
    public boolean findReady() {
        while (true) {
            if (jobIndex >= jobs.size()) {
                return false;
            }

            if (isJobDone()) {
                jobs.remove(jobIndex);
                index = 0;
                continue;
            }

            if (index >= getWork().count()) {
                jobIndex++;
                index = 0;
                continue;
            }

            if (getOffset() == -1) {
                index++;
                continue;
            }

            return true;
        }
    }

    public boolean isJobDone() {
        return getWork().count() == 0;
    }

    /**
     * get the offset of the current item.
     * 
     * @return the offset of the current item.
     */
    public long getOffset() {
        return getWork().getOffset(index);
    }

    /**
     * get is the key of the current item. This is the key for the block to give
     * to {@link #step(Block)}.
     * 
     * @return the key of the current item.
     */
    public FullKey getKey() {
        return getWork().getKey(index);
    }

    /**
     * process the current item.
     * 
     * @param block
     *            the needed block.
     * @return the data fragment for data blocks, null otherwise.
     */
    public Fragment step(Block block) {
        long offset = getWork().getOffset(index);

        if (offset == -1) {
            throw new IllegalStateException();
        }

        RestoreItem item = RestoreItemFactory.create(getKey(), block);
        getWork().replace(index, item);

        if (item.isData()) {
            Bytes data = item.getBlock().getBody();
            return new Fragment(offset, data);
        }
        return null;
    }

    /**
     * @return the name of the current job.
     */
    public String getJobName() {
        return getJob().name;
    }

    /**
     * @return the number of work items for the current job.
     */
    public int getJobSize() {
        return getWork().count();
    }

    /**
     * skip the current item.
     */
    public void skip() {
        index++;
    }

    /**
     * skip the current job.
     */
    public void skipJob() {
        jobIndex++;
    }

    /**
     * @return true if this engine has no more work to do.
     */
    public boolean isDone() {
        for (Job job : jobs) {
            if (job.work.count() > 0) {
                return false;
            }
        }
        return true;
    }

    private Job getJob() {
        return jobs.get(jobIndex);
    }

    private RestoreWork getWork() {
        return getJob().work;
    }

    public RestoreState getState() {
        RestoreState state = new RestoreState();
        for (Job job : jobs) {
            RestoreStateJob stateJob = new RestoreStateJob();
            stateJob.setName(job.name);
            RestoreWork parts = job.work;
            for (int i = 0; i < parts.count(); i++) {
                RestoreStateJobPart stateJobPart = new RestoreStateJobPart();
                stateJobPart.setOffset(parts.getOffset(i));
                stateJobPart.setKey(parts.getKey(i).toString());
                stateJob.getParts().add(stateJobPart);
            }
            state.getJobs().add(stateJob);
        }
        return state;
    }

    public void setState(RestoreState state) {
        jobs.clear();
        start();

        for (RestoreStateJob stateJob : state.getJobs()) {
            RestoreWork work = new RestoreWork();
            for (RestoreStateJobPart stateJobPart : stateJob.getParts()) {
                FullKey key = FullKeyParse.parseFullKey(stateJobPart.getKey());
                work.add(key, stateJobPart.getOffset());
            }
            jobs.add(new Job(stateJob.getName(), work));
        }
    }
}

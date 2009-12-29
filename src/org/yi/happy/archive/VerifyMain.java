package org.yi.happy.archive;

import java.io.PrintWriter;
import java.io.Writer;

import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;

public class VerifyMain {

	private final FileSystem fileSystem;
	private final Writer out;

	public VerifyMain(FileSystem fileSystem, Writer out) {
		this.fileSystem = fileSystem;
		this.out = out;
	}

	public void run(String... args) throws Exception {
		for (String arg : args) {
			String line;

			try {
				/*
				 * load the file
				 */
				byte[] data = fileSystem.load(arg, Blocks.MAX_SIZE);

				/*
				 * parse into a block
				 */
				Block block = BlockParse.load(data);

				/*
				 * try to parse into an encoded block
				 */
				EncodedBlock b = EncodedBlockFactory.parse(block);

				/*
				 * on success print ok key arg
				 */
				line = "ok " + b.getKey() + " " + arg + "\n";
			} catch (Exception e) {
				/*
				 * on failure print fail arg
				 */
				line = "fail " + arg + "\n";
			}

			out.write(line);
		}
	}

	public static void main(String[] args) throws Exception {
		FileSystem fs = new RealFileSystem();
		Writer out = new PrintWriter(System.out, true);

		new VerifyMain(fs, out).run(args);

		out.flush();
	}
}

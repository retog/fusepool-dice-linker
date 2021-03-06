package com.sindice.fusepooladapter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurableLinkerAdapter extends LinkerAdapter {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final String dukeConfigFileLocation;
	private final String inpath;
	private final String outpath;
	private final int threadsNo;

	/**
	 * constructor using provided parameters
	 * 
	 * @param dukeConfigFileLocation
	 *            - Duke XML configuraiton file path
	 * @param inpath
	 *            - data path for input store
	 * @param outpath
	 *            - data path for output store
	 * @param threadsNo
	 *            - number of threads to run Duke
	 * @throws IOException
	 */
	public ConfigurableLinkerAdapter(String dukeConfigFileLocation,
			String inpath, String outpath, int threadsNo) throws IOException {
		this.inpath = FileSystems.getDefault().getPath(inpath).toAbsolutePath()
				.toString();
		;
		this.outpath = FileSystems.getDefault().getPath(outpath)
				.toAbsolutePath().toString();
		this.dukeConfigFileLocation = dukeConfigFileLocation;
		this.threadsNo = threadsNo;
	}

	/**
	 * constructor for configuration in a property file on classpath
	 * 
	 * @param confFileName
	 *            - file name of the property file
	 * @throws IOException
	 */
	public ConfigurableLinkerAdapter(String confFileName) throws IOException {

		Properties properties = new Properties();
		properties.load(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(confFileName));
		if (properties.getProperty("inpath") == null
				|| properties.getProperty("outpath") == null
				|| properties.getProperty("dukeconfig") == null
				|| properties.getProperty("dukethrno") == null) {
			throw new IllegalArgumentException("missing mandatory property");
		}
		this.inpath = FileSystems.getDefault()
				.getPath(properties.getProperty("inpath")).toAbsolutePath()
				.toString();
		;
		this.outpath = FileSystems.getDefault()
				.getPath(properties.getProperty("outpath")).toAbsolutePath()
				.toString();
		this.dukeConfigFileLocation = properties.getProperty("dukeconfig");
		this.threadsNo = Integer.parseInt(properties.getProperty("dukethrno"));
	}

	/**
	 * @return Input dir passed to constructor.
	 */
	@Override
	protected String defaultInputDir() {
		return inpath;
	}

	/**
	 * @return Output dir passed to constructor.
	 */
	@Override
	protected String defaultOutputDir() {
		return outpath;
	}

	/**
	 * @return Thread number passed to constructor.
	 */
	@Override
	protected int defaultNumberOfThreads() {
		return threadsNo;
	}

	/**
	 * 
	 * @return "classpath:patents-jena-jdbc.xml"
	 */
	@Override
	protected String defaultConfigFileLocation() {
		return dukeConfigFileLocation;
	}
}

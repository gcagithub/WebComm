package com.webComm.data.files.storage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails.Builder;

import com.webComm.data.files.model.FileMeta;
import com.webComm.utils.HomeDir;


public class FSStorage extends Storage {
	private static final String DATA_DIR = HomeDir.getDataPath();

	@Override
	public boolean store(FileMeta file) {
		boolean result = true;

		try {
			byte[] buffer = new byte[8 * 1024];

			InputStream input = file.getContent();
			try {
				OutputStream output = new FileOutputStream(DATA_DIR + "/"
						+ file.getSerial());
				try {
					int bytesRead;
					while ((bytesRead = input.read(buffer)) != -1) {
						output.write(buffer, 0, bytesRead);
					}
					Properties p = createProperties(file);
					result = saveProperties(file.getSerial(), p);
				} finally {
					output.close();
				}
			} finally {
				input.close();

			}
		} catch (IOException e) {
			result = false;
		}
		return result;
	}

	@Override
	protected FileMeta getMetaData(String fileCode, Properties props,
			boolean openfile) {
		FileMeta meta = null;
		try {
			meta = createMeta(fileCode, props, openfile ? new FileInputStream(
					DATA_DIR + "/" + fileCode) : null);

		} catch (FileNotFoundException e) {
			meta = null;
		}
		return meta;
	}

	@Override
	public boolean isSerialValid(final String code) {
		File dir = new File(DATA_DIR);
		File[] files = dir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.toUpperCase().equals(code.toUpperCase());
			}
		});
		return files.length == 0;
	}

	@Override
	protected boolean deleteProperties(String code) {
		File f = new File(DATA_DIR + "/" + code + Storage.PROPERTIES_EXT);
		return f.delete();
	}

	@Override
	protected boolean deleteThumbnails(final String code) {
		File dir = new File(DATA_DIR);
		File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(code + Storage.THUMB);
			}
		});

		boolean result = true;
		for (File thumbfile : files) {
			result = result && thumbfile.delete();
		}

		return result;
	}

	@Override
	protected boolean deleteFile(String code) {
		File file = new File(DATA_DIR + "/" + code);
		return file.delete();
	}

	@Override
	public Properties getProperties(String fileCode) {
		Properties props = new Properties();
		InputStream is = null;

		// First try loading from the current directory
		try {
			File f = new File(DATA_DIR + "/" + fileCode
					+ Storage.PROPERTIES_EXT);
			is = new FileInputStream(f);
			props.load(is);
			is.close();
		} catch (Exception e) {
			is = null;
			props = null;
		}
		return props;
	}

	@Override
	protected boolean isThumbExists(String thumbName) {
		File f = new File(DATA_DIR + "/" + thumbName);
		return f.exists();
	}

	@Override
	protected InputStream getInputStream(String fileCode) {
		try {
			return new FileInputStream(DATA_DIR + "/" + fileCode);
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	@Override
	protected boolean writeThumb(Builder<? extends InputStream> thumb,
			String thumbName) {
		try {
			thumb.toFile(DATA_DIR + "/" + thumbName);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	protected boolean writeThumb(BufferedImage bufferedImage, String thumbName) {
		boolean result = true;

		try {
			File f = new File(DATA_DIR + "/" + thumbName);
			ImageIO.write(bufferedImage, Storage.THUMB_EXT, f);
		} catch (IOException e) {
			result = false;
		}
		return result;
	}

	@Override
	protected boolean saveProperties(String fileCode, Properties props) {
		boolean result = true;
		File f = new File(DATA_DIR + "/" + fileCode + Storage.PROPERTIES_EXT);
		OutputStream out;
		try {
			out = new FileOutputStream(f);
			props.store(out, "Properties for " + fileCode);
			out.close();
		} catch (IOException e) {
			result = false;
		}
		return result;
	}


}

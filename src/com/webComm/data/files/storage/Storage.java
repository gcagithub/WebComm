package com.webComm.data.files.storage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.webComm.data.files.model.ContentType;
import com.webComm.data.files.model.FileMeta;
import com.webComm.data.files.storage.thumbnails.ThumbnailProperties;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;

public abstract class Storage {
	public static final String PROPERTY_OWNER = "owner";
	private static final String PROPERTY_TYPE = "type";
	private static final String PROPERTY_REALSIZE = "realsize";
	private static final String PROPERTY_SIZE = "size";
	private static final String PROPERTY_NAME = "name";
	private static final String PROPERY_CONTENT = "content";
	public static final String THUMB_EXT = "jpg";
	public static final String PROPERTIES_EXT = ".properties";
	public static final String THUMB = ".thumb";

	public abstract boolean store(FileMeta file);

	public final FileMeta read(String fileCode, boolean openfile) {
		FileMeta meta = null;

		Properties props = getProperties(fileCode);

		if (props != null) {
			meta = getMetaData(fileCode, props, openfile);
		}
		return meta;
	}

	public abstract boolean isSerialValid(String code);

	public final boolean delete(String code) {
		boolean result = true;
		result = deleteFile(code);
		if (result) {
			result = deleteThumbnails(code);
			if (result) {
				result = deleteProperties(code);
			}
		}
		return result;
	}

	protected abstract boolean deleteProperties(String code);

	protected abstract boolean deleteThumbnails(String code);

	protected abstract boolean deleteFile(String code);

	public abstract Properties getProperties(String fileCode);

	protected abstract FileMeta getMetaData(String fileCode, Properties props,
			boolean openfile);

	protected final FileMeta createMeta(String fileCode, Properties props,
			InputStream filestream) throws FileNotFoundException {
		FileMeta meta = new FileMeta();
		meta.setContent(filestream);
		meta.setFileName(props.getProperty(PROPERTY_NAME));
		meta.setFileRealSize(Long.getLong(props.getProperty(PROPERTY_REALSIZE)));
		meta.setFileSize(props.getProperty(PROPERTY_SIZE));
		meta.setFileType(props.getProperty(PROPERTY_TYPE));
		meta.setOwnerCode(props.getProperty(PROPERTY_OWNER));
		String content = props.getProperty(PROPERY_CONTENT);
		if (content == null) {
			content = ContentType.DEFAULT_TYPE;
		}
		meta.setContentType(content);
		meta.setSerial(fileCode);
		return meta;
	}

	protected final Properties createProperties(FileMeta file) {
		Properties p = new Properties();
		p.setProperty(PROPERTY_NAME, file.getFileName());
		p.setProperty(PROPERTY_SIZE, file.getFileSize());
		p.setProperty(PROPERTY_REALSIZE, file.getFileRealSize().toString());
		p.setProperty(PROPERTY_TYPE, file.getFileType());
		p.setProperty(PROPERTY_OWNER, file.getOwnerCode());
		p.setProperty(PROPERY_CONTENT, file.getContentType());
		return p;
	}

	public final FileMeta readThumbnail(String fileCode,
			ThumbnailProperties thumbnailProperties) {
		FileMeta meta = null;
		Properties props = getProperties(fileCode);
		if (props != null) {
			String thumbName = getThumb(fileCode, thumbnailProperties);
			meta = getMetaData(thumbName, props, true);
		}
		return meta;
	}

	protected abstract boolean isThumbExists(String thumbName);

	protected String getThumb(String fileCode,
			ThumbnailProperties thumbnailProperties) {
		String thumbName = fileCode + THUMB + "_"
				+ thumbnailProperties.getWidth() + "_"
				+ thumbnailProperties.getHeight() + "." + THUMB_EXT;
		if (!isThumbExists(thumbName)) {
			if (!createThumb(fileCode, thumbName, thumbnailProperties)) {
				thumbName = fileCode;
			}
		}
		return thumbName;
	}

	private boolean createThumb(String fileCode, String thumbName,
			ThumbnailProperties thumbnailProperties) {
		boolean result = false;
		try {
			InputStream inputStream = getInputStream(fileCode);
			if (inputStream != null) {
				int width = thumbnailProperties.getWidth();
				int height = thumbnailProperties.getHeight();
				BufferedImage bufferedImage = Thumbnails.of(inputStream)
						.size(width, height).asBufferedImage();

				BufferedImage outputImage = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D g2 = outputImage.createGraphics();
				g2.setColor(new Color(240, 240, 240, 255));
				g2.fillRect(0, 0, width, height);
				g2.drawImage(bufferedImage, null,
						(width - bufferedImage.getWidth()) / 2,
						(height - bufferedImage.getHeight()) / 2);
				g2.dispose();
				result = writeThumb(outputImage, thumbName);
				inputStream.close();
			}
		} catch (IOException e) {
			result = false;
		}
		return result;
	}

	protected abstract boolean writeThumb(BufferedImage bufferedImage,
			String thumbName);

	protected abstract boolean writeThumb(Builder<? extends InputStream> thumb,
			String thumbName);

	protected abstract InputStream getInputStream(String fileCode);

	public String updateContentType(String fileCode, String contentType) {
		Properties props = getProperties(fileCode);
		String oldType = props.getProperty(PROPERY_CONTENT);
		if (oldType == null) {
			oldType = ContentType.DEFAULT_TYPE;
		}
		props.setProperty(PROPERY_CONTENT, contentType);
		boolean result = saveProperties(fileCode, props);
		if (result) {
			return contentType;
		} else {
			return oldType;
		}
	}

	protected abstract boolean saveProperties(String fileCode, Properties props);

	public String getContentType(String fileCode) {
		String contentType = null;
		Properties props = getProperties(fileCode);
		if (props != null) {
			contentType = props.getProperty(PROPERY_CONTENT);
		}
		if (contentType == null) {
			contentType = ContentType.DEFAULT_TYPE;
		}
		return contentType;
	}

	public List<FileMeta> loadProperties(List<String> fileCodes) {
		List<FileMeta> metas = new ArrayList<FileMeta>();
		for (String code : fileCodes) {
			FileMeta meta = read(code, false);
			if (meta != null) {
				metas.add(meta);
			}
		}
		return metas;
	}

}

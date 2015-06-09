package com.webComm.utils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import org.entityfs.Directory;
import org.entityfs.EFile;
import org.entityfs.FileSystem;
import org.entityfs.el.RelativeLocation;
import org.entityfs.fs.FSRWFileSystemBuilder;
import org.entityfs.util.Directories;

/**
 * Virtual File System
 */
public class VFS {

	private static String vfsdir;
	private static FileSystem realfs;

	public static void init(String userapphome, String appfileprefix) {
		vfsdir = userapphome;
		realfs = new FSRWFileSystemBuilder().setName(appfileprefix)
				.setRoot(new File(vfsdir)).create();

	}

	public static void stop() {

	}

	public static Directory getRoot() {
		return realfs.getRootDirectory();
	}

	public static EFile createFile(String filename) {
		Directory root = getRoot();
		EFile file = Directories.newFile(root, new RelativeLocation(filename));
		return file;
	}

	public static boolean isFileExists(String filename) {
		Directory root = getRoot();
		return Directories.containsEntity(root, new RelativeLocation(filename));
	}

	public static EFile open(String filename) {
		Directory root = getRoot();
		RelativeLocation rl = new RelativeLocation(filename);
		if (!Directories.containsEntity(root, rl)) {
			Directories.newFile(root, rl);
		}
		return Directories.getFile(root, rl);

	}

	public static Set<EFile> list(String mask) {
		Directory root = getRoot();
		Set<EFile> lst = null;
		lst = Directories.getAllFilesMatching(root, mask);

		return lst;
	}

	public static OutputStream openForWrite(String filename) {
		EFile file = open(filename);
		OutputStream fos = file.openForWrite();
		return fos;
	}

	public static InputStream openForRead(String filename) {
		EFile file = open(filename);
		InputStream fis = file.openForRead();
		return fis;
	}
}

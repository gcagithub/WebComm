package com.webComm.data.files;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.webComm.data.files.controller.FilesController;
import com.webComm.data.files.storage.Storage;

public class FilesSession {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setup(String storageClassName) {

		try {
			Class storageClass = Class.forName(storageClassName);
			Class[] types = {  };
			Constructor constructor = storageClass.getConstructor(types);
			Storage instanceOfStorageClass = (Storage) constructor
					.newInstance();
			setup(instanceOfStorageClass);

		} catch (ClassNotFoundException | NoSuchMethodException
				| SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			setup((Storage)null);
		}

	}

	public static void setup(Storage storage) {
		FilesController.setStorage(storage);
	}
}

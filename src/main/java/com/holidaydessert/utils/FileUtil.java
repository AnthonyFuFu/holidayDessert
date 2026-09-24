package com.holidaydessert.utils;

public class FileUtil {

    public static String initFileVersion(String fileVersion) {
        if (fileVersion == null || fileVersion.trim().isEmpty()) {
            return "1";
        }
        return fileVersion.trim();
    }

    public static String increaseFileVersion(String fileVersion) {
        try {
            int version = Integer.parseInt(fileVersion);
            return String.valueOf(version + 1);
        } catch (NumberFormatException e) {
            return "1";
        }
    }
    
	public static String buildBackupKey(String objectKey, String fileVersion) {
		int slashIndex = objectKey.indexOf("/");

		if (slashIndex < 0) {
			return "BACKUP/" + objectKey + "_v" + fileVersion;
		}

		String folder = objectKey.substring(0, slashIndex + 1);
		String fileName = objectKey.substring(slashIndex + 1);

		return folder + "BACKUP/" + fileName + "_v" + fileVersion;
	}

	public static String removeFolderPrefix(String objectKey) {
	    if (objectKey == null || objectKey.trim().isEmpty()) {
	        return objectKey;
	    }
	    String normalizedKey = objectKey.trim().replace("\\", "/");
	    // 找到 /BACKUP/，取出後面的檔名
	    int backupIndex = normalizedKey.indexOf("/BACKUP/");
	    if (backupIndex >= 0) {
	        return normalizedKey.substring(backupIndex + "/BACKUP/".length());
	    }
	    // 如果沒有 BACKUP，取第一個資料夾後面的檔名
	    int slashIndex = normalizedKey.indexOf("/");
	    if (slashIndex >= 0) {
	        return normalizedKey.substring(slashIndex + 1);
	    }
	    // 沒有資料夾時直接回傳原始檔名
	    return normalizedKey;
	}
	
}

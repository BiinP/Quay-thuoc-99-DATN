package com.qlnt.service;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
	File save(MultipartFile file, String folder);
	List<File> save(MultipartFile[] files, String folder);
	void delete(String folder, String name);
}

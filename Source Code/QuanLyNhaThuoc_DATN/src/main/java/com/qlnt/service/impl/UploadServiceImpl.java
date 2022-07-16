package com.qlnt.service.impl;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.qlnt.service.UploadService;

@Service
public class UploadServiceImpl implements UploadService{
	@Autowired private ServletContext app;
	
	private Path getPath(String folder, String filename) {
		File dir = Paths.get(app.getRealPath("/images"), folder).toFile();
		if(!dir.exists()) {
			dir.mkdirs();
		}
		return Paths.get(dir.getAbsolutePath(), filename);
	}

	@Override
	public File save(MultipartFile file, String folder) {
		String s = System.currentTimeMillis() + file.getOriginalFilename();
		String filename = Integer.toHexString(s.hashCode()) + s.substring(s.lastIndexOf("."));
		Path path = this.getPath(folder, filename);
		try {
			file.transferTo(path);
			System.out.println(path);
			return path.toFile();
		} catch (Exception e) {
			 throw new RuntimeException(e);
		}
	}

	@Override
	public List<File> save(MultipartFile[] files, String folder) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void delete(String folder, String name) {
		Path path = this.getPath(folder, name);
		path.toFile().delete();
	}
}

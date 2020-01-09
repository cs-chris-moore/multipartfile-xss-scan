package junk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class Main {
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
		Main main = new Main();
		List<String> files = Arrays.asList("img/motorcyclist.svg", "img/bad.svg");
		for (String file : files) {
			main.checkForXss(file);
		}
	}

	/**
	 * This is the method which needs to be added to the service(s) in question.
	 * 
	 * @param path
	 * @throws Exception
	 */
	private void checkForXss(String path) throws Exception {
		log.debug("Checking: {}", path);
		MultipartFile svg = this.loadFile(path);

		// ============================================================================================================
		// This is the specific check which needs to be performed on uploaded svg files
		try (Scanner sc = new Scanner(svg.getInputStream(), "UTF-8");) {
			Pattern p = Pattern.compile("script");
			if (sc.findWithinHorizon(p, 0) != null) {
				throw new Exception("Potential malicious code detected…");
			}
			log.info("No malicious code detected");
		}
		// ============================================================================================================
	}

	/**
	 * Load the file from the class path.
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private MultipartFile loadFile(String path) throws IOException {
		File file = new File(Main.class.getClassLoader().getResource(path).getFile());
		FileItem fileItem = new DiskFileItemFactory().createItem("file", Files.probeContentType(file.toPath()), false,
				file.getName());
		try (InputStream in = new FileInputStream(file); OutputStream out = fileItem.getOutputStream()) {
			in.transferTo(out);
		} catch (Exception e) {
			log.warn("Fail… ", e);
		}

		CommonsMultipartFile multipartFile = new CommonsMultipartFile(fileItem);
		return multipartFile;
	}
}

package org.yogurtcat.server.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;

/**
 * 压缩工具类
 * 
 * @author s
 *
 */
public class ZipUtil {

	/**
	 * 解压
	 * @param src
	 * @param target
	 * @return
	 * @throws IOException 
	 * @throws ArchiveException 
	 */
	public static void archive(String srcPath, String targetPath) throws IOException, ArchiveException {
		try (InputStream in = Files.newInputStream(Paths.get(srcPath));
				ArchiveInputStream i = new ArchiveStreamFactory()
			    .createArchiveInputStream(new BufferedInputStream(in))) {
		    ArchiveEntry entry = null;
		    while ((entry = i.getNextEntry()) != null) {
		        if (!i.canReadEntryData(entry)) {
		            // log something?
		            continue;
		        }
		        String name = filename(targetPath, entry.getName());;
		        File f = new File(name);
		        if (entry.isDirectory()) {
		            if (!f.isDirectory() && !f.mkdirs()) {
		                throw new IOException("failed to create directory " + f);
		            }
		        } else {
		            File parent = f.getParentFile();
		            if (!parent.isDirectory() && !parent.mkdirs()) {
		                throw new IOException("failed to create directory " + parent);
		            }
		            try (OutputStream o = Files.newOutputStream(f.toPath())) {
		                IOUtils.copy(i, o);
		            }
		        }
		    }
		}
	}
	
	/**
	 * 压缩
	 * @param srcPath
	 * @param targetPath
	 * @return
	 * @throws IOException 
	 * @throws ArchiveException 
	 */
	public static void compress(List<String> srcPaths, String targetPath) throws IOException, ArchiveException {
		Collection<File> filesToArchive = srcPaths.stream().map(s -> new File(s)).collect(Collectors.toList());
		try (OutputStream out = Files.newOutputStream(Paths.get(targetPath));
				ArchiveOutputStream o = new ArchiveStreamFactory()
			    .createArchiveOutputStream(ArchiveStreamFactory.ZIP, new BufferedOutputStream(out))) {
		    for (File f : filesToArchive) {
		        
		        Path dirPath = f.toPath();
	            Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {
	                @Override
	                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
	                    ArchiveEntry entry = o.createArchiveEntry(dir.toFile(), dirPath.getParent().relativize(dir).toString());
	                    o.putArchiveEntry(entry);
	                    o.closeArchiveEntry();
	                    return super.preVisitDirectory(dir, attrs);
	                }

	                @Override
	                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
	                	ArchiveEntry entry = o.createArchiveEntry(file.toFile(), dirPath.getParent().relativize(file).toString());
	                    o.putArchiveEntry(entry);
	                    try (InputStream i = Files.newInputStream(file)) {
			                IOUtils.copy(i, o);
			            }
	                    o.closeArchiveEntry();
	                    return super.visitFile(file, attrs);
	                }

	            });
		    }
		    
		    o.finish();
		}
	}
	
	/**
	 * 拼接文件名
	 * @param destDir
	 * @param name
	 * @return
	 */
	private static String filename(String destDir, String name) {
        return destDir + File.separator + name;
    }
	
}
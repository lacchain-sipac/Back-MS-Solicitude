package com.invest.honduras.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;

import com.everis.blockchain.honduras.util.HashUtils;
import com.invest.honduras.domain.model.Storage;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class FileUtil {

	public static Mono<String> saveFiles(Storage storage, String volumenPath, FilePart file) {

		log.info("saveFile id:{}, volumenPath:{}, namefile:{}", storage.getId(), volumenPath, storage.getFileName());

		Path location = Paths.get(volumenPath.concat("/" + storage.getId())).toAbsolutePath().normalize();//NOSONAR 

		try {
			Files.createDirectories(location);
		} catch (IOException e1) {
			log.error("Error.create File", e1);
		}

		Path temp = location.resolve(FilenameUtils.getName(storage.getFileName()));

		log.info("location {}", location.toString());

		if (Files.exists(temp)) {
			try {
				Files.delete(temp);
			} catch (IOException e) {
				log.error("Error.delete File", e);
			}
		}

		return getByteArray(file).map(bytes -> {
			try {
				Path tempfile = Files.createFile(temp);
				log.info("file after save {}", tempfile.getFileName());
				FileUtils.writeByteArrayToFile(tempfile.toFile(), bytes);
			} catch (IOException e) {
				log.error("Error.save File", e);
			}
			storage.setHash(HashUtils.convetStringToHash256(bytes));

			bytes = null;

			return "ok";
		});

	}

	public static Mono<byte[]> getByteArray(FilePart filePart) {
		return DataBufferUtils.join(filePart.content()).map(dataBuffer -> {
			DataBufferUtils.release(dataBuffer);
			return dataBuffer.asByteBuffer();
		}).map(ByteBuffer::array);
	}

	public static Resource getFile(String filename, String id, final String volumenPath) throws IOException {

		
		Path location = Paths.get(volumenPath.concat("/" + id)).toAbsolutePath().normalize();//NOSONAR

		Path filePath = location.resolve(FilenameUtils.getName(filename)).normalize();

		Resource resource = new UrlResource(filePath.toUri());

		return resource;

	}
}

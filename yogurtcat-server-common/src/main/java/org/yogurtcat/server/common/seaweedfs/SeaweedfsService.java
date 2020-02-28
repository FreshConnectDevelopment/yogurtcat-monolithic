package org.yogurtcat.server.common.seaweedfs;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.yogurtcat.server.common.seaweedfs.domain.DeleteFilesOrDirectoryRequest;
import org.yogurtcat.server.common.seaweedfs.domain.DeleteFilesOrDirectoryResponse;
import org.yogurtcat.server.common.seaweedfs.domain.DownloadRequest;
import org.yogurtcat.server.common.seaweedfs.domain.DownloadResponse;
import org.yogurtcat.server.common.seaweedfs.domain.ListFilesUnderDirectoryRequest;
import org.yogurtcat.server.common.seaweedfs.domain.ListFilesUnderDirectoryResponse;
import org.yogurtcat.server.common.seaweedfs.domain.UploadRequest;
import org.yogurtcat.server.common.seaweedfs.domain.UploadResponse;

import reactor.core.publisher.Mono;

/**
 * seaweedfs存储服务操作类
 * 
 * @author heaven
 *
 */
@Service
public class SeaweedfsService {

	/**
	 * http客户端
	 */
	private WebClient webClient;

	@PostConstruct
	public void init() {
		webClient = WebClient.create();
	}

	/**
	 * 文件上传
	 * 
	 * @param request
	 * @return
	 */
	public UploadResponse upload(UploadRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<FileSystemResource> entity = new HttpEntity<>(new FileSystemResource(request.getFile()), headers);
		Mono<UploadResponse> response = webClient.post().uri("http://" + request.getServerIp() + ":8888/javascript/" + request.getPath())
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.body(BodyInserters.fromMultipartData(request.getFile().getName(), entity)).retrieve()
				.bodyToMono(UploadResponse.class);
		UploadResponse uploadResponse = response.block();
		uploadResponse.setUrl("http://" + request.getServerIp() + ":8888/javascript/" + request.getPath()
				+ request.getFile().getName());
		return uploadResponse;
	}

	/**
	 * 文件获取
	 * 
	 * @return
	 * @throws IOException
	 */
	public DownloadResponse downloadFile(DownloadRequest request) throws IOException {
		Mono<ClientResponse> clientResponseMono = webClient.get().uri(request.getDownloadUrl())
				.accept(MediaType.APPLICATION_OCTET_STREAM).exchange();
		ClientResponse clientResponse = clientResponseMono.block();
		Resource resource = clientResponse.bodyToMono(Resource.class).block();
		File file = new File(request.getStorePath() + "/" + request.getName());
		FileUtils.copyInputStreamToFile(resource.getInputStream(), file);
		return DownloadResponse.builder().file(file).build();
	}

	/**
	 * 获取目录下文件列表
	 * 
	 * @param directory
	 * @return
	 */
	public ListFilesUnderDirectoryResponse listFilesUnderDirectory(ListFilesUnderDirectoryRequest request) {
		StringBuffer buffer = new StringBuffer();
		if (!StringUtils.isEmpty(request.getPath())) {
			buffer.append("/" + request.getPath());
		}
		if (request.getLastFileName().isPresent()) {
			buffer.append("&" + request.getLastFileName().get());
		}
		if (request.getLimit().isPresent()) {
			buffer.append("&" + request.getLimit().get());
		}

		Mono<ListFilesUnderDirectoryResponse> response = webClient.get()
				.uri("http://" + request.getServerIp() + " :8888/javascript?pretty=y" + buffer.toString())
				.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(ListFilesUnderDirectoryResponse.class);
		return response.block();
	}

	/**
	 * 删除文件或者目录
	 * 
	 * @param request
	 * @return
	 */
	public DeleteFilesOrDirectoryResponse deleteFilesOrDirectory(DeleteFilesOrDirectoryRequest request) {
		StringBuffer buffer = new StringBuffer();
		if (!StringUtils.isEmpty(request.getPath())) {
			buffer.append("/" + request.getPath());
		}
		if (request.isDirectory()) {
			buffer.append("&?recursive=true");
		}
		Mono<ClientResponse> clientResponseMono = webClient.delete()
				.uri("http://" + request.getServerIp() + ":8888/javascript" + buffer.toString())
				.accept(MediaType.APPLICATION_JSON).exchange();
		ClientResponse clientResponse = clientResponseMono.block();
		Mono<DeleteFilesOrDirectoryResponse> response = clientResponse.bodyToMono(DeleteFilesOrDirectoryResponse.class);
		return response.block();
	}
}

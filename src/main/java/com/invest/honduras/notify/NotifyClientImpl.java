package com.invest.honduras.notify;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.invest.honduras.notify.request.ItemUserNotifyRequest;
import com.invest.honduras.util.Constant;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class NotifyClientImpl implements NotifyClient{

	private final WebClient webClient;

	public NotifyClientImpl() {
		this.webClient = WebClient.builder().baseUrl(Constant.HOST_NOTIFY)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}

	@Override
	public Mono<Boolean> sendUpdateNotify(ItemUserNotifyRequest request) {
		request.setTypeNotify("General");
		log.info("NotifyClient.sendUpdateNotify==" + Constant.HOST_NOTIFY + Constant.API_URL_SEND_NOTIFY);
		
		this.webClient.post().uri(Constant.API_URL_SEND_NOTIFY)
				.accept(MediaType.APPLICATION_JSON).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(request), ItemUserNotifyRequest.class).exchange().doOnError(e -> {
					System.out.println("doOnError:sendUpdateNotify " + e.getMessage());
					throw new RuntimeException();
				}).subscribe(data -> {
					System.out.println("enviando correo--->>>>" + data.statusCode());
				});

		return Mono.just(Boolean.TRUE);
	}

}

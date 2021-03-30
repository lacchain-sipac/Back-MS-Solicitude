package com.invest.honduras.notify;

import com.invest.honduras.notify.request.ItemUserNotifyRequest;

import reactor.core.publisher.Mono;

public interface NotifyClient {

	Mono<Boolean> sendUpdateNotify(ItemUserNotifyRequest request);

}

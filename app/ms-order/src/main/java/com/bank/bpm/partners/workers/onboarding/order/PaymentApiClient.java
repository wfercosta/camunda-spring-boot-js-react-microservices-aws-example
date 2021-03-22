package com.bank.bpm.partners.workers.onboarding.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "payment-api", url = "http://localhost:9561/v1/payments")
public interface PaymentApiClient {

	@RequestMapping(method = RequestMethod.POST, value = "/payment", consumes = "application/json")
	CreatePaymentResponse create(CreatePaymentRequest request);

}

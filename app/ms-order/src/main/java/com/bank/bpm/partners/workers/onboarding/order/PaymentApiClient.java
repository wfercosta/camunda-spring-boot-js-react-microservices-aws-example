package com.bank.bpm.partners.workers.onboarding.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "payment-api", url = "${feign.payment-api.base-url}")
public interface PaymentApiClient {

    @RequestMapping(method = RequestMethod.POST, value = "/v1/payments/payment", consumes = "application/json")
    CreatePaymentResponse create(CreatePaymentRequest request);

}

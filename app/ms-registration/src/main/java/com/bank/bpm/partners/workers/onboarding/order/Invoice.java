package com.bank.bpm.partners.workers.onboarding.order;

import lombok.*;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

	private Long id;

	private Double total;

	@EqualsAndHashCode.Exclude
	private Order order;

	@EqualsAndHashCode.Exclude
	private LocalDateTime createdAt;

	@EqualsAndHashCode.Exclude
	private LocalDateTime updatedAt;

}

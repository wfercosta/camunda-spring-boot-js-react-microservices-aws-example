package com.bank.bpm.partners.workers.onboarding.order;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode
@Entity
@Table(name = "purchase_order")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_order_seq")
	@SequenceGenerator(name = "purchase_order_seq", sequenceName = "purchase_order_seq", allocationSize = 1)
	private Long id;

	@EqualsAndHashCode.Exclude
	@OneToOne(mappedBy = "order")
	@JsonManagedReference
	@Transient
	private OrderItem item;

	@EqualsAndHashCode.Exclude
	private Double cost;
}

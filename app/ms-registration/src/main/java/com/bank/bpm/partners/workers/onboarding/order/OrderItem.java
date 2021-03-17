package com.bank.bpm.partners.workers.onboarding.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode
@Entity
@Table(name = "purchase_order_item")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_order_item_seq")
	@SequenceGenerator(name = "purchase_order_item_seq", sequenceName = "purchase_order_item_seq", allocationSize = 1)
	private Long id;

	@Column(name = "is_virtual")
	private boolean virtual;

	@OneToOne
	@JoinColumn(name = "purchase_order_id", referencedColumnName = "id")
	@JsonBackReference
	private Order order;
}

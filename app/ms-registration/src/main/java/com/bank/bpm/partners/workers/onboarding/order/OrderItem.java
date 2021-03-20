package com.bank.bpm.partners.workers.onboarding.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString
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

	private String sku;

	private Double price;

	private int quantity;

	@ManyToOne
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	@JsonBackReference
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Order order;

	@CreatedDate
	@EqualsAndHashCode.Exclude
	private LocalDateTime createdAt;

	@LastModifiedDate
	@EqualsAndHashCode.Exclude
	private LocalDateTime updatedAt;
}

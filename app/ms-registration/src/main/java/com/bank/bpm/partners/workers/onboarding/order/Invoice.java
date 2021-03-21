package com.bank.bpm.partners.workers.onboarding.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Data
@EqualsAndHashCode
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoices_seq")
	@SequenceGenerator(name = "invoices_seq", sequenceName = "invoices_seq", allocationSize = 1)
	private Long id;

	private Double total;

	@EqualsAndHashCode.Exclude
	@JsonBackReference("order-invoice")
	@OneToOne
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private Order order;

	@CreatedDate
	@EqualsAndHashCode.Exclude
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@EqualsAndHashCode.Exclude
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime updatedAt;

}

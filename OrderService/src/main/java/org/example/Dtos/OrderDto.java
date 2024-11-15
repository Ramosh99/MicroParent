package org.example.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Models.OrderItem;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    @Id
    private String id;
    private String userId;
    private Date dateCreated;
    private String status;
    private List<OrderItem> items;
}
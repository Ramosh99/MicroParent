package org.example.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDto {

        private String id;
        private String userId;
        private Date dateCreated;
        private String status;
        private List<OrderItemDto> items;
    }



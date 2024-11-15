package org.example.Dtos;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateOrderStatusRequest {
        private String id;
        private String status;
}

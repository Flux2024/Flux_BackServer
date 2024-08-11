package com.flux.wish.model;

import com.flux.market.model.MarketDTO;
import com.flux.user.model.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishDTO {

        private Integer wishId;
        private String marketName;
        private List<String> marketImgs;

}

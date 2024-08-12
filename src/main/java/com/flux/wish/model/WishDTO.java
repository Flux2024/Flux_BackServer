package com.flux.wish.model;


import lombok.*;

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

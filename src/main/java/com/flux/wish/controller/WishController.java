package com.flux.wish.controller;

import com.flux.wish.model.Wish;
import com.flux.wish.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/wish")
@CrossOrigin(origins = "http://localhost:8000")
@Tag(name = "Wish Controller", description = "찜목록 관련 컨트롤러")
public class WishController {

    private final WishService wishService;

    @Autowired
    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @Operation(
            summary = "찜목록 전체 조회",
            description = "모든 찜목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인해 찜목록을 조회할 수 없습니다.")
    })
    @GetMapping
    public List<Wish> getAllWish() {
        return wishService.getAllWish();
    }

    @Operation(
            summary = "찜목록 상세 조회",
            description = "ID를 기반으로 특정 찜목록의 상세 정보를 조회합니다.",
            parameters = {
                    @Parameter(name = "wishId", description = "조회할 찜목록의 ID", example = "1")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "404", description = "해당 ID를 가진 찜목록을 찾을 수 없습니다.")
    })
    @GetMapping("/{wishId}")
    public ResponseEntity<Wish> getWishById(@PathVariable Integer wishId) {
        Optional<Wish> wishlist = wishService.getWishById(wishId);

        return wishlist.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "찜목록 생성",
            description = "새로운 찜목록을 생성합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "생성할 찜목록의 정보",
                    required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공적으로 생성되었습니다."),
            @ApiResponse(responseCode = "400", description = "요청이 잘못되었습니다.")
    })
    @PostMapping
    public Wish createWish(@RequestBody Wish wish) {
        return wishService.createWish(wish);
    }

    @Operation(
            summary = "찜목록 삭제",
            description = "ID를 기반으로 특정 찜목록을 삭제합니다.",
            parameters = {
                    @Parameter(name = "wishId", description = "삭제할 찜목록의 ID", example = "1")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공적으로 삭제되었습니다."),
            @ApiResponse(responseCode = "404", description = "해당 ID를 가진 찜목록을 찾을 수 없습니다.")
    })
    @DeleteMapping("/{wishId}")
    public ResponseEntity<Wish> deleteWish(@PathVariable Integer wishId) {
        wishService.deleteWish(wishId);
        return ResponseEntity.noContent().build();
    }
}

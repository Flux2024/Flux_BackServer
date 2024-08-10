package com.flux.comment.controller;

import com.flux.comment.model.Comment;
import com.flux.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/comments")
@CrossOrigin(origins = "http://localhost:8000")
@Tag(name = "Comment API", description = "댓글 관련 컨트롤러")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "아티클ID에 의한 댓글리스트 호출", description = "특정 아티클 ID에 대한 모든 댓글을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 목록이 성공적으로 반환되었습니다.",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. articleId가 유효하지 않습니다.", content = @Content),
            @ApiResponse(responseCode = "404", description = "아티클 ID에 해당하는 댓글을 찾을 수 없습니다.", content = @Content)
    })
    @GetMapping("/article/{id}")
    public ResponseEntity<List<Comment>> getCommentsByArticleId(@PathVariable Integer id) {
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Comment> comments = commentService.getCommentsByArticleId(id);
        if (comments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @Operation(summary = "새로운 댓글을 작성합니다.", description = "새로운 댓글을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "댓글이 성공적으로 생성되었습니다.",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 필수 필드가 누락되었습니다.", content = @Content)
    })
    @PostMapping("/{articleId}")
    public ResponseEntity<Comment> createComment(@PathVariable Integer articleId, @RequestBody Comment comment) {
        try {
            if (articleId <= 0 ||
                    comment.getUserId() == null || comment.getUserId() <= 0 ||
                    comment.getCommentContents() == null || comment.getCommentContents().trim().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            comment.setArticleId(articleId);
            Comment createdComment = commentService.createComment(comment);
            return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 유효성 검증 실패
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 서버 내부 오류
        }
    }

    @Operation(summary = "댓글을 수정합니다.", description = "기존의 댓글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글이 성공적으로 수정되었습니다.",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 필수 필드가 누락되었거나 ID가 유효하지 않습니다.", content = @Content),
            @ApiResponse(responseCode = "404", description = "해당 ID의 댓글을 찾을 수 없습니다.", content = @Content)
    })
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer commentId, @RequestBody Comment updatedComment) {
        if (commentId <= 0 || updatedComment.getCommentContents() == null ||
                updatedComment.getCommentContents().trim().isEmpty() ||
                updatedComment.getArticleId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Comment> existingComment = Optional.ofNullable(commentService.updateComment(commentId, updatedComment));
        if (existingComment.isPresent()) {
            return new ResponseEntity<>(existingComment.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "댓글을 삭제합니다.", description = "특정 ID의 댓글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "댓글이 성공적으로 삭제되었습니다.", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. ID가 유효하지 않습니다.", content = @Content),
            @ApiResponse(responseCode = "404", description = "해당 ID의 댓글을 찾을 수 없습니다.", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            commentService.deleteComment(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "댓글에 추천을 하는 메서드", description = "특정 ID의 댓글을 추천합니다.")
    @PutMapping("/{id}/like")
    public ResponseEntity<Comment> likeComment(@PathVariable Integer id) {
        Comment updatedComment = commentService.likeComment(id);
        return ResponseEntity.ok(updatedComment);
    }
}

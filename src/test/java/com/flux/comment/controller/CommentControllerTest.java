package com.flux.comment.controller;

import com.flux.comment.model.Comment;
import com.flux.comment.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest {

    @Mock
    private CommentService commentService; // CommentService를 Mocking하여 테스트 진행

    @InjectMocks
    private CommentController commentController; // Mock된 서비스가 주입된 CommentController 객체 생성

    private MockMvc mockMvc; // Spring MVC 테스트를 위한 MockMvc 객체

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build(); // MockMvc 설정
    }

    // getCommentsByArticleId 테스트
    @Test
    void testGetCommentsByArticleId_ValidId_ReturnsComments() throws Exception {
        // 댓글 객체 생성 및 설정
        Comment comment = new Comment();
        comment.setUserId(1);
        comment.setArticleId(1);
        comment.setCommentContents("Test Comment");

        // Mock Service 설정: 특정 ID에 대한 댓글 목록 반환
        when(commentService.getCommentsByArticleId(1)).thenReturn(Arrays.asList(comment));

        // API 호출 및 검증
        mockMvc.perform(get("/api/v1/comments/article/1"))
                .andExpect(status().isOk()); // HTTP 200 OK 예상

        // Service 메서드가 한 번 호출되었는지 검증
        verify(commentService, times(1)).getCommentsByArticleId(1);
    }

    @Test
    void testGetCommentsByArticleId_InvalidId_ReturnsBadRequest() throws Exception {
        // 잘못된 ID로 API 호출 및 검증
        mockMvc.perform(get("/api/v1/comments/article/-1"))
                .andExpect(status().isBadRequest()); // HTTP 400 Bad Request 예상
    }

    @Test
    void testGetCommentsByArticleId_NoCommentsFound_ReturnsNotFound() throws Exception {
        // Mock Service 설정: 댓글이 없는 경우
        when(commentService.getCommentsByArticleId(1)).thenReturn(Collections.emptyList());

        // API 호출 및 검증
        mockMvc.perform(get("/api/v1/comments/article/1"))
                .andExpect(status().isNotFound()); // HTTP 404 Not Found 예상
    }

    // createComment 테스트
    @Test
    void testCreateComment_ValidComment_ReturnsCreated() {
        // 댓글 객체 생성 및 설정
        Comment comment = new Comment();
        comment.setUserId(1);
        comment.setArticleId(1);
        comment.setCommentContents("Test Comment");

        // Mock Service 설정: 댓글 생성 시 동일한 객체 반환
        when(commentService.createComment(comment)).thenReturn(comment);

        // Controller 메서드 호출 및 응답 검증
        ResponseEntity<Comment> response = commentController.createComment(comment);

        // 응답 상태와 본문 검증
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(comment, response.getBody());
        verify(commentService, times(1)).createComment(comment); // Service 메서드 호출 검증
    }

    @Test
    void testCreateComment_InvalidComment_ReturnsBadRequest() {
        // 잘못된 댓글 객체 생성 (필수 필드 누락)
        Comment comment = new Comment();

        // Controller 메서드 호출 및 응답 검증
        ResponseEntity<Comment> response = commentController.createComment(comment);

        // 응답 상태 검증
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(commentService, times(0)).createComment(any(Comment.class)); // Service 메서드 호출되지 않음 검증
    }

    // updateComment 테스트
    @Test
    void testUpdateComment_ValidIdAndComment_ReturnsUpdatedComment() {
        // 수정할 댓글 객체 생성 및 설정
        Comment updatedComment = new Comment();
        updatedComment.setUserId(1);
        updatedComment.setCommentContents("Updated Comment");

        // Mock Service 설정: 댓글 수정 시 수정된 객체 반환
        when(commentService.updateComment(eq(1), any(Comment.class))).thenReturn(updatedComment);

        // Controller 메서드 호출 및 응답 검증
        ResponseEntity<Comment> response = commentController.updateComment(1, updatedComment);

        // 응답 상태와 본문 검증
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedComment, response.getBody());
        verify(commentService, times(1)).updateComment(eq(1), any(Comment.class)); // Service 메서드 호출 검증
    }

    @Test
    void testUpdateComment_InvalidId_ReturnsBadRequest() {
        // 잘못된 ID로 수정할 댓글 객체 생성
        Comment updatedComment = new Comment();
        updatedComment.setCommentContents("Updated Comment");

        // Controller 메서드 호출 및 응답 검증
        ResponseEntity<Comment> response = commentController.updateComment(-1, updatedComment);

        // 응답 상태 검증
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(commentService, times(0)).updateComment(anyInt(), any(Comment.class)); // Service 메서드 호출되지 않음 검증
    }

    @Test
    void testUpdateComment_CommentNotFound_ReturnsNotFound() {
        // Mock Service 설정: 댓글을 찾지 못하는 경우
        when(commentService.updateComment(eq(1), any(Comment.class))).thenReturn(null);

        // 수정할 댓글 객체 생성
        Comment updatedComment = new Comment();
        updatedComment.setCommentContents("Updated Comment");

        // Controller 메서드 호출 및 응답 검증
        ResponseEntity<Comment> response = commentController.updateComment(1, updatedComment);

        // 응답 상태 검증
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(commentService, times(1)).updateComment(eq(1), any(Comment.class)); // Service 메서드 호출 검증
    }

    // deleteComment 테스트
    @Test
    void testDeleteComment_ValidId_ReturnsNoContent() {
        // Mock Service 설정: 댓글 삭제 시 예외 없음
        doNothing().when(commentService).deleteComment(1);

        // Controller 메서드 호출 및 응답 검증
        ResponseEntity<Void> response = commentController.deleteComment(1);

        // 응답 상태 검증
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(commentService, times(1)).deleteComment(1); // Service 메서드 호출 검증
    }

    @Test
    void testDeleteComment_InvalidId_ReturnsBadRequest() {
        // 잘못된 ID로 Controller 메서드 호출
        ResponseEntity<Void> response = commentController.deleteComment(-1);

        // 응답 상태 검증
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(commentService, times(0)).deleteComment(anyInt()); // Service 메서드 호출되지 않음 검증
    }

    @Test
    void testDeleteComment_CommentNotFound_ReturnsNotFound() {
        // Mock Service 설정: 댓글 삭제 시 IllegalArgumentException 발생
        doThrow(new IllegalArgumentException()).when(commentService).deleteComment(1);

        // Controller 메서드 호출 및 응답 검증
        ResponseEntity<Void> response = commentController.deleteComment(1);

        // 응답 상태 검증
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(commentService, times(1)).deleteComment(1); // Service 메서드 호출 검증
    }

    // likeComment 테스트
    @Test
    void testLikeComment_ValidId_ReturnsUpdatedComment() {
        // 좋아요를 증가시킬 댓글 객체 생성 및 설정
        Comment updatedComment = new Comment();
        updatedComment.setUserId(1);
        updatedComment.setCommentContents("Liked Comment");

        // Mock Service 설정: 좋아요 증가 시 수정된 객체 반환
        when(commentService.likeComment(1)).thenReturn(updatedComment);

        // Controller 메서드 호출 및 응답 검증
        ResponseEntity<Comment> response = commentController.likeComment(1);

        // 응답 상태와 본문 검증
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedComment, response.getBody());
        verify(commentService, times(1)).likeComment(1); // Service 메서드 호출 검증
    }
}

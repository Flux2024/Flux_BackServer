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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    void testGetCommentsByArticleId_ValidId_ReturnsComments() throws Exception {
        Comment comment = new Comment();
        comment.setUserId(1);
        comment.setArticleId(1);
        comment.setCommentContents("Test Comment");

        when(commentService.getCommentsByArticleId(1)).thenReturn(Arrays.asList(comment));

        mockMvc.perform(get("/api/v1/comments/article/1"))
                .andExpect(status().isOk());

        verify(commentService, times(1)).getCommentsByArticleId(1);
    }

    @Test
    void testGetCommentsByArticleId_InvalidId_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/comments/article/-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCommentsByArticleId_NoCommentsFound_ReturnsNotFound() throws Exception {
        when(commentService.getCommentsByArticleId(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/comments/article/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateComment_ValidComment_ReturnsCreated() {
        Comment comment = new Comment();
        comment.setCommentId(1);
        comment.setUserId(1);
        comment.setCommentContents("Test Comment");

        when(commentService.createComment(comment)).thenReturn(comment);

        ResponseEntity<Comment> response = commentController.createComment(1, comment);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(comment, response.getBody());
        verify(commentService, times(1)).createComment(comment);
    }

    @Test
    void testCreateComment_InvalidComment_ReturnsBadRequest() {
        Comment comment = new Comment(); // Invalid comment (missing fields)

        ResponseEntity<Comment> response = commentController.createComment(1, comment);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(commentService, times(0)).createComment(any(Comment.class));
    }



    @Test
    void testUpdateComment_ValidIdAndComment_ReturnsUpdatedComment() {
        Comment updatedComment = new Comment();
        updatedComment.setUserId(1);
        updatedComment.setArticleId(1);
        updatedComment.setCommentContents("Updated Comment");

        when(commentService.updateComment(eq(1), any(Comment.class))).thenReturn(updatedComment);

        ResponseEntity<Comment> response = commentController.updateComment(1, updatedComment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedComment, response.getBody());
        verify(commentService, times(1)).updateComment(eq(1), any(Comment.class));
    }

    @Test
    void testUpdateComment_InvalidId_ReturnsBadRequest() {
        Comment updatedComment = new Comment();
        updatedComment.setCommentContents("Updated Comment");

        ResponseEntity<Comment> response = commentController.updateComment(-1, updatedComment);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(commentService, times(0)).updateComment(anyInt(), any(Comment.class));
    }

    @Test
    void testUpdateComment_CommentNotFound_ReturnsNotFound() {
        // Mock Service 설정: 댓글을 찾지 못하는 경우 null 반환
        when(commentService.updateComment(eq(1), any(Comment.class))).thenReturn(null);

        // 수정할 댓글 객체 생성 및 설정
        Comment updatedComment = new Comment();
        updatedComment.setCommentId(1); // ID가 있어야 하며
        updatedComment.setUserId(1);
        updatedComment.setArticleId(1); // 필수 필드 추가
        updatedComment.setCommentContents("Updated Comment");

        // Controller 메서드 호출 및 응답 검증
        ResponseEntity<Comment> response = commentController.updateComment(1, updatedComment);

        // 응답 상태 검증
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(commentService, times(1)).updateComment(eq(1), any(Comment.class));
    }


    @Test
    void testDeleteComment_ValidId_ReturnsNoContent() {
        doNothing().when(commentService).deleteComment(1);

        ResponseEntity<Void> response = commentController.deleteComment(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(commentService, times(1)).deleteComment(1);
    }

    @Test
    void testDeleteComment_InvalidId_ReturnsBadRequest() {
        ResponseEntity<Void> response = commentController.deleteComment(-1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(commentService, times(0)).deleteComment(anyInt());
    }

    @Test
    void testDeleteComment_CommentNotFound_ReturnsNotFound() {
        doThrow(new IllegalArgumentException()).when(commentService).deleteComment(1);

        ResponseEntity<Void> response = commentController.deleteComment(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(commentService, times(1)).deleteComment(1);
    }

    @Test
    void testLikeComment_ValidId_ReturnsUpdatedComment() {
        Comment likedComment = new Comment();
        likedComment.setUserId(1);
        likedComment.setCommentContents("Liked Comment");

        when(commentService.likeComment(1)).thenReturn(likedComment);

        ResponseEntity<Comment> response = commentController.likeComment(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(likedComment, response.getBody());
        verify(commentService, times(1)).likeComment(1);
    }
}

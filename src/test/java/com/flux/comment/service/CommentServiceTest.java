package com.flux.comment.service;

import com.flux.comment.model.Comment;
import com.flux.comment.repository.CommentRepository;
import com.flux.global.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository; // CommentRepository를 Mocking하여 실제 DB 접근 없이 테스트 진행

    @InjectMocks
    private CommentService commentService; // Mock된 의존성을 주입받은 CommentService 객체 생성

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito를 초기화하여 @Mock과 @InjectMocks 어노테이션이 동작하도록 설정
    }

    // getCommentsByArticleId 테스트
    @Test
    void testGetCommentsByArticleId_ValidId_ReturnsComments() {
        // 댓글 객체를 생성하고 설정
        Comment comment = new Comment();
        comment.setArticleId(1);
        comment.setCommentContents("Test Comment");

        // Mock Repository가 특정 ID에 대해 반환할 값을 설정
        when(commentRepository.findByArticleId(1)).thenReturn(Arrays.asList(comment));

        // 서비스 메서드 호출 및 결과 확인
        List<Comment> comments = commentService.getCommentsByArticleId(1);

        // 결과 검증
        assertEquals(1, comments.size());
        assertEquals("Test Comment", comments.get(0).getCommentContents());
        verify(commentRepository, times(1)).findByArticleId(1); // Repository 메서드 호출 횟수 검증
    }

    @Test
    void testGetCommentsByArticleId_InvalidId_ThrowsException() {
        // 잘못된 ID로 메서드를 호출했을 때 IllegalArgumentException이 발생하는지 테스트
        assertThrows(IllegalArgumentException.class, () -> commentService.getCommentsByArticleId(-1));
        verify(commentRepository, times(0)).findByArticleId(anyInt()); // Repository가 호출되지 않았음을 검증
    }

    // createComment 테스트
    @Test
    void testCreateComment_ValidComment_ReturnsCreatedComment() {
        // 댓글 객체 생성 및 설정
        Comment comment = new Comment();
        comment.setArticleId(1);
        comment.setUserId(1);
        comment.setCommentContents("New Comment");

        // Mock Repository 설정: 특정 ID에 댓글이 없고, 저장될 때 동일한 객체를 반환하도록 설정
        when(commentRepository.findByArticleId(1)).thenReturn(Arrays.asList());
        when(commentRepository.save(comment)).thenReturn(comment);

        // 서비스 메서드 호출 및 결과 검증
        Comment createdComment = commentService.createComment(comment);
        assertNotNull(createdComment);
        assertEquals("New Comment", createdComment.getCommentContents());
        verify(commentRepository, times(1)).save(comment); // Repository의 save 메서드 호출 검증
    }

    @Test
    void testCreateComment_DuplicateComment_ThrowsException() {
        // 중복된 댓글 객체 생성 및 설정
        Comment comment = new Comment();
        comment.setArticleId(1);
        comment.setUserId(1);
        comment.setCommentContents("Duplicate Comment");

        // Mock Repository 설정: 동일한 아티클에 이미 같은 내용의 댓글이 있는 경우
        when(commentRepository.findByArticleId(1)).thenReturn(Arrays.asList(comment));

        // 중복 댓글 작성 시 IllegalArgumentException이 발생하는지 테스트
        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(comment));
        verify(commentRepository, times(0)).save(any(Comment.class)); // 중복으로 인해 저장되지 않음을 검증
    }

    // updateComment 테스트
    @Test
    void testUpdateComment_ValidComment_ReturnsUpdatedComment() {
        // 기존 댓글 객체와 수정할 댓글 객체 생성 및 설정
        Comment existingComment = new Comment();
        existingComment.setCommentId(1);
        existingComment.setUserId(1);
        existingComment.setArticleId(1); // 기존 댓글에 ArticleId 설정
        existingComment.setCommentContents("Existing Comment");

        Comment updatedComment = new Comment();
        updatedComment.setUserId(1);
        updatedComment.setArticleId(1); // 수정할 댓글에 ArticleId 설정
        updatedComment.setCommentContents("Updated Comment");

        // Mock Repository 설정: 기존 댓글을 찾고, 저장 시 업데이트된 댓글을 반환하도록 설정
        when(commentRepository.findById(1)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(existingComment)).thenReturn(existingComment);

        // 서비스 메서드 호출 및 결과 검증
        Comment result = commentService.updateComment(1, updatedComment);
        assertEquals("Updated Comment", result.getCommentContents());
        verify(commentRepository, times(1)).save(existingComment); // 업데이트된 댓글 저장 확인
    }

    @Test
    void testUpdateComment_CommentNotFound_ThrowsException() {
        // 수정할 댓글 객체 생성 및 설정
        Comment updatedComment = new Comment();
        updatedComment.setUserId(1);
        updatedComment.setCommentContents("Updated Comment");

        // Mock Repository 설정: 특정 ID로 댓글을 찾지 못하는 경우
        when(commentRepository.findById(1)).thenReturn(Optional.empty());

        // 댓글이 없을 때 IllegalArgumentException이 발생하는지 테스트
        assertThrows(IllegalArgumentException.class, () -> commentService.updateComment(1, updatedComment));
        verify(commentRepository, times(0)).save(any(Comment.class)); // 댓글이 없으므로 저장이 호출되지 않음을 검증
    }

    @Test
    void testUpdateComment_UnauthorizedUser_ThrowsSecurityException() {
        // 기존 댓글 객체와 수정할 댓글 객체 생성 및 설정 (다른 유저 ID 설정)
        Comment existingComment = new Comment();
        existingComment.setCommentId(1);
        existingComment.setUserId(1);
        existingComment.setArticleId(1); // ArticleId 설정
        existingComment.setCommentContents("Existing Comment");

        Comment updatedComment = new Comment();
        updatedComment.setUserId(2); // 다른 유저 ID 설정
        updatedComment.setArticleId(1); // ArticleId 설정
        updatedComment.setCommentContents("Updated Comment");

        // Mock Repository 설정: 기존 댓글을 찾는 경우
        when(commentRepository.findById(1)).thenReturn(Optional.of(existingComment));

        // 다른 사용자가 수정하려 할 때 SecurityException이 발생하는지 테스트
        assertThrows(SecurityException.class, () -> commentService.updateComment(1, updatedComment));
        verify(commentRepository, times(0)).save(any(Comment.class)); // 권한이 없으므로 저장이 호출되지 않음을 검증
    }

    // deleteComment 테스트
    @Test
    void testDeleteComment_ValidId_DeletesComment() {
        // 기존 댓글 객체 생성 및 설정
        Comment existingComment = new Comment();
        existingComment.setArticleId(1);

        // Mock Repository 설정: 특정 ID로 댓글을 찾고 삭제하도록 설정
        when(commentRepository.findById(1)).thenReturn(Optional.of(existingComment));
        doNothing().when(commentRepository).deleteById(1);

        // 서비스 메서드 호출 및 결과 검증
        commentService.deleteComment(1);
        verify(commentRepository, times(1)).deleteById(1); // Repository의 deleteById 메서드가 호출되었는지 확인
    }

    @Test
    void testDeleteComment_CommentNotFound_ThrowsException() {
        // Mock Repository 설정: 특정 ID로 댓글을 찾지 못하는 경우
        when(commentRepository.findById(1)).thenReturn(Optional.empty());

        // 댓글이 없을 때 IllegalArgumentException이 발생하는지 테스트
        assertThrows(IllegalArgumentException.class, () -> commentService.deleteComment(1));
        verify(commentRepository, times(0)).deleteById(anyInt()); // 댓글이 없으므로 삭제가 호출되지 않음을 검증
    }

    // likeComment 테스트
    @Test
    void testLikeComment_ValidId_IncreasesLikeCount() {
        // 댓글 객체 생성 및 설정
        Comment comment = new Comment();
        comment.setArticleId(1);
        comment.setLikeCount(0);

        // Mock Repository 설정: 특정 ID로 댓글을 찾고, 좋아요 수를 증가시켜 저장하도록 설정
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);

        // 서비스 메서드 호출 및 결과 검증
        Comment likedComment = commentService.likeComment(1);
        assertEquals(1, likedComment.getLikeCount()); // 좋아요 수가 증가했는지 확인
        verify(commentRepository, times(1)).save(comment); // Repository의 save 메서드가 호출되었는지 확인
    }

    @Test
    void testLikeComment_CommentNotFound_ThrowsResourceNotFoundException() {
        // Mock Repository 설정: 특정 ID로 댓글을 찾지 못하는 경우
        when(commentRepository.findById(1)).thenReturn(Optional.empty());

        // 댓글이 없을 때 ResourceNotFoundException이 발생하는지 테스트
        assertThrows(ResourceNotFoundException.class, () -> commentService.likeComment(1));
        verify(commentRepository, times(0)).save(any(Comment.class)); // 댓글이 없으므로 저장이 호출되지 않음을 검증
    }
}

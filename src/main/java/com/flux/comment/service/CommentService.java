package com.flux.comment.service;

import com.flux.comment.model.Comment;
import com.flux.comment.repository.CommentRepository;
import com.flux.global.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private static final int MAX_COMMENT_LENGTH = 500; // 댓글 최대 길이 제한

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // 특정 아티클 ID에 대한 댓글 목록 조회
    @Transactional
    public List<Comment> getCommentsByArticleId(Integer articleId) {
        if (articleId == null || articleId <= 0) {
            throw new IllegalArgumentException("아티클 ID를 찾을 수 없습니다.");
        }
        return commentRepository.findByArticleId(articleId);
    }

    // 새로운 댓글 작성
    @Transactional
    public Comment createComment(Comment comment) {
        validateComment(comment);
        checkForDuplicateComment(comment);

        // Save the comment
        return commentRepository.save(comment);
    }

    // 댓글 수정
    @Transactional
    public Comment updateComment(Integer commentId, Comment updatedComment) {
        validateComment(updatedComment);

        Optional<Comment> existingComment = commentRepository.findById(commentId);
        if (existingComment.isPresent()) {
            Comment comment = existingComment.get();

            // 댓글 작성자와 수정자가 동일한지 확인 (비즈니스 규칙)
            if (!comment.getUserId().equals(updatedComment.getUserId())) {
                throw new SecurityException("본인이 작성한 댓글만 수정 할 수 있습니다.");
            }

            comment.setCommentContents(updatedComment.getCommentContents());
            return commentRepository.save(comment);
        } else {
            throw new IllegalArgumentException("댓글을 찾지 못했습니다.");
        }
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Integer commentId) {
        Optional<Comment> existingComment = commentRepository.findById(commentId);
        if (existingComment.isPresent()) {
            commentRepository.deleteById(commentId);
        } else {
            throw new IllegalArgumentException("댓글을 찾지 못했습니다.");
        }
    }

    // 댓글 유효성 검사
    private void validateComment(Comment comment) {
        if (comment.getArticleId() == null || comment.getArticleId() <= 0) {
            throw new IllegalArgumentException("아티클 ID가 없습니다.");
        }
        if (comment.getUserId() == null || comment.getUserId() <= 0) {
            throw new IllegalArgumentException("유저 ID가 없습니다.");
        }
        if (comment.getCommentContents() == null || comment.getCommentContents().trim().isEmpty()) {
            throw new IllegalArgumentException("댓글이 비어있습니다.");
        }
        if (comment.getCommentContents().length() > MAX_COMMENT_LENGTH) {
            throw new IllegalArgumentException("댓글은 500자를 넘길수 없습니다.");
        }
    }

    // 중복 댓글 방지 로직
    private void checkForDuplicateComment(Comment comment) {
        List<Comment> existingComments = commentRepository.findByArticleId(comment.getArticleId());
        for (Comment existingComment : existingComments) {
            if (existingComment.getUserId().equals(comment.getUserId()) &&
                    existingComment.getCommentContents().equalsIgnoreCase(comment.getCommentContents())) {
                throw new IllegalArgumentException("중복되는 댓글이 존재합니다.");
            }
        }
    }

    // 댓글추천 관련 로직
    @Transactional
    public Comment likeComment(Integer id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        comment.increaseLikeCount();
        return commentRepository.save(comment);
    }
}

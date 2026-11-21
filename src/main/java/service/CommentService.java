package service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import dto.request.CreateOrUpdateCommentRequest;
import dto.response.CommentResponse;
import dto.response.CommentsResponse;
import entity.Ad;
import entity.Comment;
import entity.User;
import mapper.CommentMapper;
import repository.AdRepository;
import repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository, AdRepository adRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
        this.commentMapper = commentMapper;
    }

    @PreAuthorize("permitAll()")
    public CommentsResponse getComments(Integer adId) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        List<Comment> comments = commentRepository.findByAd(ad);
        List<CommentResponse> commentResponses = comments.stream()
                .map(commentMapper::toResponse)
                .toList();

        CommentsResponse response = new CommentsResponse();
        response.setCount((int) commentRepository.countByAd(ad));
        response.setResults(commentResponses);
        return response;
    }

    @PreAuthorize("isAuthenticated()")
    public CommentResponse addComment(Integer adId, CreateOrUpdateCommentRequest request, User author) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setAd(ad);
        comment.setText(request.getText());
        comment.setCreatedAt(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);
        return commentMapper.toResponse(saved);
    }

    @PreAuthorize("isAuthenticated()")
    public void deleteComment(Integer adId, Integer commentId, User author) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getAd().getPk().equals(adId)) {
            throw new RuntimeException("Comment does not belong to this ad");
        }

        if (!comment.getAuthor().getId().equals(author.getId())) {
            throw new IllegalArgumentException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }

    @PreAuthorize("isAuthenticated()")
    public CommentResponse updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentRequest request, User author) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getAd().getPk().equals(adId)) {
            throw new RuntimeException("Comment does not belong to this ad");
        }

        if (!comment.getAuthor().getId().equals(author.getId())) {
            throw new IllegalArgumentException("You can only update your own comments");
        }

        if (request.getText() != null) {
            comment.setText(request.getText());
        }

        Comment saved = commentRepository.save(comment);
        return commentMapper.toResponse(saved);
    }
}

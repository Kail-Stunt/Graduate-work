
package service;

import org.springframework.stereotype.Service;
import dto.request.CreateOrUpdateCommentRequest;
import dto.response.CommentResponse;
import dto.response.CommentsResponse;
import entity.Ad;
import entity.Comment;
import entity.User;
import repository.AdRepository;
import repository.CommentRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;

    public CommentService(CommentRepository commentRepository, AdRepository adRepository) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
    }

    public CommentsResponse getComments(Integer adId) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        List<Comment> comments = commentRepository.findByAd(ad);
        List<CommentResponse> commentResponses = comments.stream()
                .map(this::toResponse)
                .toList();

        CommentsResponse response = new CommentsResponse();
        response.setCount((int) commentRepository.countByAd(ad));
        response.setResults(commentResponses);
        return response;
    }

    public CommentResponse addComment(Integer adId, CreateOrUpdateCommentRequest request, User author) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setAd(ad);
        comment.setText(request.getText());
        comment.setCreatedAt(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);
        return toResponse(saved);
    }

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
        return toResponse(saved);
    }

    private CommentResponse toResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setPk(comment.getPk());
        response.setAuthor(comment.getAuthor().getId());
        response.setAuthorImage(comment.getAuthor().getImage());
        response.setAuthorFirstName(comment.getAuthor().getFirstName());
        response.setCreatedAt(comment.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli());
        response.setText(comment.getText());
        return response;
    }
}

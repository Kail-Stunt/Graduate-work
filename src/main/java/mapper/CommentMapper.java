package mapper;

import dto.response.CommentResponse;
import entity.Comment;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class CommentMapper {

    public CommentResponse toResponse(Comment comment) {
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

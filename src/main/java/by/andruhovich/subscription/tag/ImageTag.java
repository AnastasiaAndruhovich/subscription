package by.andruhovich.subscription.tag;

import Decoder.BASE64Encoder;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.service.PublicationService;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class ImageTag extends TagSupport {
    private int publicationId;

    public void setPublicationId(int id) {
        publicationId = id;
    }

    @Override
    public int doStartTag() throws JspException {
        PublicationService publicationService = new PublicationService();
        try {
            byte[] picture = publicationService.findPictureByPublicationId(publicationId);
            if (picture == null) {
                pageContext.getOut().write("<img src=\"../img/no-image.png\" alt=\"user\" class=\"img-circle img-responsive\">");
            } else {
                BASE64Encoder base64Encoder = new BASE64Encoder();
                String image = "<img src=\"data:image/png;base64," + base64Encoder.encode(picture) + "\"alt=\"user\" class=\"img-circle img-responsive\">";
                pageContext.getOut().write(image);
            }
        } catch (IOException | ServiceTechnicalException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}

package groupware.dispatcher.service.model;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AvatarImage {
    @JsonProperty("imageId")
    private String imageId = null;

    @JsonProperty("imageUrl")
    private String imageUrl = null;

    @JsonProperty("imageWidth")
    private Integer imageWidth = null;

    @JsonProperty("imageHeight")
    private Integer imageHeight = null;

    public AvatarImage imageId(String imageId) {
        this.imageId = imageId;
        return this;
    }

    /**
     * Get imageId
     * @return imageId
     **/
    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public AvatarImage imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    /**
     * Get imageUrl
     * @return imageUrl
     **/
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public AvatarImage imageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
        return this;
    }

    /**
     * Get imageWidth
     * @return imageWidth
     **/
   public Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }

    public AvatarImage imageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
        return this;
    }

    /**
     * Get imageHeight
     * @return imageHeight
     **/
    public Integer getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AvatarImage multimediaDescriptionImage = (AvatarImage) o;
        return  Objects.equals(this.imageId, multimediaDescriptionImage.imageId) &&
                Objects.equals(this.imageUrl, multimediaDescriptionImage.imageUrl) &&
                Objects.equals(this.imageWidth, multimediaDescriptionImage.imageWidth) &&
                Objects.equals(this.imageHeight, multimediaDescriptionImage.imageHeight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageId, imageUrl, imageWidth, imageHeight);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MultimediaDescriptionImage {\n");

        sb.append("    imageId: ").append(toIndentedString(imageId)).append("\n");
        sb.append("    imageUrl: ").append(toIndentedString(imageUrl)).append("\n");
        sb.append("    imageWidth: ").append(toIndentedString(imageWidth)).append("\n");
        sb.append("    imageHeight: ").append(toIndentedString(imageHeight)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

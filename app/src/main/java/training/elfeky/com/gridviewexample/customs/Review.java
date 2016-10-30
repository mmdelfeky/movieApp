package training.elfeky.com.gridviewexample.customs;

/**
 * Created by f on 11/09/2016.
 */
public class Review {
        String author;
        String content;

        public Review(String author, String content) {
            this.author = author;
            this.content = content;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }
}

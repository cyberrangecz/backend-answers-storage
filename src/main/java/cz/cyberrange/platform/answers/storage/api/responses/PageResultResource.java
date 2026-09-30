package cz.cyberrange.platform.answers.storage.api.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collections;
import java.util.List;

/**
 * One page of results together with its paging summary.
 */
@Schema(name = "PageResultResource", description = "One page of results together with its paging summary.")
public class PageResultResource<E> {

    @JsonProperty(required = true)
    private List<E> content;
    @JsonProperty(required = true)
    private Pagination pagination;

    public PageResultResource() {
    }

    /**
     * Builds a page result whose pagination summary stays null.
     */
    public PageResultResource(List<E> content) {
        super();
        this.content = content;
    }

    public PageResultResource(List<E> content, Pagination pageMetadata) {
        super();
        this.content = content;
        this.pagination = pageMetadata;
    }

    /**
     * Returns an unmodifiable view of the content list.
     */
    public List<E> getContent() {
        return Collections.unmodifiableList(content);
    }

    public void setContent(List<E> content) {
        this.content = content;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    @Override
    public String toString() {
        return "PageResultResource{" +
                "content=" + content +
                ", pagination=" + pagination +
                '}';
    }

    public static class Pagination {

        @Schema(description = "Index of this page within the whole result.", example = "1")
        @JsonProperty(required = true)
        private int number;
        @Schema(description = "How many items this page holds.", example = "20")
        @JsonProperty(required = true, value = "number_of_elements")
        private int numberOfElements;
        @Schema(description = "Largest number of items a page may hold.", example = "20")
        @JsonProperty(required = true)
        private int size;
        @Schema(description = "How many items all pages hold together.", example = "100")
        @JsonProperty(required = true, value = "total_elements")
        private long totalElements;
        @Schema(description = "Total number of pages.", example = "5")
        @JsonProperty(required = true, value = "total_pages")
        private int totalPages;

        public Pagination() {
        }

        public Pagination(int number, int numberOfElements, int size, long totalElements, int totalPages) {
            super();
            this.number = number;
            this.numberOfElements = numberOfElements;
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getNumberOfElements() {
            return numberOfElements;
        }

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public long getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(long totalElements) {
            this.totalElements = totalElements;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }


        @Override
        public String toString() {
            return "Pagination{" +
                    "number=" + number +
                    ", numberOfElements=" + numberOfElements +
                    ", size=" + size +
                    ", totalElements=" + totalElements +
                    ", totalPages=" + totalPages +
                    '}';
        }
    }
}

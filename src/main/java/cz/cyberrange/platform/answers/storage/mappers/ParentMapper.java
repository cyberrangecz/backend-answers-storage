package cz.cyberrange.platform.answers.storage.mappers;

import cz.cyberrange.platform.answers.storage.api.reponses.PageResultResource;
import org.springframework.data.domain.Page;

/**
 * Common base for the mappers, supplying the shared pagination summary conversion.
 */
public interface ParentMapper {

    /**
     * Builds a pagination summary from a page. The page's content is ignored.
     *
     * @param objects the page to summarize
     * @return the pagination summary
     */
    default PageResultResource.Pagination createPagination(Page<?> objects) {
        PageResultResource.Pagination pageMetadata = new PageResultResource.Pagination();
        pageMetadata.setNumber(objects.getNumber());
        pageMetadata.setNumberOfElements(objects.getNumberOfElements());
        pageMetadata.setSize(objects.getSize());
        pageMetadata.setTotalElements(objects.getTotalElements());
        pageMetadata.setTotalPages(objects.getTotalPages());
        return pageMetadata;
    }
}

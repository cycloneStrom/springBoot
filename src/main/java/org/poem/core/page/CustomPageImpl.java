package org.poem.core.page;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by poem on 2016/4/15.
 * 分页
 */
public class CustomPageImpl<T> implements Page<T>,Serializable {
    private long total;
    private List<T> content;
    private Pageable pageable;

    public CustomPageImpl(){

    }

    public CustomPageImpl(List<T> content, Pageable pageable, long total) {
        this.content=content;
        this.pageable=pageable;
        this.total=total;
    }

    public CustomPageImpl(List<T> content) {
        this.content=content;
    }

    public int getTotalPages() {
        return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Page#getTotalElements()
     */
    public long getTotalElements() {
        return total;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Page#hasPreviousPage()
     */
    public boolean hasPreviousPage() {
        return hasPrevious();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Page#isFirstPage()
     */
    public boolean isFirstPage() {
        return isFirst();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#hasNext()
     */
    @Override
    public boolean hasNext() {
        return hasNextPage();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Page#hasNextPage()
     */
    public boolean hasNextPage() {
        return getNumber() + 1 < getTotalPages();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#isLast()
     */
    @Override
    public boolean isLast() {
        return isLastPage();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Page#isLastPage()
     */
    public boolean isLastPage() {
        return !hasNextPage();
    }

    public int getNumber() {
        return pageable == null ? 0 : pageable.getPageNumber();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#getSize()
     */
    public int getSize() {
        return pageable == null ? 0 : pageable.getPageSize();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#getNumberOfElements()
     */
    public int getNumberOfElements() {
        return content.size();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#hasPrevious()
     */
    public boolean hasPrevious() {
        return getNumber() > 0;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#isFirst()
     */
    public boolean isFirst() {
        return !hasPrevious();
    }



    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#nextPageable()
     */
    public Pageable nextPageable() {
        return hasNext() ? pageable.next() : null;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#previousPageable()
     */
    public Pageable previousPageable() {

        if (hasPrevious()) {
            return pageable.previousOrFirst();
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#hasContent()
     */
    public boolean hasContent() {
        return !content.isEmpty();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#getContent()
     */
    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#getSort()
     */
    public Sort getSort() {
        return pageable == null ? null : pageable.getSort();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<T> iterator() {
        return content.iterator();
    }


    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String contentType = "UNKNOWN";
        List<T> content = getContent();

        if (content.size() > 0) {
            contentType = content.get(0).getClass().getName();
        }
        return String.format("Page %s of %d containing %s instances", getNumber(), getTotalPages(), contentType);
    }
}

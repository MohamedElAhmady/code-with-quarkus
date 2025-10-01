package org.stibodx.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Generic wrapper for paginated results.
 * Contains the users along with pagination metadata.
 */
public class PagedResult<T> {
    
    @JsonProperty("users")
    private List<T> users;
    
    @JsonProperty("pagination")
    private PaginationInfo pagination;
    
    public PagedResult() {}
    
    public PagedResult(List<T> users, int page, int size, long totalElements) {
        this.users = users;
        this.pagination = new PaginationInfo(page, size, totalElements);
    }
    
    public List<T> getUsers() {
        return users;
    }
    
    public void setUsers(List<T> users) {
        this.users = users;
    }
    
    public PaginationInfo getPagination() {
        return pagination;
    }
    
    public void setPagination(PaginationInfo pagination) {
        this.pagination = pagination;
    }
    
    /**
     * Pagination metadata information.
     */
    public static class PaginationInfo {
        
        @JsonProperty("page")
        private int page;
        
        @JsonProperty("size")
        private int size;
        
        @JsonProperty("totalElements")
        private long totalElements;
        
        @JsonProperty("totalPages")
        private int totalPages;
        
        @JsonProperty("hasNext")
        private boolean hasNext;
        
        @JsonProperty("hasPrevious")
        private boolean hasPrevious;
        
        public PaginationInfo() {}
        
        public PaginationInfo(int page, int size, long totalElements) {
            this.page = page;
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = (int) Math.ceil((double) totalElements / size);
            this.hasNext = page < totalPages - 1;
            this.hasPrevious = page > 0;
        }
        
        // Getters and setters
        public int getPage() {
            return page;
        }
        
        public void setPage(int page) {
            this.page = page;
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
        
        public boolean isHasNext() {
            return hasNext;
        }
        
        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }
        
        public boolean isHasPrevious() {
            return hasPrevious;
        }
        
        public void setHasPrevious(boolean hasPrevious) {
            this.hasPrevious = hasPrevious;
        }
    }
}
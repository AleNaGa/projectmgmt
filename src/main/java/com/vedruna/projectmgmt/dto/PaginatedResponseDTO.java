package com.vedruna.projectmgmt.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaginatedResponseDTO<T> {
        private List<T> content; 
        private int currentPage;
        private int totalPages;  
        private long totalItems; 
        private int pageSize;  
        
        
        public PaginatedResponseDTO(Page<T> page) {
            this.content = page.getContent();
            this.currentPage = page.getNumber();
            this.totalPages = page.getTotalPages();
            this.totalItems = page.getTotalElements();
            this.pageSize = page.getSize();
        }
        
}

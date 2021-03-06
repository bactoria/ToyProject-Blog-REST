package com.bactoria.toy1.domain.category;

import com.bactoria.toy1.domain.category.dto.CategoryModifyRequestDto;
import com.bactoria.toy1.domain.category.dto.CategoryResponseDto;
import com.bactoria.toy1.domain.category.dto.CategorySaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<Category> resCategory() {
        return categoryRepository.findAll();
    }

    public CategoryResponseDto saveCategory(CategorySaveRequestDto requestDto) {
        Category category = modelMapper.map(requestDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryResponseDto.class);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category resCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(new Category());
    }

    public void modifyCategory(Long id, CategoryModifyRequestDto requestDto) {
        Category savedCategory = categoryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        modelMapper.map(requestDto, savedCategory);
        categoryRepository.save(savedCategory);
    }
}

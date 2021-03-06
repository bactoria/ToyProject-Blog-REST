package com.bactoria.toy1.domain.category;

import com.bactoria.toy1.domain.category.dto.CategoryModifyRequestDto;
import com.bactoria.toy1.domain.category.dto.CategoryResponseDto;
import com.bactoria.toy1.domain.category.dto.CategorySaveRequestDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    private CategoryRepository categoryRepositoryMock;
    private CategoryService categoryService;
    private ModelMapper modelMapper;

    @Before
    public void setup() {
        this.categoryRepositoryMock = Mockito.mock(CategoryRepository.class);
        this.modelMapper = new ModelMapper();
        this.categoryService = new CategoryService(categoryRepositoryMock, modelMapper);
    }

    @Test
    public void Mocking이_정상적으로_만들어졌다() {
        assertThat(categoryRepositoryMock).isNotNull();
        assertThat(categoryService).isNotNull();
    }

    @Test
    public void test001_모든_카테고리를_불러온다() {

        //given
        String firstCategoryName = "카테고리1";
        String secondCategoryName = "카테고리2";
        given(categoryRepositoryMock.findAll()).willReturn(Arrays.asList(
                Category.builder().name(firstCategoryName).build(),
                Category.builder().name(secondCategoryName).build()
        ));

        //when
        List<Category> categoryList = categoryService.resCategory();

        //then
        verify(categoryRepositoryMock, times(1)).findAll();
        assertThat(categoryList).hasSize(2);
        assertThat(categoryList.get(0).getName()).isEqualTo(firstCategoryName);
        assertThat(categoryList.get(1).getName()).isEqualTo(secondCategoryName);
    }

    @Test
    public void test002_특정_카테고리를_불러온다() {

        //given
        String categoryName = "카테고리1";
        given(categoryRepositoryMock.findById(anyLong())).willReturn(
                java.util.Optional.ofNullable(Category.builder().name(categoryName).build())
        );

        //when
        Category category = categoryService.resCategoryById(1L);

        //then
        verify(categoryRepositoryMock, times(1)).findById(1L);
        assertThat(category.getName()).isEqualTo(categoryName);
    }

    @Test
    public void 카테고리를_정상적으로_저장한다() {

        // given
        final String CATEGORY_NAME = "카테고리";

        CategorySaveRequestDto requestDto = CategorySaveRequestDto.builder()
                .name(CATEGORY_NAME)
                .build();

        Category category = Category.builder()
                .id(1L)
                .name(CATEGORY_NAME)
                .build();

        given(categoryRepositoryMock.save(any(Category.class))).willReturn(category);

        // when
        CategoryResponseDto result = categoryService.saveCategory(requestDto);

        // then
        assertThat(result.getName()).isEqualTo(CATEGORY_NAME);
    }

    @Test
    public void 특정_카테고리를_정상적으로_삭제한다() {
        // given
        final Long CATEGORY_ID = 1L;

        // when
        categoryService.deleteCategory(CATEGORY_ID);

        // then
        verify(categoryRepositoryMock).deleteById(CATEGORY_ID);
    }

    @Test
    public void 특정_카테고리를_정상적으로_수정한다() {
        // given
        final Long CATEGORY_ID = 1L;
        final String CATEGORY_NAME_MOD = "카테고리_수정";
        CategoryModifyRequestDto dto = CategoryModifyRequestDto.builder()
                .name(CATEGORY_NAME_MOD)
                .build();

        Category category = Category.builder().name(CATEGORY_NAME_MOD).build();
        given(categoryRepositoryMock.findById(CATEGORY_ID)).willReturn(Optional.of(category));

        // when
        categoryService.modifyCategory(CATEGORY_ID, dto);

        // then
        verify(categoryRepositoryMock, times(1)).save(any(Category.class));
    }

}
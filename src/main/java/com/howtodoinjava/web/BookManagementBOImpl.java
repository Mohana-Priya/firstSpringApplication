package com.howtodoinjava.web;

import java.util.List;

import com.howtodoinjava.dto.Author;
import com.howtodoinjava.dto.Book;
import com.howtodoinjava.dto.Books;
import com.howtodoinjava.dto.Category;

public class BookManagementBOImpl implements BookManagementBO {

	private BookManagementDao bookMGMTDao = new BookManagementDaoImpl();

	@Override
	public Books getBookById(Integer id) {		
		return bookMGMTDao.getBookById(id);
	}

	@Override
	public List<Book> getAllBooks(Integer authorId,Integer categoryId) {
		if(authorId == null && categoryId == null){
			return bookMGMTDao.getAllBooks();
		} else if(authorId != null && categoryId == null) {
			return bookMGMTDao.getAllBooksOfAnAuthor(authorId);
		} else if(categoryId != null && authorId == null) {
			return bookMGMTDao.getAllBooksOfACategory(categoryId);
		}
		return null;
	}

	@Override
	public void postBookById(Books bookObj) {		
		bookMGMTDao.postBookById(bookObj);
	}
	
	@Override
	public void editBookById(Books bookObj) {
		bookMGMTDao.editBookById(bookObj);		
	}

	@Override
	public void deleteBookById(Integer id) {
		bookMGMTDao.deleteBookById(id);		
	}

	@Override
	public Author getAuthorById(Integer id) {		
		return bookMGMTDao.getAuthorById(id);
	}

	@Override
	public List<Author> getAllAuthors() {		
		return bookMGMTDao.getAllAuthors();
	}

	@Override
	public void postAuthorById(Author authorObj) {		
		bookMGMTDao.postAuthorById(authorObj);
	}

	@Override
	public void editAuthorById(Author authorObj) {
		bookMGMTDao.editAuthorById(authorObj);
		
	}

	@Override
	public void deleteAuthorById(Integer id) {
		bookMGMTDao.deleteAuthorById(id);
		
	}

	@Override
	public Category getCategoryById(Integer id) {
		
		return bookMGMTDao.getCategoryById(id);
	}

	@Override
	public List<Category> getAllCategories() {
		
		return bookMGMTDao.getAllCategories();
	}

	@Override
	public void postCategoryById(Category categoryObj) {
		
		bookMGMTDao.postCategoryById(categoryObj);
	}

	@Override
	public void editCategoryById(Category categoryObj) {
		bookMGMTDao.editCategoryById(categoryObj);
		
	}

	@Override
	public void deleteCategoryById(Integer id) {
		bookMGMTDao.deleteCategoryById(id);		
	}	
}

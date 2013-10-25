package com.howtodoinjava.web;

import java.util.List;
import com.howtodoinjava.dto.Author;
import com.howtodoinjava.dto.Book;
import com.howtodoinjava.dto.Books;
import com.howtodoinjava.dto.Category;

public interface BookManagementDao {

	//Methods for Book
	Books getBookById(Integer id);
	List<Book> getAllBooks();
	void postBookById(Books bookObj);
	void editBookById(Books bookObj);
	void deleteBookById(Integer id);
	
	//Methods for Author
	Author getAuthorById(Integer id);
	List<Author> getAllAuthors();
	void postAuthorById(Author authorObj);
	void editAuthorById(Author authorObj);
	void deleteAuthorById(Integer id);
	
	//Methods for Category
	Category getCategoryById(Integer id);
	List<Category> getAllCategories();
	void postCategoryById(Category CategoryObj);
	void editCategoryById(Category CategoryObj);
	void deleteCategoryById(Integer id);
	
	List<Book> getAllBooksOfAnAuthor(Integer authorId);
	List<Book> getAllBooksOfACategory(Integer categoryId);

}

package com.howtodoinjava.web;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.howtodoinjava.dto.Book;
import com.howtodoinjava.dto.Books;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import com.google.gson.Gson;

@Controller
@RequestMapping("/books")
public class BooksController 
{
	private BookManagementBO bookMGMTBO = new BookManagementBOImpl();
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}", headers="Accept=*/*")
	public @ResponseBody String getBookById(@PathVariable Integer id) 
	{
		Books book = bookMGMTBO.getBookById(id);
		System.out.println(book);		
		Gson gson = new Gson();		 
		// convert java object to JSON format,
		// and returned as JSON formatted string
		String json = gson.toJson(book);
		return json;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/author/{id}", headers="Accept=*/*")
	public @ResponseBody List<Book> getBookByAuthorId(@PathVariable Integer id) 
	{
		List<Book> bookList = new ArrayList<Book>();
		bookList = bookMGMTBO.getAllBooks(id,null);		
		return bookList;		
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/category/{id}", headers="Accept=*/*")
	public @ResponseBody List<Book> getBooksByCategoryId(@PathVariable Integer id) 
	{
		List<Book> bookList = new ArrayList<Book>();
		bookList = bookMGMTBO.getAllBooks(null,id);		
		return bookList;		
	}
	
	@RequestMapping(method = RequestMethod.GET,  headers="Accept=*/*")
	public @ResponseBody List<Book> getAllBooks() 
	{
		List<Book> bookList = new ArrayList<Book>();
		bookList = bookMGMTBO.getAllBooks(null,null);		
		return bookList;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/{book}", headers="Accept=*/*")
	public @ResponseBody String postBookById(@PathVariable String book) 
	{
		ObjectMapper mapper = new ObjectMapper();    	
    	try {
    		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    		Books bookObj = mapper.readValue(book, Books.class);
    		bookMGMTBO.postBookById(bookObj); 
		}  catch (Exception e) {
			e.printStackTrace();
			return "Error in posting the book";
		}		
		return "Books posted successfully";
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/{book}", headers="Accept=*/*")
	public @ResponseBody String editBookById(@PathVariable String book) 
	{
		ObjectMapper mapper = new ObjectMapper();    	
		try {
			mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Books bookObj = mapper.readValue(book, Books.class);
			bookMGMTBO.editBookById(bookObj); 
		}  catch (Exception e) {
			e.printStackTrace();
			return "Error in editing book";
		}		
		return "edited book successfully";
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/{id}", headers="Accept=*/*")
	public @ResponseBody String deleteBookById(@PathVariable Integer id) 
	{		
		try {
			bookMGMTBO.deleteBookById(id);
		} catch (Exception e) {			
			e.printStackTrace();
			return "Error in deleting book";
		}
		return "Book with id:"+id+" and its mappings in bookauthormapping table are deleted successfully if exists";
	}	
}
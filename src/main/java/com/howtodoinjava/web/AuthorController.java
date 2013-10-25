package com.howtodoinjava.web;

import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.howtodoinjava.dto.Author;

@Controller
@RequestMapping("/authors")
public class AuthorController 
{
	private BookManagementBO bookMGMTBO = new BookManagementBOImpl();
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}", headers="Accept=*/*")
	public @ResponseBody Author getAuthorById(@PathVariable Integer id) 
	{
		Author author = bookMGMTBO.getAuthorById(id);
		return author;
	}
	
	@RequestMapping(method = RequestMethod.GET,  headers="Accept=*/*")
	public @ResponseBody List<Author> getAllAuthors() 
	{
		List<Author> authorList= bookMGMTBO.getAllAuthors();		
		return authorList;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/{author}", headers="Accept=*/*")
	public @ResponseBody String postAuthorById(@PathVariable String author) 
	{
		ObjectMapper mapper = new ObjectMapper();    	
    	try {    		
    		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    		Author authorObj = mapper.readValue(author, Author.class);
    		bookMGMTBO.postAuthorById(authorObj); 
		}  catch (Exception e) {
			e.printStackTrace();
			return "Error in saving author";
		}		
		return "Author posted successfully";
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/{author}", headers="Accept=*/*")
	public @ResponseBody String editAuthorById(@PathVariable String author) 
	{
		ObjectMapper mapper = new ObjectMapper();    	
    	try {
    		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    		Author authorObj = mapper.readValue(author, Author.class);
    		bookMGMTBO.editAuthorById(authorObj); 
		}  catch (Exception e) {
			e.printStackTrace();
			return "Error in editing author";
		}		
		return "edited author successfully";
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/{id}", headers="Accept=*/*")
	public @ResponseBody String deleteAuthorById(@PathVariable Integer id) 
	{
		try {
			bookMGMTBO.deleteAuthorById(id);
		} catch (Exception e) {			
			e.printStackTrace();
			return "Error in deleting author";
		}
		return "Author with id:"+id+" deleted successfully if exists";
	}	
}
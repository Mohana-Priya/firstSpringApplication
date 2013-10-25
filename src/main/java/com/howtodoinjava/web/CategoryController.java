package com.howtodoinjava.web;

import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.howtodoinjava.dto.Category;

@Controller
@RequestMapping("/category")
public class CategoryController 
{
	private BookManagementBO bookMGMTBO = new BookManagementBOImpl();
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}", headers="Accept=*/*")
	public @ResponseBody Category getCategoryById(@PathVariable Integer id) 
	{		
		Category category = bookMGMTBO.getCategoryById(id);
		return category;
	}
	
	@RequestMapping(method = RequestMethod.GET,  headers="Accept=*/*")
	public @ResponseBody List<Category> getAllCategories() 
	{
		List<Category> categoryList= bookMGMTBO.getAllCategories();		
		return categoryList;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/{categoryJSON}", headers="Accept=*/*")
	public @ResponseBody String postCategoryById(@PathVariable String categoryJSON) 
	{
		ObjectMapper mapper = new ObjectMapper();    	
    	try {    		
    		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    		Category categoryObj = mapper.readValue(categoryJSON, Category.class);
    		bookMGMTBO.postCategoryById(categoryObj); 
		}  catch (Exception e) {
			e.printStackTrace();
			return "Error in saving category";
		}		
		return "Category posted successfully";
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/{categoryJSON}", headers="Accept=*/*")
	public @ResponseBody String editCategoryById(@PathVariable String categoryJSON) 
	{		
		ObjectMapper mapper = new ObjectMapper();    	
    	try {
    		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    		Category categoryObj = mapper.readValue(categoryJSON, Category.class);
    		bookMGMTBO.editCategoryById(categoryObj); 
		}  catch (Exception e) {
			e.printStackTrace();
			return "Error in editing category";
		}		
		return "edited category successfully";
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/{id}", headers="Accept=*/*")
	public @ResponseBody String deleteCategoryById(@PathVariable Integer id) 
	{
		try {
			bookMGMTBO.deleteCategoryById(id);
		} catch (Exception e) {			
			e.printStackTrace();
			return "Error in deleting category";
		}
		return "category with id:"+id+" deleted successfully if exists";		
	}	
}
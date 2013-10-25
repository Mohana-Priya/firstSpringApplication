package com.howtodoinjava.web;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.howtodoinjava.dto.Author;
import com.howtodoinjava.dto.Book;
import com.howtodoinjava.dto.Books;
import com.howtodoinjava.dto.Category;

public class BookManagementDaoImpl implements BookManagementDao {

	@Override
	public Books getBookById(Integer id) {
		Books book = null;
		try {
			DataCon.establishConnection();

			ResultSet rs=DataCon.st.executeQuery("select * from books where id="+id);
			if(rs.first()) {
				book = new Books();
				book.setId(rs.getInt(1));
				book.setTitle(rs.getString(2));
				ResultSet rs1 = DataCon.st.executeQuery("select * from category where id ="+rs.getInt(3));
				if(rs1.first()){
					Category category = new Category();
					category.setId(rs1.getInt(1));
					category.setName(rs1.getString(2));
					book.setCategory(category);
				}
				ResultSet rs2 = DataCon.st.executeQuery("select authorid from bookauthormapping where bookid ="+id);
				List<Author> authorList = new ArrayList<Author>();
				while(rs2.next()){
					Author author = getAuthorById(rs2.getInt(1));
					authorList.add(author);
				}
				book.setAuthor(authorList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getting the book with id:"+id);
		} finally {
			DataCon.closeConnection();
		}
		return book;
	}

	@Override
	public List<Book> getAllBooks() {
		List<Book> bookList = new ArrayList<Book>();
		try {
			DataCon.establishConnection();
			ResultSet rs=DataCon.st.executeQuery("select * from books");
			while(rs.next()) {
				Book book = new Book();
				book.setId(rs.getInt(1));
				book.setTitle(rs.getString(2));
				bookList.add(book);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getting all books");
		} finally {
			DataCon.closeConnection();
		}

		return bookList;
	}

	@Override
	public void postBookById(Books bookObj) {
		try {
			DataCon.establishConnection();

			ResultSet rs1 = DataCon.st.executeQuery("select max(id) from books");
			if(rs1.last())
				bookObj.setId(rs1.getInt(1)+1);
			else
				bookObj.setId(1);

			PreparedStatement ps = DataCon.con.prepareStatement("insert into books values(?,?,?)");
			ps.setInt(1, bookObj.getId());
			ps.setString(2, bookObj.getTitle());

			rs1 = DataCon.st.executeQuery("select * from category where id="+bookObj.getCategory().getId());			
			if(!rs1.next()) {//create new category if the category does not exist				
				Category categoryObj = bookObj.getCategory();
				rs1 = DataCon.st.executeQuery("select max(id) from category");
				if(rs1.last()){
					categoryObj.setId(rs1.getInt(1)+1);
					ps.setInt(3, rs1.getInt(1)+1);
				} else {
					categoryObj.setId(1);
					ps.setInt(3, 1);
				}
				PreparedStatement ps1 = DataCon.con.prepareStatement("insert into category values(?,?)");
				ps1.setInt(1, categoryObj.getId());
				ps1.setString(2, categoryObj.getName());
				ps1.execute();				
			} else {
				ps.setInt(3, bookObj.getCategory().getId());
			}
			
			ps.execute();			
			
			for(Author author : bookObj.getAuthor()){
				rs1 = DataCon.st.executeQuery("select * from author where id="+author.getId());
				if(!rs1.next()) {					
					rs1 = DataCon.st.executeQuery("select max(id) from author");
					if(rs1.last())
						author.setId(rs1.getInt(1)+1);
					else
						author.setId(1);
					
					ps = DataCon.con.prepareStatement("insert into author values(?,?,?,?)");
					ps.setInt(1, author.getId());
					ps.setString(2, author.getName());
					ps.setString(3, author.getLastName());
					ps.setDate(4, author.getBirthday());
					ps.execute();										
				}
				PreparedStatement ps2 = DataCon.con.prepareStatement("insert into bookauthormapping values(?,?)");
				ps2.setInt(1, bookObj.getId());
				ps2.setInt(2, author.getId());
				ps2.execute();
			}
			
			DataCon.con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in creating new book");
		} finally {
			DataCon.closeConnection();
		}
	}
	
	@Override
	public void editBookById(Books bookObj) {
		try {
			DataCon.establishConnection();
			PreparedStatement ps = DataCon.con.prepareStatement("update books set title=?, categoryid=? where id=?");
			ps.setInt(3, bookObj.getId());
			ps.setString(1, bookObj.getTitle());
			
			ResultSet rs1 = DataCon.st.executeQuery("select * from category where id="+bookObj.getCategory().getId());			
			if(!rs1.next()) {//create new category if the category does not exist				
				Category categoryObj = bookObj.getCategory();
				rs1 = DataCon.st.executeQuery("select max(id) from category");
				if(rs1.last()){
					categoryObj.setId(rs1.getInt(1)+1);
					ps.setInt(2, rs1.getInt(1)+1);
				} else {
					categoryObj.setId(1);
					ps.setInt(2, 1);
				}
				PreparedStatement ps1 = DataCon.con.prepareStatement("insert into category values(?,?)");
				ps1.setInt(1, categoryObj.getId());
				ps1.setString(2, categoryObj.getName());
				ps1.execute();				
			} else {
				ps.setInt(2, bookObj.getCategory().getId());
			}
			
			ps.execute();			

			//delete the existing mappings for the book and author and create new mappings passed in request			
			PreparedStatement ps1 = DataCon.con.prepareStatement("delete from bookauthormapping where bookid=?");
			ps1.setInt(1,bookObj.getId());
			ps1.execute();
			
			for(Author author : bookObj.getAuthor()){
				rs1 = DataCon.st.executeQuery("select * from author where id="+author.getId());
				if(!rs1.next()) {					
					rs1 = DataCon.st.executeQuery("select max(id) from author");
					if(rs1.last())
						author.setId(rs1.getInt(1)+1);
					else
						author.setId(1);
					
					ps = DataCon.con.prepareStatement("insert into author values(?,?,?,?)");
					ps.setInt(1, author.getId());
					ps.setString(2, author.getName());
					ps.setString(3, author.getLastName());
					ps.setDate(4, author.getBirthday());
					ps.execute();
				}
				PreparedStatement ps2 = DataCon.con.prepareStatement("insert into bookauthormapping values(?,?)");
				ps2.setInt(1, bookObj.getId());
				ps2.setInt(2, author.getId());
				ps2.execute();				
			}
			DataCon.con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in Updating book");
		} finally {
			DataCon.closeConnection();
		}
	}

	@Override
	public void deleteBookById(Integer id) {
		try{
			DataCon.establishConnection();
			PreparedStatement pst=DataCon.con.prepareStatement("delete from books where id="+id);
			pst.execute();
			
			// Delete the mappings corresponding to the book when deleting book
			PreparedStatement pst1=DataCon.con.prepareStatement("delete from bookauthormapping where bookid="+id);
			pst1.execute();
			DataCon.con.commit();
		} catch(Exception e) {
			throw new RuntimeException("Error in deleting book with id:"+id);
		} finally {
			DataCon.closeConnection();
		}
	}

	@Override
	public Author getAuthorById(Integer id) {

		Author author = null;
		try {
			DataCon.establishConnection();
			ResultSet rs=DataCon.st.executeQuery("select * from author where id="+id);
			if(rs.first()) {
				author = new Author();
				author.setId(rs.getInt(1));
				author.setName(rs.getString(2));
				author.setLastName(rs.getString(3));
				author.setBirthday(rs.getDate(4));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getting author with id:"+id);
		} finally {
			DataCon.closeConnection();
		}
		return author;
	}

	@Override
	public List<Author> getAllAuthors() {
		List<Author> authorList = new ArrayList<Author>();
		try {
			DataCon.establishConnection();
			ResultSet rs=DataCon.st.executeQuery("select * from author order by upper(name)");
			while(rs.next()) {
				Author author = new Author();
				author.setId(rs.getInt(1));
				author.setName(rs.getString(2));
				author.setLastName(rs.getString(3));
				author.setBirthday(rs.getDate(4));
				authorList.add(author);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getting all the authors");
		} finally {
			DataCon.closeConnection();
		}
		return authorList;
	}

	@Override
	public void postAuthorById(Author authorObj) {

		try {
			DataCon.establishConnection();
			ResultSet rs1 = DataCon.st.executeQuery("select max(id) from author");
			if(rs1.last())
				authorObj.setId(rs1.getInt(1)+1);
			else
				authorObj.setId(1);
			System.out.println(authorObj.getId());
			PreparedStatement ps = DataCon.con.prepareStatement("insert into author values(?,?,?,?)");
			ps.setInt(1, authorObj.getId());
			ps.setString(2, authorObj.getName());
			ps.setString(3, authorObj.getLastName());
			ps.setDate(4, authorObj.getBirthday());
			ps.execute();
			DataCon.con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in creating new author");
		} finally {
			DataCon.closeConnection();
		}
	}

	@Override
	public void editAuthorById(Author authorObj) {
		try {
			DataCon.establishConnection();
			PreparedStatement ps = DataCon.con.prepareStatement("update author set name=?, lastname=?, birthday=? where id=?");
			ps.setInt(4, authorObj.getId());
			ps.setString(1, authorObj.getName());
			ps.setString(2, authorObj.getLastName());
			ps.setDate(3, authorObj.getBirthday());
			ps.execute();
			DataCon.con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in Updating author");
		} finally {
			DataCon.closeConnection();
		}
	}

	@Override
	public void deleteAuthorById(Integer id) {
		try{
			DataCon.establishConnection();
			PreparedStatement pst=DataCon.con.prepareStatement("delete from author where id="+id);
			pst.execute();
			
			// Delete the mappings corresponding to the author when deleting author
			PreparedStatement pst1=DataCon.con.prepareStatement("delete from bookauthormapping where authorid="+id);
			pst1.execute();
			DataCon.con.commit();
		} catch(Exception e) {
			throw new RuntimeException("Error in deleting author");
		} finally {
			DataCon.closeConnection();
		}
	}

	@Override
	public Category getCategoryById(Integer id) {
		Category category = null;
		try {
			DataCon.establishConnection();
			ResultSet rs=DataCon.st.executeQuery("select * from category where id="+id);
			if(rs.first()) {
				category = new Category();
				category.setId(rs.getInt(1));
				category.setName(rs.getString(2));
			}
			DataCon.st.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getting category by id" + id);
		} finally {
			DataCon.closeConnection();
		}
		return category;
	}

	@Override
	public List<Category> getAllCategories() {
		List<Category> categoryList = new ArrayList<Category>();
		try {
			DataCon.establishConnection();
			ResultSet rs=DataCon.st.executeQuery("select * from category");
			while(rs.next()) {
				Category category = new Category();
				category.setId(rs.getInt(1));
				category.setName(rs.getString(2));
				categoryList.add(category);
			}
			DataCon.st.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getting all categories");
		} finally {
			DataCon.closeConnection();
		}
		return categoryList;
	}

	@Override
	public void postCategoryById(Category categoryObj) {
		try {
			DataCon.establishConnection();
			ResultSet rs1 = DataCon.st.executeQuery("select max(id) from category");
			if(rs1.last())
				categoryObj.setId(rs1.getInt(1)+1);
			else
				categoryObj.setId(1);

			PreparedStatement ps = DataCon.con.prepareStatement("insert into category values(?,?)");
			ps.setInt(1, categoryObj.getId());
			ps.setString(2, categoryObj.getName());
			ps.execute();
			DataCon.con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in creating new category");
		} finally {
			DataCon.closeConnection();
		}
	}

	@Override
	public void editCategoryById(Category categoryObj) {
		try {
			DataCon.establishConnection();
			PreparedStatement ps = DataCon.con.prepareStatement("update category set name=? where id=?");
			ps.setInt(2, categoryObj.getId());
			ps.setString(1, categoryObj.getName());
			ps.execute();
			DataCon.con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in Updating category");
		} finally {
			DataCon.closeConnection();
		}
	}

	@Override
	public void deleteCategoryById(Integer id) {
		try{
			DataCon.establishConnection();
			PreparedStatement pst=DataCon.con.prepareStatement("delete from category where id="+id);
			pst.execute();
			DataCon.con.commit();
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error in deleting category");
		} finally {
			DataCon.closeConnection();
		}
	}

	@Override
	public List<Book> getAllBooksOfAnAuthor(Integer authorId) {
		List<Book> bookList = new ArrayList<Book>();
		try {
			DataCon.establishConnection();
			ResultSet rs=DataCon.st.executeQuery("select * from books where id in(select bookid from bookauthormapping where authorid="+authorId+")");
			while(rs.next()) {
				Book book = new Book();
				book.setId(rs.getInt(1));
				book.setTitle(rs.getString(2));
				bookList.add(book);
			}
			DataCon.st.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getting all books for author:"+authorId);
		} finally {
			DataCon.closeConnection();
		}

		return bookList;
	}

	@Override
	public List<Book> getAllBooksOfACategory(Integer categoryId) {
		List<Book> bookList = new ArrayList<Book>();
		try {
			DataCon.establishConnection();
			ResultSet rs=DataCon.st.executeQuery("select * from books where categoryId="+categoryId);
			while(rs.next()) {
				Book book = new Book();
				book.setId(rs.getInt(1));
				book.setTitle(rs.getString(2));
				bookList.add(book);
			}
			DataCon.st.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getting all books for Category:"+categoryId);
		} finally {
			DataCon.closeConnection();
		}
		return bookList;
	}
}
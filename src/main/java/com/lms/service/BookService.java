package com.lms.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lms.exception.ResourceNotFoundException;
import com.lms.model.Author;
import com.lms.model.Book;
import com.lms.model.Genre;
import com.lms.repository.BookRepository;

@Service
public class BookService
{
	@Autowired
	private BookRepository bookrepo;
	
	@Autowired
	private AuthorService as;
	
	@Autowired
	private GenreService gs;
	
	private int rownum,cellnum;
	
	private XSSFWorkbook workbook;
	
	private XSSFSheet sheet;
	
	private HttpHeaders header;
	
	public ResponseEntity<Book> addBook(Book book)
	{
		Author a=book.getAuthor();
		Genre g=book.getGenre();
		a.setAuthorId(as.getAuthorId(a.getAuthorName()));
		g.setGenreId(gs.getGenreId(g.getGenreName()));
		book.setAuthor(a);
		book.setGenre(g);
		bookrepo.save(book);
		return ResponseEntity.ok().body(book);
	}
	
	public ResponseEntity<Book> updateBook(Integer bookId,Book bnew) throws ResourceNotFoundException
	{

		Book b=bookrepo.findById(bookId).orElseThrow(()->new ResourceNotFoundException("Book not found for this id "+bookId));
		Author a=new Author();
		Genre g=new Genre();
		a.setAuthorId(as.getAuthorId(bnew.getAuthor().getAuthorName()));
		a.setAuthorName(as.getAuthorname(a.getAuthorId()));
		g.setGenreId(gs.getGenreId(bnew.getGenre().getGenreName()));
		g.setGenreName(gs.getGenrename(g.getGenreId()));
		b.setAuthor(a);
		b.setGenre(g);
		b.setDate(bnew.getDate());
		b.setQty(bnew.getQty());
		b.setTitle(bnew.getTitle());
		b.setUrl(bnew.getUrl());
		b.setIsbn(bnew.getIsbn());
		b.setDatabaseFile(bnew.getDatabaseFile());
		b.setDescription(bnew.getDescription());
		bookrepo.save(b);
		return ResponseEntity.ok().body(b);
	}
	
	public ResponseEntity<Book> deleteBook(Integer bookId) throws ResourceNotFoundException
	{
		Book book=bookrepo.findById(bookId).orElseThrow(()->new ResourceNotFoundException("Book not found for this id "+bookId));
		bookrepo.delete(book);
		return ResponseEntity.ok().body(book);
	}
	
	public List<Book> getAllBooks()
	{
		List<Book> bl=new ArrayList<Book>();
		for(Book b:bookrepo.findAll())
		{
			bl.add(b);
		}
		return bl;
	}
	
	public ResponseEntity<Book> getBookById(int bookId) throws ResourceNotFoundException
	{
		Book book=bookrepo.findById(bookId).orElseThrow(()->new ResourceNotFoundException("Book not found for this id "+bookId));
		return ResponseEntity.ok().body(book);
	}
	
	public ResponseEntity<List<Book>> getBookByTitle(String title)
	{
		List<Book> books=new ArrayList<Book>();
		for(Book b:bookrepo.findAll())
		{
			if(b.getTitle().equalsIgnoreCase(title))
			{
				books.add(b);
			}
		}
		return ResponseEntity.ok().body(books);
	}
	
	public ResponseEntity<List<Book>> getBookByGenre(String genreName)
	{
		List<Book> bl=new ArrayList<Book>();
		for(Book b:bookrepo.findAll())
		{
			if(b.getGenre().getGenreName().equalsIgnoreCase(genreName))
			{
				bl.add(b);
			}
		}
		return ResponseEntity.ok().body(bl);
	}
	
	public ResponseEntity<List<Book>> getBookByAuthor(String authorName)
	{
		List<Book> bl=new ArrayList<Book>();
		for(Book b:bookrepo.findAll())
		{
			if(b.getAuthor().getAuthorName().equalsIgnoreCase(authorName))
			{
				bl.add(b);
			}
		}
		return ResponseEntity.ok().body(bl);
	}
	
	public ResponseEntity<Book> getBookByIsbn(long isbn) throws ResourceNotFoundException
	{
		for(Book book:bookrepo.findAll())
		{
			if(book.getIsbn()==isbn)
			{

				return ResponseEntity.ok().body(book);
			}
		}
		return ResponseEntity.ok().body(null);
	}
	
	public ResponseEntity<List<Book>> getAvailableBooks()
	{
		List<Book> availableBooks=new ArrayList<Book>();
		for(Book b:bookrepo.findAll())
		{
			if(b.getQty()>0)
			{
				availableBooks.add(b);
			}
		}
		return ResponseEntity.ok().body(availableBooks);
	}
	
	public void createSheet()
	{
		header=new HttpHeaders();
		header.setContentType(new MediaType("application","force-download"));
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Booksreport.xlsx");
		workbook=new XSSFWorkbook();
		sheet=workbook.createSheet("Books");
		rownum=1;
		Row rownames=sheet.createRow(rownum++);
		cellnum=1;
		Cell cellidName=rownames.createCell(cellnum++);
		cellidName.setCellValue("bookId");
		Cell celltitleName=rownames.createCell(cellnum++);
		celltitleName.setCellValue("Title");
		Cell cellIsbn=rownames.createCell(cellnum++);
		cellIsbn.setCellValue("ISBN");
		Cell cellquantityName=rownames.createCell(cellnum++);
		cellquantityName.setCellValue("Quantity");
		Cell celldateName=rownames.createCell(cellnum++);
		celldateName.setCellValue("Date of added");
		Cell cellauthorName=rownames.createCell(cellnum++);
		cellauthorName.setCellValue("Author Name");
		Cell cellgenreName=rownames.createCell(cellnum++);
		cellgenreName.setCellValue("Genre Name");
		Cell cellDescription=rownames.createCell(cellnum++);
		cellDescription.setCellValue("Description");
	}
	
	public void createDataInSheet(Book b)
	{
		Row rowvalues=sheet.createRow(rownum++);
		cellnum=1;
		Cell cellid=rowvalues.createCell(cellnum++);
		cellid.setCellValue(b.getBookId());
		Cell celltitle=rowvalues.createCell(cellnum++);
		celltitle.setCellValue(b.getTitle());
		Cell cellIsbnvalue=rowvalues.createCell(cellnum++);
		cellIsbnvalue.setCellValue(b.getIsbn());
		Cell cellquantity=rowvalues.createCell(cellnum++);
		cellquantity.setCellValue(b.getQty());
		Cell celldate=rowvalues.createCell(cellnum++);
		celldate.setCellValue(b.getDate());
		Cell cellauthor=rowvalues.createCell(cellnum++);
		cellauthor.setCellValue(b.getAuthor().getAuthorName());
		Cell cellgenre=rowvalues.createCell(cellnum++);
		cellgenre.setCellValue(b.getGenre().getGenreName());
		Cell cellDescriptionvalue=rowvalues.createCell(cellnum++);
		cellDescriptionvalue.setCellValue(b.getDescription());
	}
	
	public ResponseEntity<ByteArrayResource> generateReport()
	{
		ByteArrayOutputStream bs=new ByteArrayOutputStream();
		createSheet();
		for(Book b:bookrepo.sortBookByTitle())
		{
			createDataInSheet(b);
		}
		try
		{
			workbook.write(bs);
			bs.close();
			workbook.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return new ResponseEntity<>(new ByteArrayResource(bs.toByteArray()),header,HttpStatus.CREATED);
	}
	
	
	public ResponseEntity<ByteArrayResource> generateReportOfAvailableBooks()
	{
		ByteArrayOutputStream bs=new ByteArrayOutputStream();
		createSheet();
		for(Book b:bookrepo.sortBookByTitle())
		{
			if(b.getQty()>0)
			{
				createDataInSheet(b);
			}
		}
		try
		{
			workbook.write(bs);
			bs.close();
			workbook.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return new ResponseEntity<>(new ByteArrayResource(bs.toByteArray()),header,HttpStatus.CREATED);
	}
	
	public ResponseEntity<ByteArrayResource> generateReportOfNotAvailableBooks()
	{
		ByteArrayOutputStream bs=new ByteArrayOutputStream();
		createSheet();
		for(Book b:bookrepo.sortBookByTitle())
		{
			if(b.getQty()==0)
			{
				createDataInSheet(b);
			}
		}
		try
		{
			workbook.write(bs);
			bs.close();
			workbook.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return new ResponseEntity<>(new ByteArrayResource(bs.toByteArray()),header,HttpStatus.CREATED);
	}
	
}

package com.library.database_system.onstartup_scheduling;

import com.library.database_system.domain.*;
import com.library.database_system.repository.*;
import com.library.database_system.service.*;
import org.junit.runner.RunWith;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Component
public class BootStrapData implements CommandLineRunner  {

    private final AuthorRepository authorRepository;
    private  final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowRepository borrowRepository;
    private final GradeLevelRepository gradeLevelRepository;
    private final SectionRepository sectionRepository;
    private final PublishingHouseRepository publishingHouseRepository;
    private final ShelfRepository shelfRepository;
    private final CopyrightRepository copyrightRepository;
    private final ReservationRepository reservationRepository;
    private final AuthorService authorService;
    private final BookService bookService;
    private final BookCopyRepository bookCopyRepository;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final AdminService adminService;
    private final GradeLevelService gradeLevelService;


    public BootStrapData(AuthorRepository authorRepository, BookRepository bookRepository, UserRepository userRepository, BorrowRepository borrowRepository, GradeLevelRepository gradeLevelRepository, SectionRepository sectionRepository, PublishingHouseRepository publishingHouseRepository, CategoryRepository categoryRepository, ShelfRepository shelfRepository, CopyrightRepository copyrightRepository, ReservationRepository reservationRepository, AuthorService authorService, BookService bookService, CategoryRepository categoryRepository1, AdminService adminService, BookCopyRepository bookCopyRepository, CategoryService categoryService, GradeLevelService gradeLevelService) {
//    public BootStrapData(AdminService adminService, CategoryService categoryService, GradeLevelService gradeLevelService) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.borrowRepository = borrowRepository;
        this.gradeLevelRepository = gradeLevelRepository;
        this.sectionRepository = sectionRepository;
        this.publishingHouseRepository = publishingHouseRepository;
        this.shelfRepository = shelfRepository;
        this.copyrightRepository = copyrightRepository;
        this.reservationRepository = reservationRepository;
        this.authorService = authorService;
        this.bookService = bookService;
        this.categoryRepository = categoryRepository1;
        this.bookCopyRepository = bookCopyRepository;
        this.adminService = adminService;
        this.categoryService = categoryService;
        this.gradeLevelService = gradeLevelService;
    }

    @Override
    public void run(String... args) throws Exception {
//        // Cleanup the tables
//        authorRepository.deleteAllInBatch();
//        bookRepository.deleteAllInBatch();
//        userRepository.deleteAllInBatch();
//        borrowRepository.deleteAllInBatch();
//        gradeLevelRepository.deleteAllInBatch();
//        sectionRepository.deleteAllInBatch();
//        publishingHouseRepository.deleteAllInBatch();
//        categoryRepository.deleteAllInBatch();
//        shelfRepository.deleteAllInBatch();
//        copyrightRepository.deleteAllInBatch();
//        reservationRepository.deleteAllInBatch();


        if (categoryService.isEmpty()) {
            // AUTHORS
            Author robert  = new Author("Robert", "C.", "Martin");
            Author timothy  = new Author("Timothy", "R.", "Ottinger");
            Author michael  = new Author("Michael", "J.", "Langr");
            // TODO: Check first if the author exist by checking full name
            authorRepository.saveAll(List.of(robert, timothy, michael));
            // BOOKS
            String summary = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Fames ac turpis egestas integer eget aliquet nibh praesent. Aliquet enim tortor at auctor. Purus sit amet volutpat consequat mauris nunc congue. Nec tincidunt praesent semper feugiat nibh sed pulvinar proin. A scelerisque purus semper eget duis at tellus. Amet commodo nulla facilisi nullam vehicula. Gravida arcu ac tortor dignissim. Nunc congue nisi vitae suscipit tellus mauris a diam. Eget egestas purus viverra accumsan in nisl nisi scelerisque. Massa tincidunt nunc pulvinar sapien et ligula ullamcorper malesuada. Et odio pellentesque diam volutpat commodo. Etiam non quam lacus suspendisse faucibus interdum posuere lorem. Enim nunc faucibus a pellentesque sit amet. Nunc sed id semper risus in hendrerit gravida rutrum.";
            Book cleanCode = new Book(
                    "Clean Code",
                    "0132350882",
                    3,
                    3,
                    "English",
                    "1",
                    LocalDate.now(),
                    LocalDate.of(2017, 6, 15),
                    summary,
                    "empty",
                    LocalTime.now()
                    );


            Book cleanCode2 = new Book(
                    "Discrete Mathematics and Its Application",
                    "9780073383095",
                    4,
                    4,
                    "English",
                    "7",
                    LocalDate.now(),
                    LocalDate.of(2012, 6, 15),
                    summary,
                    "empty",
                    LocalTime.now() );

            Book tSP = new Book(
                    "The Self-Taught Programmer",
                    "9781940733012",
                    10,
                    10,
                    "English",
                    "1",
                    LocalDate.now(),
                    LocalDate.of(2019, 4, 20),
                    summary,
                    "empty",
                    LocalTime.now());
            bookRepository.saveAll(List.of(cleanCode, cleanCode2, tSP));

            // copies
            Set<BookCopy> bookCopies = new HashSet<>();
            BookCopy cpy;
            for (int i = 0; i < cleanCode.getQuantity(); i++) {
                cpy = new BookCopy(i+1, "Available");
                this.bookCopyRepository.save(cpy);
                cpy.setBook(cleanCode);
                bookCopies.add(cpy);
            }
            cleanCode.setBookCopy(bookCopies);
            this.bookCopyRepository.saveAll(bookCopies);

            bookCopies = new HashSet<>();
            for (int i = 0; i < cleanCode2.getQuantity(); i++) {
                cpy = new BookCopy(i+1, "Available");
                this.bookCopyRepository.save(cpy);
                cpy.setBook(cleanCode2);
                bookCopies.add(cpy);
            }
            cleanCode2.setBookCopy(bookCopies);
            this.bookCopyRepository.saveAll(bookCopies);

            bookCopies = new HashSet<>();
            for (int i = 0; i < tSP.getQuantity(); i++) {
                cpy = new BookCopy(i+1, "Available");
                this.bookCopyRepository.save(cpy);
                cpy.setBook(tSP);
                bookCopies.add(cpy);
            }
            tSP.setBookCopy(bookCopies);
            this.bookCopyRepository.saveAll(bookCopies);

            // adding on set
            robert.getBooksAuthored().add(cleanCode);
            timothy.getBooksAuthored().add(cleanCode);
            michael.getBooksAuthored().add(cleanCode);

            robert.getBooksAuthored().add(cleanCode2);
            timothy.getBooksAuthored().add(cleanCode2);
            michael.getBooksAuthored().add(cleanCode2);

            robert.getBooksAuthored().add(tSP);

            List<Author> auts = List.of(robert, timothy, michael);
            cleanCode.getAuthors().addAll(auts);
            cleanCode2.getAuthors().addAll(auts);
            tSP.getAuthors().add(robert);

            // saving on database
            bookRepository.saveAll(List.of(cleanCode, cleanCode2, tSP));
            authorRepository.saveAll(List.of(robert, timothy, michael));

            // USERS and BORROW
            User jamsed = new User("20250014549", "Albert", "Roke" , "Raul", "US", "Student");
            userRepository.save(jamsed);

            int numDaysBorrow = 5;
            LocalDate currentDate = LocalDate.now();
            LocalDate returnedDate = currentDate.plusDays(numDaysBorrow);

            BookCopy cleanCodeCopy1 = cleanCode.getBookCopy().stream().filter(copyFilt -> copyFilt.getCopy_num() == 1).findFirst().get();
            Borrow jamsedCleanCode1 = new Borrow(cleanCodeCopy1, jamsed, currentDate, returnedDate);
            cleanCodeCopy1.setStatus("Borrowed");
            borrowRepository.save(jamsedCleanCode1);

            jamsed.getBorrowed().add(jamsedCleanCode1);
            cleanCodeCopy1.setBorrower(jamsedCleanCode1);

            userRepository.save(jamsed);
            borrowRepository.save(jamsedCleanCode1);
            bookCopyRepository.save(cleanCodeCopy1);

            // GRADELEVEL and SECTIONS
            GradeLevel g8 = new GradeLevel(8);
            GradeLevel g9 = new GradeLevel(9);
            gradeLevelRepository.saveAll(List.of(g8, g9));

            Section c = new Section("C", g8);
            Section h = new Section("H", g8);
            Section b = new Section("B", g9);
            sectionRepository.saveAll(List.of(c, h, b));

            // add section to user
            jamsed.setSection(c);
            userRepository.save(jamsed);
            g8.getSections().addAll(List.of(c, h));
            g9.getSections().add(b);
            gradeLevelRepository.saveAll(List.of(g8, g9));

            // ADD PUBLISHER on BOOKS
            PublishingHouse oRiely = new PublishingHouse(
                    "O'Reilly Media, Inc.",
                    "1005 Gravenstein Highway North, Sebastopol, CA 95472");
            PublishingHouse pearson = new PublishingHouse(
                    "Pearson Education, Inc",
                    "501 Boylston Street, Suite 900 Boston, MA 02116"
            );
            publishingHouseRepository.saveAll(List.of(oRiely, pearson));

            tSP.setPublisher(oRiely);
            cleanCode.setPublisher(pearson);
            cleanCode2.setPublisher(pearson);
            oRiely.getBooks().add(tSP);
            pearson.getBooks().addAll(List.of(cleanCode, cleanCode2));

            publishingHouseRepository.saveAll(List.of(oRiely, pearson));
            bookRepository.saveAll(List.of(cleanCode, cleanCode2, tSP));

            // CATEGORY
            String[] lccCats = {
                    "General Works",
                    "Philosophy. Psychology. Religion",
                    "Auxiliary Sciences of History",
                    "World History and History of Asia, etc",
                    "History of America",
                    "Local History of the Americas",
                    "Geography, Anthropology, Recreation",
                    "Social Sciences",
                    "Political Science",
                    "Law",
                    "Education",
                    "Music",
                    "Fine Arts",
                    "Language and Literature",
                    "Science",
                    "Medicine",
                    "Agriculture",
                    "Technology",
                    "Military Science",
                    "Naval Science",
                    "Bibliography, Library Science"};
            List<Category> ltCats = new ArrayList<>();
            for (String cat: lccCats){
                ltCats.add(new Category(cat));
            }
            categoryService.saveAll(ltCats);
    //
            int scienceIdx = 14;
            int compIdx = 17;

            ltCats.get(scienceIdx).getBooks().add(tSP);
            ltCats.get(compIdx).getBooks().addAll(List.of(cleanCode, cleanCode2));

            cleanCode.setCategory(ltCats.get(compIdx));
            cleanCode2.setCategory(ltCats.get(compIdx));
            tSP.setCategory(ltCats.get(scienceIdx));

            bookRepository.saveAll(List.of(cleanCode, cleanCode2, tSP));

            // SHELF
            Shelf shelfOne = new Shelf("1");
            Shelf shelfTwo = new Shelf("2");
            shelfRepository.saveAll(List.of(shelfOne, shelfTwo));

            cleanCode.setShelf(shelfOne);
            cleanCode2.setShelf(shelfOne);
            tSP.setShelf(shelfTwo);

            shelfOne.getBooks().addAll(List.of(cleanCode, cleanCode2));
            shelfTwo.getBooks().add(tSP);

            shelfRepository.saveAll(List.of(shelfOne, shelfTwo));
            bookRepository.saveAll(List.of(cleanCode, cleanCode2, tSP));

            // COPYRIGHT
            Copyright pearsonCopyright = new Copyright("Pearson Education, Inc.", (short) 2009);
            Copyright michaelCopyright = new Copyright("Michael Y. Yang", (short)2011);
            copyrightRepository.saveAll(List.of(pearsonCopyright, michaelCopyright));  // not necessary

            pearsonCopyright.getBooks().addAll(List.of(cleanCode, cleanCode2));
            michaelCopyright.getBooks().add(tSP);

            cleanCode.setCopyright(pearsonCopyright);
            cleanCode2.setCopyright(pearsonCopyright);
            tSP.setCopyright(michaelCopyright);

            copyrightRepository.saveAll(List.of(pearsonCopyright, michaelCopyright));
            bookRepository.saveAll(List.of(cleanCode, cleanCode2, tSP));

            // RESERVATION
            BookCopy tSP1 = tSP.getBookCopy().stream().filter(copyFilt -> copyFilt.getCopy_num() == 1).findFirst().get();
            Reservation jamReservedSP1 = new Reservation(
                    //LocalDate.of(2021, 3, 30),
                    tSP1,
                    jamsed,
                    returnedDate);
            reservationRepository.save(jamReservedSP1);

            jamsed.getReservations().add(jamReservedSP1);
            tSP1.setReserved(jamReservedSP1);
            reservationRepository.save(jamReservedSP1);
            userRepository.save(jamsed);


    //        if (this.gradeLevelService.isEmpty()){
    //            GradeLevel gradeLevel = new GradeLevel();
    //            gradeLevel.setLevel(1);
    //            this.gradeLevelService.addNewGradeLevel(gradeLevel);
    //        }
            // ADD ADMIN
//            Admin admin = new Admin("admin", "admin");
//            if (!this.adminService.contain(admin)) {
//                this.adminService.addNewAdmin(admin);
//            }
        }
    }
}
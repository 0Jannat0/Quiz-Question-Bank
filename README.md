# Quiz Question Bank

Student: Sadykova Zhannat
ID: 250104017
Group: COMSE25

## Description
Quiz Question Bank is a console-based Java application for managing a collection of quiz questions.  
Users can create, read, update, and delete questions. Questions are stored persistently (files or SQLite database).  
The application supports quizzes, filtering by topic, and CSV import/export.

## Objectives
- Provide a user-friendly command-line interface for managing quiz questions.
- Implement full CRUD operations (Create, Read, Update, Delete).
- Ensure data persistence between sessions (file storage and SQL database).
- Demonstrate core OOP principles: Encapsulation, Inheritance, Polymorphism.
- Allow data export and import in CSV format.

## Key Requirements (10 implemented)
1. **CRUD Operations** – Add, list, edit, and delete questions.
2. **Command Line Interface** – Clear menus and prompts for all actions.
3. **Input Validation** – Prevents empty text, invalid answer indices, etc.
4. **Data Persistence** – Saves questions to a text file (`questions_db.txt`) and optionally SQLite database (`quizbank.db`).
5. **Modular Design** – Separated into multiple classes: `Question`, `Topic`, storage interfaces and implementations.
6. **Documentation** – This README provides full project overview.
7. **Error Handling** – Graceful handling of file I/O errors, invalid user input.
8. **Encapsulation** – Private fields with public getters/setters in `Question` class.
9. **Inheritance** – Abstract base class `BaseQuestionStorage` extended by all storage implementations.
10. **Polymorphism** – `QuestionStorage` interface allows switching between memory, file, and SQL storage without changing `Main`.

## Project Structure
com.quizbank/
├── Question.java // Question entity with validation
├── Topic.java // Enum of topics
├── QuestionStorage.java // Storage interface (polymorphism)
├── BaseQuestionStorage.java // Abstract base class (inheritance)
├── InMemoryQuestionStorage.java // RAM storage (for testing)
├── FileQuestionStorage.java // Text file persistence
├── SQLQuestionStorage.java // SQLite database (bonus)
└── Main.java // CLI menu and program entry



## How to Run
1. Ensure Java 17+ is installed.
2. Compile all `.java` files:
java com/quizbank/*.java
3. Run the main class:
java com.quizbank.Main
4. (For SQL storage) Add `sqlite-jdbc-3.42.0.0.jar` to classpath.
5. 
## Test Cases & Screenshots

### Test Case 1: Adding a question
- Input:
Text: What is the capital of Italy?
Options: Rome, Milan, Naples
Correct answer: 1
Topic: General Knowledge
- Expected output: "Question added and saved."

### Test Case 2: Listing all questions
- Expected: Table of questions with indices and topics.

### Test Case 3: Taking a quiz
- Expected: Randomised questions, score calculation at the end.

### Test Case 4: CSV Export/Import
- Export creates `export.csv`; import reads similarly formatted CSV.

## Presentation
All presentation materials (slides, demo video) are available here:  

## Data Files
- `questions_db.txt` – plain text storage format (text|options|correctIndex|topic)
- `quizbank.db` – SQLite database file (if using SQL storage)
- `export.csv` / `import.csv` – CSV data exchange

## Challenges & Solutions
- **File persistence:** Used `BufferedReader`/`BufferedWriter` with custom text format. Learned about `try-with-resources` for safe file handling.
- **Polymorphism design:** Created interface `QuestionStorage` and abstract base class to share validation logic.
- **Input validation:** Added helper methods `readInt()` and `readNonEmptyString()` to prevent crashes.
- **CSV parsing:** Implemented simple split-based parser; in production would use a library like OpenCSV.

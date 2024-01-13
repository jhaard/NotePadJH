package com.example.notepadjh;
// Model
import android.content.Context;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class EventManager {
    Context context;
    String fileType = ".txt";
    public static ArrayList<Note> notes = new ArrayList<>();

    public EventManager(Context context) {
        this.context = context;
    }

    /**
     *
     * @param note Aktuell note att radera.
     * @return Aktuell note, annars null.
     *
     * Ta bort fil och note för position i adapter.
     */
    public File deleteNote(Note note) {
        if (getTitleFile(note).delete() && getTextFile(note).delete()) {
            return getTitleFile(note);
        }
        return null;
    }

    /**
     *
     * @param note som skapas i NoteActivity.
     * Spara till fil eller skriv över om titel matchar filen.
     */
    public void saveNoteData(Note note) {
        File folderTitle = new File(context.getFilesDir(), "NoteTitleFolder");
        File folderText = new File(context.getFilesDir(), "NoteTextFolder");

        if (!folderTitle.exists() && !folderText.exists()) {
            folderTitle.mkdir();
            folderText.mkdir();
        }

        File titleFile = new File(folderTitle, note.getNoteTitle() + fileType);
        File textFile = new File(folderText, note.getNoteTitle() + fileType);

        if(!titleFile.exists() && !textFile.exists()) {
            try {
                PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(titleFile, true)));
                PrintWriter writer2 = new PrintWriter(new BufferedWriter(new FileWriter(textFile, true)));
                writer.append(note.getNoteTitle());
                writer2.append(note.getNoteText());
                writer.flush();
                writer2.flush();
                writer.close();
                writer2.close();

                notes.add(note);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Toast.makeText(context, "SAVED NEW NOTE", Toast.LENGTH_SHORT).show();

        } else {
            try {
                PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(titleFile, false)));
                PrintWriter writer2 = new PrintWriter(new BufferedWriter(new FileWriter(textFile, false)));
                writer.append(note.getNoteTitle());
                writer2.append(note.getNoteText());
                writer.flush();
                writer2.flush();
                writer.close();
                writer2.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Toast.makeText(context, "OVERWRITING EXISTING", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Ladda in de filer som finns och spara i objekt-lista vid start.
     */
    public void loadNoteFromFile() {
        File readTitleFile = new File(context.getFilesDir(), "/NoteTitleFolder/");
        File readTextFile = new File(context.getFilesDir(), "/NoteTextFolder/");

        if (readTitleFile.exists() && readTextFile.exists()) {

            // Gå igenom filerna i Titel-mappen. Spara substräng av enbart filnamn.
            for (File f : Objects.requireNonNull(readTitleFile.listFiles())) {
                String s = f.toString().substring(57);
                // Samma i Text-mappen
                for (File f2 : Objects.requireNonNull(readTextFile.listFiles())) {
                    String s2 = f2.toString().substring(56);
                    // Om filnamnen är samma, hämta båda filerna.
                    if (s.equals(s2)) {
//
                        try {
                            Scanner scanner = new Scanner(f);
                            String content = scanner.nextLine();
                            scanner.close();

                            Scanner scanner2 = new Scanner(f2);
                            // Om enbart titel angetts, stäng scanner2 för text direkt
                            // (NoSuchElementException)
                            if (!scanner2.hasNextLine()) {
                                Note note = new Note(content, "");
                                notes.add(note);
                                System.out.println("Notes no text: " + note);

                                scanner2.close();
                            } else {
                                String content2 = scanner2.nextLine();

                                System.out.println(content2);

                                Note note = new Note(content, content2);
                                notes.add(note);
                                System.out.println("Notes with text: " + note);
                                scanner2.close();
                            }

                        } catch (FileNotFoundException e) {
                            e.getStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @param note Aktuellt note-objekt.
     * @return Fil för valt objekt.
     */
    public File getTitleFile(Note note) {
        File directoryForTitles = new File(context.getFilesDir(), "/NoteTitleFolder/");
        String clickedTitleFile = directoryForTitles + "/" + note.getNoteTitle() + ".txt";
        String subStr = clickedTitleFile.substring(57);

        ArrayList<File> currentFilesInTitleDirectory = goThroughFilesInTitleFolder(directoryForTitles, subStr);

        if (!currentFilesInTitleDirectory.isEmpty()) {
            for (File file : currentFilesInTitleDirectory) {
                return new File(file.getAbsolutePath());
            }
        }
        return null;
    }

    /**
     *
     * @param note Aktuellt note-objekt.
     * @return Fil för valt objekt.
     */
    public File getTextFile(Note note) {
        File directoryForTexts = new File(context.getFilesDir(), "/NoteTextFolder/");
        String clickedTextFile = directoryForTexts + "/" + note.getNoteTitle() + ".txt";
        String subStr2 = clickedTextFile.substring(56);

        ArrayList<File> currentFilesInTextDirectory = goThroughFilesInTextFolder(directoryForTexts, subStr2);

        if (!currentFilesInTextDirectory.isEmpty()) {
            for (File file : currentFilesInTextDirectory) {
                return new File(file.getAbsolutePath());
            }
        }
        return null;
    }

    /**
     *
     * @param noteTitleDirectory Mapp för title-filer.
     * @param name Namn på fil.
     * @return Lista på files som finns.
     */
    public ArrayList<File> goThroughFilesInTitleFolder(File noteTitleDirectory, String name) {
        ArrayList<File> filesInMemory = new ArrayList<>();
        File[] files = noteTitleDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    filesInMemory.addAll(goThroughFilesInTitleFolder(file, name));
                } else {
                    if (file.getName().equals(name)) {
                        filesInMemory.add(file);
                    }
                }
            }
        }
        return filesInMemory;
    }

    /**
     *
     * @param noteTextDirectory Mapp för text-filer.
     * @param name Namn på fil.
     * @return Lista på files som finns.
     */
    public ArrayList<File> goThroughFilesInTextFolder(File noteTextDirectory, String name) {
        ArrayList<File> filesInMemory = new ArrayList<>();
        File[] files = noteTextDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    filesInMemory.addAll(goThroughFilesInTextFolder(file, name));
                } else {
                    if (file.getName().equals(name)) {
                        filesInMemory.add(file);
                    }
                }
            }
        }
        return filesInMemory;
    }

    /**
     *
     * @param title Titel-fil.
     * @return Sträng-innehållet i filen.
     */
    public String readTitleContent(File title) {
        String content;
        try {
            Scanner scanner = new Scanner(title);
            content = scanner.nextLine();
            scanner.close();

        } catch (FileNotFoundException e) {
            e.getStackTrace();
            throw new RuntimeException(e);
        }
        return content;
    }

    /**
     *
     * @param text Text-fil.
     * @return Sträng-innehållet i filen.
     */
    public String readTextContent(File text) {
        String content;
        try {
            Scanner scanner = new Scanner(text);
            if (scanner.hasNextLine()) {
                content = scanner.nextLine();
                scanner.close();
            } else {
                content = null;
                scanner.close();
            }
            } catch(FileNotFoundException e){
                e.getStackTrace();
                throw new RuntimeException(e);
            }
            return content;
        }
    }
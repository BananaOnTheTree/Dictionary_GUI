package Game.Quiz.utils;
import Game.Quiz.models.Category;
import Game.Quiz.models.Question;
import Game.Quiz.ObjectDB;
import java.io.*;
import java.util.ArrayList;

public class FileHandler {
    public static final String CATEGORY_FOLDER = "categories";
    public static final String SAVE_FILENAME = "user.save";
    public static final String SAVE_FIlEDICTIONARY = "dictionariesBackup.txt";
    public static ArrayList<Category> loadCategory() {

        ArrayList<Category> result = new ArrayList<Category>();
        int score;
        String questionPrompt;
        String answer;

        try {
            String path = System.getProperty("user.dir") + File.separator + CATEGORY_FOLDER;
            System.out.println(String.format("Loading path %s", path));
            File folder = new File(path);
            for (File fileEntry : folder.listFiles()) {
                Category category = new Category(fileEntry.getName());
                BufferedReader br = new BufferedReader(new FileReader(fileEntry));

                String line;
                while ((line = br.readLine()) != null) {
                    String[] strList = line.split(",");

                    score = Integer.parseInt(strList[0]);
                    questionPrompt = strList[1];
                    answer = strList[2];

                    Question question = new Question(questionPrompt, answer, score);
                    category.add(question);
                }

                result.add(category);
                br.close();
            }

        } catch (Exception e) {
            System.out.println("loadCategory error");
            e.printStackTrace();
        }

        return result;
    }
    public static void saveDB(ObjectDB db) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILENAME))) {
            oos.writeObject(db);
            System.out.println("DB has been saved");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ObjectDB loadDB() {
        ObjectDB db = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILENAME))) {
            db = (ObjectDB) ois.readObject();
        } catch (Exception e) {
            System.out.println("DB loading error");
            e.printStackTrace();
        }
        return db;
    }
    public static boolean saveFileExist() {
        File f = new File(SAVE_FILENAME);
        if(f.exists() && !f.isDirectory()) {
            System.out.println("Save file exist");
            return true;
        }
        System.out.println("Save file dne");
        return false;
    }

}

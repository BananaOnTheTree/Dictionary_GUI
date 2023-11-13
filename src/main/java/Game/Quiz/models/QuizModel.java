package Game.Quiz.models;
import Game.Quiz.ObjectDB;
import Game.Quiz.utils.FileHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizModel {
    private int _winning;
    private int _remainingQuestion;
    private ArrayList<Category> _cats;
    private Game.Quiz.models.Question _activeQuestion;
    public QuizModel() {
        _winning = 0;
        _cats = FileHandler.loadCategory();
        updateRemainingQuestion();
        try {
            if (FileHandler.saveFileExist()) {
                load();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean answerQuestion(Game.Quiz.models.Question question, String input) {
        question.setAttempted(true);
        _remainingQuestion -= 1;
        if (question.getAnswer().equalsIgnoreCase(input)) {
            _winning += question.getScore();
            return true;
        } else {
            _winning -= question.getScore();
            return false;
        }

    }
    public void updateRemainingQuestion() {
        _remainingQuestion = 0;
        for (Game.Quiz.models.Category cat : _cats) {
            for (Game.Quiz.models.Question question : cat.getQuestions()) {
                if (!question.isAttempted()) {
                    _remainingQuestion += 1;
                }
            }
        }
    }
    public void save() {
        ObjectDB db = new ObjectDB();
        db.setWinning(_winning);
        HashMap<String, Boolean> isAttemptedMap = new HashMap<String, Boolean>();
        for (Game.Quiz.models.Category cat : _cats) {
            for (Game.Quiz.models.Question question : cat.getQuestions()) {
                isAttemptedMap.put(question.toString(), question.isAttempted());
            }
        }
        db.setIsAttemptedMap(isAttemptedMap);
        FileHandler.saveDB(db);
    }

    public void load() {
        ObjectDB db = FileHandler.loadDB();
        _winning = db.getWinning();
        Map<String, Boolean> isAttemptedMap = db.getIsAttemptedMap();
        for (Game.Quiz.models.Category cat : _cats) {
            for (Game.Quiz.models.Question question : cat.getQuestions()) {
                if (isAttemptedMap.get(question.toString()) == true) {
                    question.setAttempted(true);
                }
            }

        }
        updateRemainingQuestion();
    }
    public void reset() {
        _winning = 0;
        _remainingQuestion = 0;
        for (Game.Quiz.models.Category cat : _cats) {
            for (Game.Quiz.models.Question question : cat.getQuestions()) {
                question.setAttempted(false);
                _remainingQuestion += 1;
            }
        }
    }

    public void setActiveQuestion(Game.Quiz.models.Question question) {
        _activeQuestion = question;
    }
    public Game.Quiz.models.Question getActiveQuestion() {
        return _activeQuestion;
    }
    public ArrayList<Game.Quiz.models.Category> getCategoryList() {
        return _cats;
    }
    public int getWinning() {
        return _winning;
    }
    public String getWinningStr() {
        return "$" + Integer.toString(_winning);
    }
    public int getRemainingQuestionCount() {
        return _remainingQuestion;
    }

}

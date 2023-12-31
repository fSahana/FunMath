package com.example.funmath.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME= "MathQuiz.db";
    private static final int DATABASE_VERSION=1;
    private SQLiteDatabase db;
    public Database( Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;
        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuizContact.QuestionsTable.TABLE_NAME + " ( " +
                QuizContact.QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuizContact.QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuizContact.QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuizContact.QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuizContact.QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuizContact.QuestionsTable.COLUMN_ANSWER_NR + " INTEGER" + ")";
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int v, int v1) {

        db.execSQL("DROP TABLE IF EXISTS " + QuizContact.QuestionsTable.TABLE_NAME);
        onCreate(db);
    }



    private  void fillQuestionsTable(){
        Question q1 = new Question ("6+4?" ,
                "10", "7", "9", 1);
        addQuestion(q1);
        Question q2 = new Question ("12/4?" ,
                "4", "3", "6", 2);
        addQuestion(q2);
        Question q3 = new Question ("9-3?" ,
                "4", "5", "6", 3);
        addQuestion(q3);
        Question q4 = new Question ("3*3?" ,
                "9", "6", "1", 1);
        addQuestion(q4);
        Question q5 = new Question ("10-6?" ,
                "2", "5", "4", 3);
        addQuestion(q5);
        Question q6 = new Question ("8+7?" ,
                "14", "15", "13", 2);
        addQuestion(q6);
        Question q7 = new Question ("8/4?" ,
                "12", "2", "4", 2);
        addQuestion(q7);
        Question q8 = new Question ("12-10?" ,
                "7", "5", "2", 3);
        addQuestion(q8);
        Question q9 = new Question ("11/1?" ,
                "11", "1", "5", 1);
        addQuestion(q9);
        Question q10 = new Question ("10/2?" ,
                "6", "2", "5", 3);
        addQuestion(q10);
    }

    private void addQuestion(Question question){
        ContentValues cv = new ContentValues();
        cv.put(QuizContact.QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuizContact.QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuizContact.QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuizContact.QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuizContact.QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        db.insert(QuizContact.QuestionsTable.TABLE_NAME, null,cv);
    }


    @SuppressLint("Range")
    public ArrayList<Question> getAllQuestions(){
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ QuizContact.QuestionsTable.TABLE_NAME, null);

        if(c.moveToFirst()){
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuizContact.QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuizContact.QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuizContact.QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuizContact.QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuizContact.QuestionsTable.COLUMN_ANSWER_NR)));
                questionList.add(question);
            }while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}

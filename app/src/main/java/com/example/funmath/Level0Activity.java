package com.example.funmath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funmath.Database.Database;
import com.example.funmath.Database.Question;

import java.util.ArrayList;
import java.util.Collections;



public class Level0Activity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extraScore";

    private static final String KEY_SCORE = "keyscore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";

    private static final String Key_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";


    private TextView qNumber;
    private TextView qScore;
    private TextView QuestionLvl0;
    private RadioGroup qRadioButton;
    private RadioButton qOption1;
    private RadioButton qOption2;
    private RadioButton qOption3;
    private Button lNext;

    private Drawable backGroundColorDefaultRb;
    private ColorStateList textColorDefaultRb;

    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private boolean answered;

    MediaPlayer player1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level0);


        qNumber = findViewById(R.id.textView3);
        qScore = findViewById(R.id.text_view_score2);
        QuestionLvl0 = findViewById(R.id._text_view_question4);
        qRadioButton = findViewById(R.id.radio_group);
        qOption1 = findViewById(R.id.radio_button1);
        qOption2 = findViewById(R.id.radio_button2);
        qOption3 = findViewById(R.id.radio_button3);
        lNext = findViewById(R.id.button_next);

        player1= MediaPlayer.create(this,R.raw.applausesound);

        textColorDefaultRb = qOption1.getTextColors();
        backGroundColorDefaultRb = qOption1.getBackground();



        if (savedInstanceState == null){

            Database dbHelper = new Database(this);
            questionList = dbHelper.getAllQuestions();
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestions();
        } else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            answered = savedInstanceState.getBoolean(Key_ANSWERED);


        }
        lNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (qOption1.isChecked() || qOption2.isChecked() || qOption3.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(Level0Activity.this, "Please Select an Answer", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    showNextQuestions();

                    if(player1!=null)
                    {
                        player1.stop();
                        player1.prepareAsync();
                    }
                }
            }
        });
    }

    private void showNextQuestions() {
        qOption1.setBackgroundColor(Color.rgb(255,127,80));
        qOption2.setBackgroundColor(Color.rgb(255,127,80));
        qOption1.setBackgroundColor(Color.rgb(255,127,80));
        qRadioButton.clearCheck();

        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            QuestionLvl0.setText(currentQuestion.getQuestion());
            qOption1.setText(currentQuestion.getOption1());
            qOption2.setText(currentQuestion.getOption2());
            qOption3.setText(currentQuestion.getOption3());

            questionCounter++;
            qNumber.setText("Question: " + questionCounter + " /" + questionCountTotal);
            answered = false;
            lNext.setText("Confirm");


        } else {
            finishQuiz();
        }
    }


    private void checkAnswer() {
        answered = true;
        RadioButton qOptionSelected = findViewById(qRadioButton.getCheckedRadioButtonId());
        int answerRt = qRadioButton.indexOfChild(qOptionSelected) + 1;

        if (answerRt == currentQuestion.getAnswerNr()){
            score++;

            player1.start();

            qScore.setText("Score: "+score);
        }
        showSolution();

    }


    private void showSolution(){
        qOption1.setBackgroundColor(Color.RED);
        qOption2.setBackgroundColor(Color.RED);
        qOption3.setBackgroundColor(Color.RED);

        switch (currentQuestion.getAnswerNr()){
            case 1:
                qOption1.setBackgroundColor(Color.GREEN);
                QuestionLvl0.setText("Answer 1  Correct");
                break;

            case 2:
                qOption2.setBackgroundColor(Color.GREEN);
                QuestionLvl0.setText("Answer 2  Correct");
                break;

            case 3:
                qOption3.setBackgroundColor(Color.GREEN);
                QuestionLvl0.setText("Answer 3  Correct");
                break;
        }

        if(questionCounter < questionCountTotal){
            lNext.setText("Next");
        }else{
            lNext.setText("Confirm");
        }


    }


    private void finishQuiz() {
        Intent resultIntent = new Intent(Level0Activity.this, ResultActivity.class);
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK);
        startActivity(resultIntent);
        finish();
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putBoolean(Key_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);

    }

    public  void play(){
        while(true){
            player1.start();
        }
    }

}
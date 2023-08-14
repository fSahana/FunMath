package com.example.funmath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import java.util.Locale;

public class Level1Activity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "Score More";
    private static final long COUNTDOWN_IN_MILLIS = 20000;

    private static final String KEY_SCORE = "keyscore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String Key_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";


    private TextView qNumber;
    private TextView qScore;
    private TextView textViewCountDown;
    private TextView QuestionLvl0;
    private RadioGroup qRadioButton;
    private RadioButton qOption1;
    private RadioButton qOption2;
    private RadioButton qOption3;
    private Button lNext;

    private Drawable backGroundColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private  long timeLeftInMillis;

    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private boolean answered;

    private long backPressedTime;
    int minutes,seconds;
    MediaPlayer player1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level1);


        qNumber = findViewById(R.id.textView4);
        qScore = findViewById(R.id.text_view_score);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        QuestionLvl0 = findViewById(R.id._text_view_question2);
        qRadioButton = findViewById(R.id.radio_group);
        qOption1 = findViewById(R.id.radio_button1);
        qOption2 = findViewById(R.id.radio_button2);
        qOption3 = findViewById(R.id.radio_button3);
        lNext = findViewById(R.id.button_next2);

        player1= MediaPlayer.create(this,R.raw.applausesound);

        backGroundColorDefaultRb = qOption1.getBackground();
        textColorDefaultCd = textViewCountDown.getTextColors();

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
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(Key_ANSWERED);

            if (!answered){
                startCountDown();
            }else {
                updateCountDownText();
                showSolution();
            }
        }
        lNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (qOption1.isChecked() || qOption2.isChecked() || qOption3.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(Level1Activity.this, "Select an answer", Toast.LENGTH_SHORT).show();
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
            lNext.setText("Apply it");
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;


            startCountDown();


        } else {
            finishQuiz();
        }
    }

    private void startCountDown(){

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis =0;

                showNextQuestions();
                finishQuiz();
            }
        }
                .start();
    }

    private  void updateCountDownText(){
        minutes =  (int )(timeLeftInMillis / 1000) / 60;
        seconds = (int) (timeLeftInMillis /1000) %60;

        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes,seconds);

        textViewCountDown.setText(timeFormatted);

        if( timeLeftInMillis < 10000){
            textViewCountDown.setTextColor(Color.RED);
        }else if (timeLeftInMillis <= 20000){
            textViewCountDown.setTextColor(Color.GREEN);
        }
        else {
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer() {
        answered = true;

        countDownTimer.cancel();

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
    private void finishQuiz () {
        Intent resultIntent = new Intent(Level1Activity.this, ResultActivity.class);
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK);
        startActivity(resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime +2000 > System.currentTimeMillis()){
            finishQuiz();
        }
        else {
            Toast.makeText(this, " Press again", Toast.LENGTH_SHORT).show();
        }
        backPressedTime= System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer !=null){
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(Key_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);

    }

    public  void play(){
        while(true){
            player1.start();
        }
    }

}
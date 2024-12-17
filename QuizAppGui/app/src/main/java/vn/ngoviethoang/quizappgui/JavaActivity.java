package vn.ngoviethoang.quizappgui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.ArrayList;

public class JavaActivity extends AppCompatActivity {
    private ArrayList<Question> questions = new ArrayList<>();
    private ArrayList<Answer> selectedAnswers = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private boolean isAnswered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);

        questions.add(new Question("What is Java?", new String[]{"A programming language", "A car", "A fruit", "A city"}, "A programming language"));
        questions.add(new Question("Which company developed Java?", new String[]{"Microsoft", "Apple", "Sun Microsystems", "Google"}, "Sun Microsystems"));
        questions.add(new Question("What is JVM?", new String[]{"Java Virtual Machine", "Java Visual Machine", "Java Version Machine", "Java Variable Machine"}, "Java Virtual Machine"));

        displayQuestion();
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            TextView questionTextView = findViewById(R.id.questionTextView);
            questionTextView.setText(question.getQuestion());

            String[] answers = question.getAnswers();
            ((TextView) findViewById(R.id.answerATextView)).setText(answers[0]);
            ((TextView) findViewById(R.id.answerBTextView)).setText(answers[1]);
            ((TextView) findViewById(R.id.answerCTextView)).setText(answers[2]);
            ((TextView) findViewById(R.id.answerDTextView)).setText(answers[3]);
        } else {
            goToResult();
        }
    }

    public void checkAnswer(View view) {
        if (isAnswered) return;

        TextView selectedTextView = (TextView) view;
        String selectedAnswer = selectedTextView.getText().toString();
        Question currentQuestion = questions.get(currentQuestionIndex);

        if (selectedAnswer.equals(currentQuestion.getCorrectAnswer())) {
            selectedTextView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            selectedTextView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            highlightCorrectAnswer(currentQuestion.getCorrectAnswer());
        }

        selectedAnswers.add(new Answer(currentQuestion.getQuestion(), selectedAnswer));
        isAnswered = true;
    }

    private void highlightCorrectAnswer(String correctAnswer) {
        if (((TextView) findViewById(R.id.answerATextView)).getText().toString().equals(correctAnswer)) {
            findViewById(R.id.answerATextView).setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else if (((TextView) findViewById(R.id.answerBTextView)).getText().toString().equals(correctAnswer)) {
            findViewById(R.id.answerBTextView).setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else if (((TextView) findViewById(R.id.answerCTextView)).getText().toString().equals(correctAnswer)) {
            findViewById(R.id.answerCTextView).setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else if (((TextView) findViewById(R.id.answerDTextView)).getText().toString().equals(correctAnswer)) {
            findViewById(R.id.answerDTextView).setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }
    }

    public void goToNextQuestion(View view) {
        if (!isAnswered) return;

        currentQuestionIndex++;
        isAnswered = false;
        resetAnswerColors();
        displayQuestion();
    }

    private void resetAnswerColors() {
        findViewById(R.id.answerATextView).setBackgroundColor(getResources().getColor(android.R.color.white));
        findViewById(R.id.answerBTextView).setBackgroundColor(getResources().getColor(android.R.color.white));
        findViewById(R.id.answerCTextView).setBackgroundColor(getResources().getColor(android.R.color.white));
        findViewById(R.id.answerDTextView).setBackgroundColor(getResources().getColor(android.R.color.white));
    }

    private void goToResult() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putParcelableArrayListExtra("selectedAnswers", selectedAnswers);
        startActivity(intent);
    }
}